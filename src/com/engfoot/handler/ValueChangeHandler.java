package com.engfoot.handler;

/**
 * @author Jacob Moss
 * @param <E> the data type of this handler
 */
public interface ValueChangeHandler<E> {

    void onChange(Value<E> value);
}
