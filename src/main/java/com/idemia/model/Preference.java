package com.idemia.model;

import io.vavr.API;

import static io.vavr.API.$;
import static io.vavr.API.Case;

public enum Preference {
    WINDOW,
    AISLE;

    public static Preference parse(String string) {
        return API.Match(string).of(
                Case($("W"), WINDOW),
                Case($("A"), AISLE)
        );
    }
}
