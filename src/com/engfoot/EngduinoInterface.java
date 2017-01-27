/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engfoot;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author zcabmos
 */
public class EngduinoInterface implements SerialPortEventListener {
    
    private ButtonHandler buttonHandler;
    private SerialPort serialPort;
        
    public EngduinoInterface(SerialPort serialPort) {
        this.serialPort = serialPort;
        try {
            serialPort.addEventListener(this);
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }
    
    public void addButtonHandler(ButtonHandler handler) {
        buttonHandler = handler;
    }
    
    public void sendMessage() {
        
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        System.out.println("EVENT: " + serialPortEvent.getEventType() + " value: " + serialPortEvent.getEventValue());
    }
}
