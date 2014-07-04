package edu.fmi.ip.skeleton.util;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorUtil {

	private ColorUtil() {
		// blank
	}

	/**
	 * {@value}
	 */
	public static final int INTENSITY_MAX = 255;

	/**
	 * {@value}
	 */
	public static final int INTENSITY_FOREGROUND = 1;

	/**
	 * {@value}
	 */
	public static final int INTENSITY_BACKGROUND = 0;

	/**
	 * {@value}
	 */
	public static final double LUMINANCE_BLUE = 0.07;

	/**
	 * {@value}
	 */
	public static final double LUMINANCE_GREEN = 0.71;

	/**
	 * {@value}
	 */
	public static final double LUMINANCE_RED = 0.21;

	public static int toRGB(int red, int green, int blue) {
		return new Color(red, green, blue).getRGB();
	}

}
