package com.idemia.model;

import io.vavr.API;

import static io.vavr.API.$;
import static io.vavr.API.Case;

public enum SeatingPolicy {
    FROM_FRONT, FROM_BACK;

    public static SeatingPolicy parse(String group) {
        return API.Match(group).of(Case($("1"), FROM_FRONT), Case($("2"), FROM_BACK));
    }
}
