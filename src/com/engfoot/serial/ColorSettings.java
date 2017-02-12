package com.engfoot.serial;

import java.awt.Color;

/**
 *
 * @author Jacob
 */
public class ColorSettings {

    public int r, g, b;
    public boolean on;

    public ColorSettings(boolean on, int red, int green, int blue) {
        this.on = on;
        r = red;
        g = green;
        b = blue;
    }

    public ColorSettings(boolean on, Color color) {
        this(on, color.getRed(), color.getGreen(), color.getBlue());
    }

}