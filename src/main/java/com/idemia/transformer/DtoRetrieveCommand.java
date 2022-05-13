package com.idemia.transformer;

import com.idemia.http.dto.AisleDto;
import com.idemia.http.dto.PersonDto;
import com.idemia.http.dto.PersonType;
import com.idemia.http.dto.PlaceDto;
import com.idemia.http.dto.SeatDto;
import com.idemia.model.Aisle;
import com.idemia.model.Command;
import com.idemia.model.Plane;
import com.idemia.model.Seat;
import io.vavr.Value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

public class DtoRetrieveCommand implements Command<List<List<PlaceDto>>> {

    @Override
    public List<List<PlaceDto>> executeOn(Plane plane) {
        return transform(plane);
    }

    private List<List<PlaceDto>> transform(Plane plane) {
        return plane.getPlaces()
                .map(p -> Match(p).of(
                        Case($(instanceOf(Aisle.class)), new AisleDto()),
                        Case($(instanceOf(Seat.class)), this::mapSeat)
                ))
                .grouped(plane.getWidth())
                .map(Value::toJavaList)
                .collect(Collectors.toList());
    }

    private SeatDto mapSeat(Seat seat) {
        return Optional.ofNullable(seat.getPerson())
                .map(PersonType::from)
                .map(PersonDto::new)
                .map(SeatDto::new)
                .orElseGet(() -> new SeatDto());
    }
}
