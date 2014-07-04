package edu.fmi.ip.skeleton;

import static edu.fmi.ip.skeleton.util.ColorUtil.INTENSITY_MAX;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class DistanceMapRetriever {

	/**
	 * Returns the maximum inscribed ball distance map, associated with the
	 * given image
	 * 
	 * @param image
	 *            the image for which the inscribed ball distance map is to be
	 *            generated
	 * @return the maximum inscribed ball distance map, associated with the
	 *         image
	 */
	public static int[][] getDistanceMap(final BufferedImage image) {
		final int[][] map = new int[image.getHeight()][image.getWidth()];
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[i].length; ++j) {
				if (Color.BLACK.getRGB() == image.getRGB(j, i)) {
					map[i][j] = 0;
				} else {
					final int red = new Color(image.getRGB(j, i)).getRed();
					map[i][j] = INTENSITY_MAX - red;
				}
			}
		}
		return map;
	}

}
