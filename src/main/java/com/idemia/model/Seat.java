package com.idemia.model;

public abstract class Seat implements Place {

    protected Person person;

    public Seat(Person person) {
        this.person = person;
    }

    public boolean isFree() {
        return person == null;
    }

    public Person getPerson() {
        return person;
    }

    public abstract Seat with(Person person);

    public boolean vipSeat() {
        return person == Person.VIP;
    }
}
