package com.idemia.model;

import io.vavr.collection.List;

public class FromFrontPolicy implements Policy {
    @Override
    public <T> List<T> sort(List<T> original) {
        return original;
    }
}
