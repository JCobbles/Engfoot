/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engfoot.util;

import com.engfoot.serial.SerialException;
import com.engfoot.serial.SerialPortWrapper;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jacob
 */
public class LightCommandBuilder {

    private final SerialPortWrapper serialPort;
    private final Map<Integer, ColorSettings> leds = new HashMap<>();

    public LightCommandBuilder(SerialPortWrapper serialPort) {
        this.serialPort = serialPort;
    }

    public LightCommandBuilder setLED(int led, boolean on, Color color) {
        return setLED(led, new ColorSettings(led, on, color));
    }

    public LightCommandBuilder setLED(int led, ColorSettings color) {
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
            command.append(String.format("1,%1$d,%2$d,%3$d,%4$d,%5$d\n",
                    entry.getKey(),
                    color.on ? "1" : "0",
                    color.r,
                    color.g,
                    color.b)
            );
        }
        return new LightCommand("");
    }

    class LightCommand {

        private final String command;

        public LightCommand(String command) {
            this.command = command;
        }

        public void execute() throws SerialException {
            serialPort.writeString(command);
        }
    }

    class ColorSettings {

        public int index, r, g, b;
        public boolean on;

        public ColorSettings(int index, boolean on, int red, int green, int blue) {
            this.on = on;
            this.index = index;
            r = red;
            g = green;
            b = blue;
        }

        public ColorSettings(int index, boolean on, Color color) {
            this(index, on, color.getRed(), color.getGreen(), color.getBlue());
        }

    }
}
