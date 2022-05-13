package com.idemia.http.dto;

import lombok.Data;

import static com.idemia.http.dto.PlaceType.AISLE;

@Data
public class AisleDto implements PlaceDto {
    @Override
    public PlaceType getType() {
        return AISLE;
    }
}
