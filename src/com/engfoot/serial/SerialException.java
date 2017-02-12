package com.engfoot.serial;

/**
 * @author Jacob Moss
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
