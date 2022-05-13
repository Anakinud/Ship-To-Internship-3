package com.idemia.model;

import io.vavr.collection.List;

public interface Policy {
    <T> List<T> sort(List<T> original);
}
