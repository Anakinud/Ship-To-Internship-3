package com.idemia.http.dto;

import com.idemia.model.SeatingPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SeatingPolicyDto {
    private SeatingPolicy type;
}
