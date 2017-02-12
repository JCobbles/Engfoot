package com.engfoot.serial;

import jssc.SerialPort;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author jacob
 */
public class SerialPortWrapper implements SerialPortInterface {

    private final SerialPort serialPort;

    public SerialPortWrapper(SerialPort port) {
        serialPort = port;
    }

    @Override
    public boolean writeString(String string) throws SerialException {
        try {
            return serialPort.writeString(string);
        } catch (SerialPortException ex) {
            throw new SerialException("Writing string failed with error: " + ex.getExceptionType());
        }
    }

    @Override
    public boolean openPort() throws ConnectionException {
        try {
            return serialPort.openPort();
        } catch (SerialPortException ex) {
            throw new ConnectionException("Error opening port with error: " + ex.getExceptionType());
        }
    }

    @Override
    public boolean setParams(int baudRate, int dataBits, int stopBits, int parity) throws ConnectionException {
        try {
            return serialPort.setParams(baudRate, dataBits, stopBits, parity);
        } catch (SerialPortException ex) {
            throw new ConnectionException("Error setting parameters with error " + ex.getExceptionType());
        }
    }

    @Override
    public boolean setFlowControlMode(int mask) throws ConnectionException {
        try {
            return serialPort.setFlowControlMode(mask);
        } catch (SerialPortException ex) {
            throw new ConnectionException("Error setting flow control mode with error " + ex.getExceptionType());
        }
    }

    @Override
    public void addEventListener(SerialPortEventListener listener, int mask) throws ConnectionException {
        try {
            serialPort.addEventListener(listener, mask);
        } catch (SerialPortException ex) {
            throw new ConnectionException("Error adding event listener with error " + ex.getExceptionType());
        }
    }

    @Override
    public boolean closePort() throws SerialException {
        try {
            return serialPort.closePort();
        } catch (SerialPortException ex) {
            throw new SerialException("Closing port failed with error: " + ex.getExceptionType());
        }
    }

    /**
     * Reads from the serial port until a newline is found
     *
     * @return buffer as a string
     * @throws SerialException
     */
    @Override
    public String readString() throws SerialException {
        StringBuilder incomingMessage = new StringBuilder();
        try {
            byte buffer[];
            byte currentByte;
            while ((buffer = serialPort.readBytes(1)) != null) {
                currentByte = buffer[0];
                if (currentByte == '\n') {
                    return incomingMessage.toString();
                } else {
                    incomingMessage.append((char) currentByte);
                }
            }
        } catch (SerialPortException ex) {
            throw new SerialException("Error in receiving string from COM-port: " + ex.getExceptionType());
        }
        return null;
    }
    
    @Override
    public boolean purgeAndClose() throws SerialException {
        if (serialPort.isOpened()) {
            try {
                serialPort.purgePort(1);
                serialPort.purgePort(2);
                serialPort.closePort();
                return true;
            } catch (SerialPortException ex) {
                throw new SerialException("Purging and closing failed with error: " + ex.getExceptionType());
            }
        } else {
            return false;
        }
    }
}
