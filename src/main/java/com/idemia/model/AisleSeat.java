package com.idemia.model;

public class AisleSeat extends Seat {
    public AisleSeat(Person person) {
        super(person);
    }

    @Override
    public Seat with(Person person) {
        return new AisleSeat(person);
    }
}
