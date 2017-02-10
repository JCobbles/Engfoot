/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engfoot.serial;

/**
 *
 * @author jacob
 */
public class SerialException extends Exception {

    private final String message;

    public SerialException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
    
}
