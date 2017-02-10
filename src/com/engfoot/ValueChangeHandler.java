package com.engfoot;

/**
 *
 * @author jacob
 */
public interface ValueChangeHandler<E> {

    void onChange(Value<E> value);
}
