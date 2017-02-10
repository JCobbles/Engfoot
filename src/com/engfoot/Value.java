/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engfoot;

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
