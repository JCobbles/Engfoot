package com.engfoot.serial;

import com.engfoot.handler.ButtonHandler;
import com.engfoot.handler.Value;
import com.engfoot.handler.ValueChangeHandler;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

/**
 *
 * @author zcabmos
 */
public class EngduinoInterface {

    private ButtonHandler buttonHandler;
    private ValueChangeHandler<Double> temperatureHandler;
    private ValueChangeHandler<Double> accelerometerHandler;
    private final SerialPortWrapper serialPort;
    private int lastButtonState = 0;

    public EngduinoInterface(SerialPortWrapper serialPort) throws ConnectionException {
        this.serialPort = serialPort;

        serialPort.openPort();
        serialPort.setParams(
                SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

        serialPort.addEventListener(new SerialListener(), SerialPort.MASK_RXCHAR);
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

    public void sendMessage(String message) throws SerialException {
        serialPort.writeString(message);
    }

    public boolean disconnect() throws SerialException {
        return serialPort.closePort();
    }

    private void process(String message) {
        System.out.println("MESSAGE: " + message);
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
            case "2": // button
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
            case "4": // magnetometer
                break;
        }
    }

    public class SerialListener implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String input = serialPort.readString();
                    if (input != null) {
                        process(input);
                    }
                } catch (SerialException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }

    }
}
