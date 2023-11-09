package org.example;

public interface Filter<T> {
    T execute (T input);
}