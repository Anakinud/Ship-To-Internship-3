package com.idemia.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.idemia.http.dto.PlaceType.SEAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto implements PlaceDto {
    private PersonDto person;

    @Override
    public PlaceType getType() {
        return SEAT;
    }
}
