package com.idemia.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = AisleDto.class, name = "AISLE"),
        @JsonSubTypes.Type(value = SeatDto.class, name = "SEAT") })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface PlaceDto {
    PlaceType getType();
}
