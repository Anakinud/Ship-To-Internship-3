package com.idemia.http.dto;

import com.idemia.model.Person;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

public enum PersonType {
    NORMAL, VIP;

    public static PersonType from(Person p) {
        return Match(p).of(
                Case($(Person.NORMAL), NORMAL),
                Case($(Person.VIP), VIP)
        );
    }

    public Person toPerson() {
        return Match(this).of(
                Case($(NORMAL), Person.NORMAL),
                Case($(VIP), Person.VIP)
        );
    }
}
