package com.idemia.model;

public class WindowAisleSeat extends Seat {
    public WindowAisleSeat(Person person) {
        super(person);
    }

    @Override
    public Seat with(Person person) {
        return new WindowAisleSeat(person);
    }
}
