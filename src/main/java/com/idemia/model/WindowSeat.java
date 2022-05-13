package com.idemia.model;

public class WindowSeat extends Seat {
    public WindowSeat(Person person) {
        super(person);
    }

    @Override
    public Seat with(Person person) {
        return new WindowSeat(person);
    }
}
