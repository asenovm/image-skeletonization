package edu.fmi.ip.skeleton;

import static edu.fmi.ip.skeleton.util.ColorUtil.INTENSITY_MAX;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageMapRetriever {

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
	public static int[][] getBallMap(final BufferedImage image) {
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

	public static int[][] getDistanceMap(final int[][] colors) {
		final int[][] map = new int[colors.length][colors[0].length];
		for (int i = 0; i < colors.length; ++i) {
			for (int j = 0; j < colors[i].length; ++j) {
				if (colors[i][j] > 0) {
					map[i][j] = getClosestBackgroundDistance(colors, j, i);
				}
			}
		}
		return map;
	}

	private static int getDistance(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	private static int getClosestBackgroundDistance(final int[][] image,
			final int x, final int y) {
		int distance = Integer.MAX_VALUE;
		for (int i = 0; i < image.length; ++i) {
			for (int j = 0; j < image[i].length; ++j) {
				if (image[i][j] == 0) {
					int currentDistance = getDistance(j, i, x, y);
					if (currentDistance < distance) {
						distance = currentDistance;
					}
				}
			}
		}
		return distance;
	}

}
