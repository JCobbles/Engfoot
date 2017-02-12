package com.engfoot.serial;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Internal utility class to easily create commands that turn on/off leds on the engduino
 * 
 * @author Jacob Moss
 */
public class LightCommandBuilder {

    private final SerialPortWrapper serialPort;
    private final Map<Integer, ColorSettings> leds = new HashMap<>();

    protected LightCommandBuilder(SerialPortWrapper serialPort) {
        this.serialPort = serialPort;
    }

    public LightCommandBuilder setLED(int led, boolean on, Color color) {
        return setLED(led, new ColorSettings(on, color));
    }

    public LightCommandBuilder setLED(int led, ColorSettings color) {
        if (led < 1 || led > 15) {
            throw new IllegalArgumentException("LED number has to be between 1 and 15 (inclusive) but was " + led);
        }
        if (leds.containsKey(led)) {
            leds.replace(led, color);
        } else {
            leds.put(led, color);
        }
        return this;
    }

    public LightCommand build() {
        StringBuilder command = new StringBuilder();
        for (Map.Entry<Integer, ColorSettings> entry : leds.entrySet()) {
            ColorSettings color = entry.getValue();
            command.append(String.format("1,%1$d,%2$s,%3$d,%4$d,%5$d\n",
                    entry.getKey(),
                    color.on ? "1" : "0",
                    color.r,
                    color.g,
                    color.b)
            );
        }
        return new LightCommand(command.toString());
    }

    public class LightCommand {

        private final String command;

        public LightCommand(String command) {
            this.command = command;
        }

        public void execute() throws SerialException {
            serialPort.writeString(command);
        }
    }

}
