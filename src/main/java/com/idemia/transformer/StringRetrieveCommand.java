package com.idemia.transformer;

import com.idemia.model.Aisle;
import com.idemia.model.Command;
import com.idemia.model.Person;
import com.idemia.model.Plane;
import com.idemia.model.Seat;

import static com.idemia.model.Person.NORMAL;
import static com.idemia.model.Person.VIP;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

public class StringRetrieveCommand implements Command<String> {

    @Override
    public String executeOn(Plane plane) {
        return transform(plane);
    }

    private String transform(Plane plane) {
        return plane.getPlaces()
                .map(p -> Match(p).of(
                        Case($(instanceOf(Aisle.class)), " "),
                        Case($(instanceOf(Seat.class)), this::mapSeat)
                ))
                .grouped(plane.getWidth())
                .map(s -> s.foldLeft("", (l, r) -> l + r))
                .map(s -> "|" + s + "|\n")
                .foldLeft("", (l, r) -> l + r);
    }

    private String mapSeat(Seat seat) {
        return seat.isFree() ? "O" : mapPerson(seat.getPerson());
    }

    private String mapPerson(Person person) {
        return Match(person).of(
                Case($(NORMAL), "X"),
                Case($(VIP), "V"),
                Case($(), "O")
        );
    }
}
