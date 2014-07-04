package edu.fmi.ip.skeleton.util;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorsUtil {

	private ColorsUtil() {
		// blank
	}

	public static int[][] getImageColors(final BufferedImage image) {
		final int[][] colors = new int[image.getHeight()][image.getWidth()];
		for (int i = 0; i < colors.length; ++i) {
			for (int j = 0; j < colors[i].length; ++j) {
				if (Color.BLACK.getRGB() == image.getRGB(j, i)) {
					colors[i][j] = 0;
				} else {
					colors[i][j] = 1;
				}
			}
		}
		return colors;
	}

}
