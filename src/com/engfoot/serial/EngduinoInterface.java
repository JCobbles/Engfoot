package com.engfoot.serial;

import com.engfoot.Engfoot;
import com.engfoot.handler.ButtonHandler;
import com.engfoot.handler.Value;
import com.engfoot.handler.ValueChangeHandler;
import java.util.Arrays;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

/**
 * This class handles all implemented communication to the Engduino
 * Do not create directly - use <code>Engfoot</code>
 * @author Jacob Moss
 */
public class EngduinoInterface  {

    private ButtonHandler buttonHandler;
    private ValueChangeHandler<Float> temperatureHandler;
    private ValueChangeHandler<Double> accelerometerHandler;
    private ValueChangeHandler<Float> lightSensorHandler;
    private ValueChangeHandler<float[]> magnetometerHandler;
    private final SerialPortWrapper serialPort;
    private int lastButtonState = 0;

    public EngduinoInterface(SerialPortWrapper serialPort) throws ConnectionException {
        this.serialPort = serialPort;
        try {
            serialPort.purgeAndClose();
        } catch (SerialException ex) {
            ex.printStackTrace();
        }
        serialPort.openPort();
        Engfoot.addUsedPort(serialPort);
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

    public void addTemperatureHandler(ValueChangeHandler<Float> handler) {
        this.temperatureHandler = handler;
    }

    public void addAccelerometerHandler(ValueChangeHandler<Double> handler) {
        this.accelerometerHandler = handler;
    }
    
    public void addLightSensorHandler(ValueChangeHandler<Float> handler) {
        this.lightSensorHandler = handler;
    }
    
    public void addMagnetometerHandler(ValueChangeHandler<float[]> handler) {
        this.magnetometerHandler = handler;
    }
    
    public LightCommandBuilder createLightCommand() {
        return new LightCommandBuilder(serialPort);
    }

    public void sendMessage(String message) throws SerialException {
        serialPort.writeString(message);
    }

    public boolean disconnect() throws SerialException {
        return serialPort.closePort();
    }

    private void process(String message) {
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
                if (temperatureHandler != null) try {
                    temperatureHandler.onChange(new Value(Float.parseFloat(value)));
                } catch (NumberFormatException e) {
                }
                break;
            case "4": // magnetometer
                if (magnetometerHandler != null) try {
                    Float[] values = Arrays.stream(value.split(" "))
                            .map(Float::parseFloat)
                            .toArray(Float[]::new);
                    magnetometerHandler.onChange(new Value(new float[] { values[0], values[1], values[2] }));
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                }
                break;
            case "5": // light
                if (lightSensorHandler != null) try {
                    lightSensorHandler.onChange(new Value(Float.parseFloat(value)));
                } catch (NumberFormatException e) {
                }
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
