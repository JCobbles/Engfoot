package com.engfoot.handler;

/**
 *
 * @author jacob
 */
public interface ValueChangeHandler<E> {

    void onChange(Value<E> value);
}
