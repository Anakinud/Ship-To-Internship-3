package com.idemia.model;

public class NormalSeat extends Seat {
    public NormalSeat(Person person) {
        super(person);
    }

    @Override
    public Seat with(Person person) {
        return new NormalSeat(person);
    }
}
