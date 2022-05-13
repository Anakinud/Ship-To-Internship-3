package com.idemia.parser;

import com.idemia.http.dto.AisleDto;
import com.idemia.http.dto.PersonDto;
import com.idemia.http.dto.PersonType;
import com.idemia.http.dto.PlaceDto;
import com.idemia.http.dto.SeatDto;
import com.idemia.model.Aisle;
import com.idemia.model.AisleSeat;
import com.idemia.model.NormalSeat;
import com.idemia.model.Person;
import com.idemia.model.Place;
import com.idemia.model.Plane;
import com.idemia.model.WindowAisleSeat;
import com.idemia.model.WindowSeat;
import io.vavr.collection.List;

import java.util.Optional;

public class DtoParser {

    public Plane parse(java.util.List<java.util.List<PlaceDto>> dtos) {
        List<Place> places = io.vavr.collection.List.of();
        for (int i = 0; i < dtos.size(); i++) {
            places = places.append(mapWindowPlaces(dtos.get(i).get(0), next(dtos.get(i), 0)));
            for (int j = 1; j < dtos.get(i).size() - 1; j++) {
                if (dtos.get(i).get(j) instanceof AisleDto) {
                    places = places.append(new Aisle());
                } else {
                    places = places.append(mapWindowPlaces(dtos.get(i).get(j), previous(dtos.get(i), j), next(dtos.get(i), j)));
                }
            }
            int lastIndex = dtos.get(i).size() - 1;
            places = places.append(mapWindowPlaces(dtos.get(i).get(lastIndex), previous(dtos.get(i), lastIndex)));
        }
        return new Plane(places, places.size() / dtos.size());
    }

    private Place mapWindowPlaces(PlaceDto current, Optional<PlaceDto> neighbourPlace) {
        Person person = getPerson(current);
        if (isAisle(neighbourPlace)) {
            return new WindowAisleSeat(person);
        } else {
            return new WindowSeat(person);
        }
    }

    private Place mapWindowPlaces(PlaceDto current, Optional<PlaceDto> left, Optional<PlaceDto> right) {
        Person person = getPerson(current);
        if ((isAisle(left)) || isAisle(right)) {
            return new AisleSeat(person);
        } else {
            return new NormalSeat((person));
        }
    }

    private Person getPerson(PlaceDto current) {
        return Optional.ofNullable((SeatDto) current)
                .map(SeatDto::getPerson)
                .map(PersonDto::getType)
                .map(PersonType::toPerson)
                .orElse(null);
    }

    private boolean isAisle(Optional<PlaceDto> right) {
        return right.isPresent() && right.get() instanceof AisleDto;
    }

    private Optional<PlaceDto> next(java.util.List<PlaceDto> placeDtos, int i) {
        return i + 1 >= placeDtos.size() ? Optional.empty() : Optional.ofNullable(placeDtos.get(i + 1));
    }

    private Optional<PlaceDto> previous(java.util.List<PlaceDto> placeDtos, int i) {
        return i - 1 < 0 ? Optional.empty() : Optional.ofNullable(placeDtos.get(i - 1));
    }
}
