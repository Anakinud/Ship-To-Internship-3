package com.idemia.model;

public interface Command<T> {

    T executeOn(Plane plane);
}
