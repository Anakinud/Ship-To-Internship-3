package com.idemia.model;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

public enum Person {
    NORMAL,
    VIP;

    public static Person parse(String value) {
        return Match(value).of(
                Case($("X"), NORMAL),
                Case($("V"), VIP),
                Case($(), () -> null)
        );
    }
}
