/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.engfoot.handler.ButtonHandler;
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
import jssc.SerialPortException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author jacob
 */
@RunWith(MockitoJUnitRunner.class)
public class EngfootTests {

    @Mock
    SerialPort serialPort;

    private Engfoot engfoot;
    private EngduinoInterface footInterface;
    private SerialPortWrapper serialPortWrapper;

    public EngfootTests() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ConnectionException, SerialPortException {
        MockitoAnnotations.initMocks(this);
        serialPortWrapper = new SerialPortWrapper(serialPort);

        engfoot = Mockito.mock(Engfoot.class);

        Mockito.when(serialPort.openPort()).thenReturn(true);
        footInterface = new EngduinoInterface(serialPortWrapper);

        Mockito.when(engfoot.connect()).thenReturn(footInterface);
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
        String[] ports = new Engfoot().getSerialPorts();
        assert ports.length > 0;
    }

    @Test
    public void readsCorrectStringFromSerialPort() throws SerialPortException {
        Mockito.when(serialPort.readBytes(1)).thenReturn("2".getBytes(), ":".getBytes(), " ".getBytes(), "1".getBytes(), "\n".getBytes());
        try {
            String response = serialPortWrapper.readString();
            assertEquals("2: 1", response);
        } catch (SerialException ex) {
            fail(ex.getMessage());
        }
    }
}
