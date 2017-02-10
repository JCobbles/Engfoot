/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.engfoot.ButtonHandler;
import com.engfoot.serial.ConnectionException;
import com.engfoot.serial.EngduinoInterface;
import com.engfoot.Engfoot;
import com.engfoot.serial.SerialException;
import com.engfoot.serial.SerialPortInterface;
import com.engfoot.serial.SerialPortWrapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author jacob
 */
public class EngfootTests {

    private Engfoot engfoot;
    private EngduinoInterface footInterface;

    public EngfootTests() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        engfoot = new Engfoot();
    }

    @After
    public void tearDown() {
        if (footInterface != null) {
            try {
                footInterface.disconnect();
            } catch (SerialException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Test
    public void canCheckSerialPorts() {
        String[] ports = engfoot.getSerialPorts();
        assert ports.length > 0;
    }

    @Test
    public void canConnect() {
        try {
            footInterface = engfoot.connect();
        } catch (ConnectionException ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void canReceiveInput() {
        try {
            footInterface = engfoot.connect();
        } catch (ConnectionException ex) {
            fail("Cannot connect, make sure engduino is connected to computer");
        }
        SerialPortInterface serialPort = Mockito.mock(SerialPortInterface.class);
        try {
            Mockito.when(serialPort.readString()).thenReturn("2: 0");
        } catch (SerialException ex) {
            fail();
        }
//        try {
//            footInterface.new SerialListener();
//        } catch (ConnectionException ex) {
//            fail("Could not add eventlistener");
//        }
        footInterface.addButtonHandler(new ButtonHandler() {
            @Override
            public void handle() {
                System.out.println("handle");
            }
        });

    }
}
