
import com.engfoot.serial.ConnectionException;
import com.engfoot.serial.EngduinoInterface;
import com.engfoot.Engfoot;
import com.engfoot.serial.SerialException;
import com.engfoot.serial.SerialPortWrapper;
import com.engfoot.serial.ColorSettings;
import com.engfoot.serial.LightCommandBuilder.LightCommand;
import java.awt.Color;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
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

    }

    @Test
    public void canCheckSerialPorts() {
        String[] ports = new Engfoot().getSerialPorts();
        assert ports.length > 0;
    }

    @Test
    public void readsCorrectStringFromSerialPort() throws SerialPortException, SerialException {
        Mockito.when(serialPort.readBytes(1)).thenReturn("2".getBytes(), ":".getBytes(), " ".getBytes(), "1".getBytes(), "\n".getBytes());
        String response = serialPortWrapper.readString();
        assertEquals("2: 1", response);
    }

    @Test
    public void writesLightCommandCorrectly() throws SerialPortException, SerialException {
        LightCommand c = footInterface.createLightCommand().setLED(12, new ColorSettings(true, 255, 100, 100)).build();
        c.execute();
        Mockito.verify(serialPort, Mockito.times(1)).writeString("1,12,1,255,100,100\n");
    }
    
    @Test
    public void writesSecondLightCommandCorrectly() throws SerialPortException, SerialException {
        LightCommand c = footInterface.createLightCommand().setLED(2, true, Color.PINK).build();
        c.execute();
        Mockito.verify(serialPort, Mockito.times(1)).writeString("1,2,1,255,175,175\n");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfInvalidLightValuesEntered() throws SerialPortException, SerialException {
        LightCommand c = footInterface.createLightCommand().setLED(16, new ColorSettings(true, 255, 100, 100)).build();
        c.execute();
    }
}
