/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engfoot.serial;

import jssc.SerialPortEventListener;

/**
 *
 * @author jacob
 */
public interface SerialPortInterface {

    boolean openPort() throws ConnectionException;

    boolean closePort() throws SerialException;

    boolean setParams(int baudRate, int dataBits, int stopBits, int parity) throws ConnectionException;

    boolean setFlowControlMode(int mask) throws ConnectionException;

    void addEventListener(SerialPortEventListener listener, int mask) throws ConnectionException;

    boolean writeString(String string) throws SerialException;

    String readString() throws SerialException;

    boolean purgeAndClose() throws SerialException;
}
