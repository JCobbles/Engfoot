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
    private ValueChangeHandler<Double> temperatureHandler;
    private ValueChangeHandler<Double> accelerometerHandler;
    private SerialPort serialPort;
    private StringBuilder incomingMessage = new StringBuilder();
    private int lastButtonState = 0;

    public EngduinoInterface(SerialPort serialPort) {
        this.serialPort = serialPort;
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    public void addButtonHandler(ButtonHandler handler) {
        buttonHandler = handler;
    }

    public void addTemperatureHandler(ValueChangeHandler<Double> handler) {
        this.temperatureHandler = handler;
    }

    public void addAccelerometerHandler(ValueChangeHandler<Double> handler) {
        this.accelerometerHandler = handler;
    }

    public void sendMessage(String message) {

    }

    private void process(String message) {
        System.out.println(message);
        String[] splitMessage = message.split(":");
        String key, value;
        try {
            key = splitMessage[0].trim();
            value = splitMessage[1].trim();
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        switch (key) {
            case "1": // accelerometer
                try {
                    if (accelerometerHandler != null) {
                        accelerometerHandler.onChange(new Value(Double.parseDouble(value)));
                    }
                } catch (NumberFormatException e) {

                }
                break;
            case "2":
                try {
                    int state = Integer.parseInt(value);
                    if (state != lastButtonState) {
                        lastButtonState = state;
                        if (buttonHandler != null) {
                            buttonHandler.handle();
                        }
                    }
                } catch (NumberFormatException e) {

                }
                break;
            case "3": // temperature
                try {
                    if (temperatureHandler != null) {
                        temperatureHandler.onChange(new Value(Double.parseDouble(value)));
                    }
                } catch (NumberFormatException e) {

                }
                break;
            case "4":
                break;
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                byte buffer[] = serialPort.readBytes();
                for (byte b : buffer) {
                    if (b == '\n') {
                        process(incomingMessage.toString());
                        incomingMessage.setLength(0);
                    } else {
                        incomingMessage.append((char) b);
                    }
                }
            } catch (SerialPortException ex) {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }
}
