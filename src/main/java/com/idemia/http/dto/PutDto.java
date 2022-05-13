package com.idemia.http.dto;

import com.idemia.model.Preference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PutDto {
    private PersonType type;
    private Preference seatTypePreference;

    public PutDto(PersonType type) {
        this.type = type;
    }
}
