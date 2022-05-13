package com.idemia.model;

import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.idemia.model.HexUtils.byte2hex;
import static com.idemia.model.Person.NORMAL;
import static com.idemia.model.Person.VIP;
import static com.idemia.model.Preference.AISLE;
import static com.idemia.model.Preference.WINDOW;
import static com.idemia.model.SeatingPolicy.FROM_BACK;
import static com.idemia.model.SeatingPolicy.FROM_FRONT;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static java.util.Optional.empty;

public class Plane {
    private Policy policy = new FromFrontPolicy();
    private List<Place> places;
    private final int width;

    public Plane(List<Place> places, int width) {
        this.width = width;
        this.places = places;
    }

    public List<Place> getPlaces() {
        return List.ofAll(places);
    }

    public int getWidth() {
        return width;
    }

    private void remove(int placeIndex) {
        this.update(placeIndex, (Person) null);
    }

    private static boolean isSeat(Place place) {
        return place instanceof Seat;
    }

    private void update(Integer index, Person person) {
        this.update(index, s -> s.with(person));
    }

    private void update(Integer index, Function<Seat, Seat> updateFunction) {
        this.places = this.places.update(index, updateFunction.apply((Seat) places.get(index)));
    }

    private Stream<Place> getPlacesAroundIndex(Integer index) {
        return seatsSquare(index)
                .map(i -> places.get(i));
    }

    private Stream<Integer> seatsSquare(Integer index) {
        int columns = this.width;
        int rows = this.places.size() / columns;
        int currentRow = index / columns;
        int currentColumn = index % columns;
        Set<Integer> vipSquareIndexes = new HashSet<>();
        for (int i = Math.max(0, currentRow - 1); i <= Math.min(currentRow + 1, rows - 1); i++) {
            for (int j = Math.max(0, currentColumn - 1); j <= Math.min(currentColumn + 1, columns - 1); j++) {
                vipSquareIndexes.add(i * columns + j);
            }
        }
        vipSquareIndexes.remove(index);
        return vipSquareIndexes.stream();
    }

    private List<Tuple2<Place, Integer>> getPlacesWithPolicy() {
        return this.policy.sort(this.places.iterator()
                .zipWithIndex()
                .toList());
    }

    public <T> T execute(Command<T> command) {
        return command.executeOn(this);
    }

    public static class UnoccupiedSignedCommand implements Command<Unoccupied> {

        private String key;

        public UnoccupiedSignedCommand(String key) {
            this.key = key;
        }

        @Override
        @SneakyThrows
        public Unoccupied executeOn(Plane plane) {
            int unoccupied = plane.getPlaces()
                    .filter(instanceOf(Seat.class))
                    .map(Seat.class::cast)
                    .count(Seat::isFree);
            ByteBuffer unoccupiedBuffer = ByteBuffer.allocate(Integer.BYTES).putInt(unoccupied);
            unoccupiedBuffer.rewind();
            var sk = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            var mac = Mac.getInstance("HmacSHA256");
            mac.init(sk);

            var signatureBytes = mac.doFinal(unoccupiedBuffer.array());

            var signature = new StringBuilder();
            for (byte b : signatureBytes) {
                signature.append(byte2hex(b));
            }
            return new Unoccupied(unoccupied, signature.toString());
        }
    }

    public static class ChangePolicyCommand implements Command<Void> {

        private SeatingPolicy policy;

        public ChangePolicyCommand(SeatingPolicy policy) {
            this.policy = policy;
        }

        @Override
        public Void executeOn(Plane plane) {
            plane.policy = Match(policy).of(
                    Case($(FROM_FRONT), new FromFrontPolicy()),
                    Case($(FROM_BACK), new FromBackPolicy())
            );
            return null;
        }

    }

    public static class PutCommand implements Command<Void> {

        private final Person person;

        private final Optional<Preference> preference;

        public PutCommand(Person person) {
            this.person = person;
            this.preference = empty();
        }

        public PutCommand(Person person, Preference preference) {
            this.person = person;
            this.preference = Optional.ofNullable(preference);
        }

        @Override
        public Void executeOn(Plane plane) {
            Match(person).of(
                    Case($(NORMAL), put(Function2.of(this::findSeatForRegular).apply(plane))),
                    Case($(VIP), put(Function2.of(this::findSeatForVip).apply(plane)))
            ).peek(index -> plane.update(index, person));
            return null;
        }

        private Option<Integer> put(Function<Predicate<Place>, Option<Integer>> findSeatPolicy) {
            return preference
                    .map(pref -> Match(pref).of(
                            Case($(WINDOW), () -> findSeatPolicy.apply(p -> p instanceof WindowSeat || p instanceof WindowAisleSeat)
                                    .orElse(() -> findSeatPolicy.apply(p -> isSeat(p)))),
                            Case($(AISLE), () -> findSeatPolicy.apply(p -> p instanceof AisleSeat || p instanceof WindowAisleSeat)
                                    .orElse(() -> findSeatPolicy.apply(p -> isSeat(p))))
                    ))
                    .orElseGet(() -> findSeatPolicy.apply(p -> isSeat(p)));
        }

        private Option<Integer> findSeatForRegular(Plane plane, Predicate<Place> preferredPlace) {
            return plane.getPlacesWithPolicy()
                    .filter(t -> preferredPlace.test(t._1))
                    .map(s -> Tuple.of((Seat) s._1, s._2))
                    .filter(s -> s._1.isFree())
                    .find(s -> plane.getPlacesAroundIndex(s._2).noneMatch(this::noVipAround))
                    .map(t -> t._2);
        }

        private Option<Integer> findSeatForVip(Plane plane, Predicate<Place> preferredPlace) {
            return plane.getPlacesWithPolicy()
                    .filter(t -> preferredPlace.test(t._1))
                    .map(s -> Tuple.of((Seat) s._1, s._2))
                    .filter(s -> s._1.isFree())
                    .find(s -> plane.getPlacesAroundIndex(s._2).allMatch(this::spaceForVip))
                    .map(t -> t._2);
        }

        private boolean spaceForVip(Place place) {
            return Match(place).of(
                    Case($(instanceOf(Aisle.class)), true),
                    Case($(instanceOf(Seat.class)), s -> s.isFree())
            );
        }

        private boolean noVipAround(Place place) {
            return Match(place).of(
                    Case($(instanceOf(Aisle.class)), false),
                    Case($(instanceOf(Seat.class)), s -> s.vipSeat())
            );
        }

    }

    public static class TakeCommand implements Command<Void> {

        private final int column;

        private final int row;

        public TakeCommand(int row, int column) {
            this.column = column;
            this.row = row;
        }

        @Override
        public Void executeOn(Plane plane) {
            int actualColumnIndex = column - 1;
            int actualRow = plane.width * (row - 1);
            for (int i = 0; i <= actualColumnIndex; i++) {
                if (plane.places.get(actualRow + i) instanceof Aisle) {
                    actualColumnIndex++;
                }
            }
            int placeIndex = actualColumnIndex + actualRow;
            plane.remove(placeIndex);
            return null;
        }
    }
}
