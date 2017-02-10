package com.engfoot;

import com.engfoot.serial.EngduinoInterface;
import com.engfoot.serial.ConnectionException;
import com.engfoot.serial.SerialPortWrapper;
import jssc.SerialPort;
import jssc.SerialPortList;

/**
 *
 * @author zcabmos
 */
public class Engfoot {
        
    public Engfoot() {
    }
    
    /**
     * 
     * @return list of available ports, or an empty array if none available
     */
    public String[] getSerialPorts() {
        return jssc.SerialPortList.getPortNames();
    }
    
    public EngduinoInterface connect(String serialPort) throws ConnectionException {
        if (serialPort == null) {
            if (jssc.SerialPortList.getPortNames().length > 0) {
                serialPort = SerialPortList.getPortNames()[0];
            } else {
                throw new ConnectionException("No ports available");
            }
        }
        
        return new EngduinoInterface(new SerialPortWrapper(serialPort));
    }
    
    public EngduinoInterface connect() throws ConnectionException {
        return connect(null);
    }
}
