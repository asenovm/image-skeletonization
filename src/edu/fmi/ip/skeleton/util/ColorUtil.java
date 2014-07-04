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

	public static int distanceToColor(final int distance) {
		return new Color(INTENSITY_MAX - distance, INTENSITY_MAX - distance,
				INTENSITY_MAX - distance, INTENSITY_MAX).getRGB();
	}

	public static int[][] convertToBinaryColors(BufferedImage image) {

		final int[][] result = new int[image.getHeight()][image.getWidth()];

		for (int i = 0; i < image.getHeight(); ++i) {
			for (int j = 0; j < image.getWidth(); ++j) {
				final int rgb = image.getRGB(j, i);
				final Color color = new Color(rgb);
				if (Color.WHITE.equals(color)) {
					result[i][j] = INTENSITY_FOREGROUND;
				} else {
					result[i][j] = INTENSITY_BACKGROUND;
				}
			}
		}

		return result;
	}

}
