package com.engfoot.handler;

/**
 *
 * @author Jacob Moss
 */
public class Value<E> {

    private final E value;

    public Value(E value) {
        this.value = value;
    }

    public E get() {
        return value;
    }
}
