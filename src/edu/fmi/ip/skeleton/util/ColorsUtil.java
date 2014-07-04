package edu.fmi.ip.skeleton.util;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorsUtil {

	private ColorsUtil() {
		// blank
	}

	public static int[][] getDistanceMap(final BufferedImage image) {
		final int[][] map = new int[image.getHeight()][image.getWidth()];
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[i].length; ++j) {
				if (Color.BLACK.getRGB() == image.getRGB(j, i)) {
					map[i][j] = 0;
				} else {
					final int red = new Color(image.getRGB(j, i)).getRed();
					map[i][j] = 255 - red;
				}
			}
		}
		return map;
	}

}
