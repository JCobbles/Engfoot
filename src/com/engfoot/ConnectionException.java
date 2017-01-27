/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engfoot;

/**
 *
 * @author zcabmos
 */
public class ConnectionException extends Exception {

    private String message;
    
    public ConnectionException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message; //To change body of generated methods, choose Tools | Templates.
    }
    
}
