package com.idemia.parser;

import com.idemia.model.Aisle;
import com.idemia.model.AisleSeat;
import com.idemia.model.Command;
import com.idemia.model.NormalSeat;
import com.idemia.model.Person;
import com.idemia.model.Place;
import com.idemia.model.Plane;
import com.idemia.model.Preference;
import com.idemia.model.SeatingPolicy;
import com.idemia.model.WindowAisleSeat;
import com.idemia.model.WindowSeat;
import io.vavr.collection.List;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

public class StringParser {

    public static final String AISLE_GROUP = "Aisle";
    public static final String AISLE_SEAT_GROUP = "AisleSeat";
    public static final String WINDOW_GROUP = "Window";
    public static final String WINDOW_WITH_AISLE_GROUP = "WindowWithAisle";
    public static final String NORMAL_SEATS_GROUP = "OtherSeat";
    private static final Pattern DEFINITION_REGEX = Pattern.compile("(?<WindowWithAisle>((?<=\\|)[VXO](?= ))|((?<= )[VXO](?=\\|)))|" +
                                                                    "(?<Window>((?<=\\|)[VXO])|([VXO](?=\\|)))|" +
                                                                    "(?<AisleSeat>((?<= )[VXO])|([VXO](?= )))|" +
                                                                    "(?<OtherSeat>[VXO])|" +
                                                                    "(?<Aisle>[ ])");
    private static final Pattern PUT_SEAT_REGEXP = Pattern.compile("P ([XV])(:[WA])?");
    private static final Pattern TAKE_REGEX = Pattern.compile("(\\d+):(\\d+)");
    private static final Pattern CHANGE_POLICY_REGEX = Pattern.compile("S (\\d)$");

    public Plane parse(java.util.List<String> input) {
        List<Place> places = input.stream()
                .flatMap(l -> parseLine(l))
                .collect(List.collector());

        return new Plane(places, places.size() / input.size());
    }

    private Stream<Place> parseLine(String l) {
        java.util.List<Place> places = new ArrayList<>();
        Matcher matcher = DEFINITION_REGEX.matcher(l);
        while (matcher.find()) {
            if (matcher.group(AISLE_GROUP) != null) {
                places.add(new Aisle());
            } else if (matcher.group(WINDOW_GROUP) != null) {
                places.add(new WindowSeat(Person.parse(matcher.group(WINDOW_GROUP))));
            } else if (matcher.group(WINDOW_WITH_AISLE_GROUP) != null) {
                places.add(new WindowAisleSeat(Person.parse(matcher.group(WINDOW_WITH_AISLE_GROUP))));
            } else if (matcher.group(AISLE_SEAT_GROUP) != null) {
                places.add(new AisleSeat(Person.parse(matcher.group(AISLE_SEAT_GROUP))));
            } else {
                places.add(new NormalSeat(Person.parse(matcher.group(NORMAL_SEATS_GROUP))));
            }
        }
        return places.stream();
    }

    public Command parseCommand(String input) {
        return Match(input).of(
                Case($(s -> s.startsWith("P")), s -> {
                    Matcher matcher = PUT_SEAT_REGEXP.matcher(s);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException("Hamuj się z taką komendą Put: " + s);
                    }

                    return Optional.ofNullable(matcher.group(2))
                            .map(g -> new Plane.PutCommand(
                                    Person.parse(matcher.group(1)),
                                    Preference.parse(matcher.group(2).replace(":", ""))))
                            .orElse(new Plane.PutCommand(Person.parse(matcher.group(1))));
                }),
                Case($(s -> s.startsWith("T")), s -> {
                    Matcher matcher = TAKE_REGEX.matcher(s);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException("Hamuj się z taką komendą Take: " + s);
                    }
                    return new Plane.TakeCommand(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)));
                }),
                Case($(s -> s.startsWith("S")), s -> {
                    Matcher matcher = CHANGE_POLICY_REGEX.matcher(s);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException("Hamuj się z taką komendą S: " + s);
                    }
                    return new Plane.ChangePolicyCommand(SeatingPolicy.parse(matcher.group(1)));
                })
        );
    }
}
