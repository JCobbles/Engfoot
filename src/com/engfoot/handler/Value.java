package com.engfoot.handler;

/**
 *
 * @author jacob
 */
public class Value<E> {

    private E value;

    public Value(E value) {
        this.value = value;
    }

    public E get() {
        return value;
    }
}
