package edu.fmi.ir.skeleton;

import java.util.LinkedList;
import java.util.List;

public class ImageThinner {

	private class Point {
		private int x;

		private int y;

		public Point(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
	}

	public int[][] getThinnedImage(final int[][] image) {
		boolean hasDeleted = true;
		while (hasDeleted) {
			hasDeleted = false;

			hasDeleted = firstPass(image);
			hasDeleted = secondPass(image) || hasDeleted;
		}
		return image;
	}

	private boolean secondPass(final int[][] image) {
		final List<Point> toRemove = new LinkedList<Point>();
		boolean result = false;
		for (int i = 1; i < image.length - 1; ++i) {
			for (int j = 1; j < image[i].length - 1; ++j) {
				final int aValue = getA(image, j, i);
				final int bValue = getB(image, j, i);
				if (isRemovingSecondStep(image, i, j, aValue, bValue)) {
					result = true;
					toRemove.add(new Point(j, i));
				}
			}
		}

		for (final Point point : toRemove) {
			image[point.y][point.x] = 0;
		}
		return result;
	}

	private boolean firstPass(final int[][] image) {
		final List<Point> toRemove = new LinkedList<Point>();
		boolean result = false;
		for (int i = 1; i < image.length - 1; ++i) {
			for (int j = 1; j < image[i].length - 1; ++j) {
				final int aValue = getA(image, j, i);
				final int bValue = getB(image, j, i);
				if (isRemovingFirstStep(image, i, j, aValue, bValue)) {
					result = true;
					toRemove.add(new Point(j, i));
				}
			}
		}

		for (final Point point : toRemove) {
			image[point.y][point.x] = 0;
		}
		return result;
	}

	private boolean isRemovingSecondStep(final int[][] image, int y, int x,
			final int aValue, final int bValue) {
		return bValue >= 2 && bValue <= 6 && aValue == 1 && image[y][x] == 1
				&& image[y - 1][x] * image[y][x + 1] * image[y][x - 1] == 0
				&& image[y - 1][x] * image[y + 1][x] * image[y][x - 1] == 0;
	}

	private boolean isRemovingFirstStep(final int[][] image, int y, int x,
			int aValue, int bValue) {
		return bValue >= 2 && bValue <= 6 && aValue == 1 && image[y][x] == 1
				&& image[y - 1][x] * image[y][x + 1] * image[y + 1][x] == 0
				&& image[y][x + 1] * image[y + 1][x] * image[y][x - 1] == 0;
	}

	public int getB(final int[][] image, final int x, final int y) {
		return image[y - 1][x - 1] + image[y - 1][x] + image[y - 1][x + 1]
				+ image[y][x - 1] + image[y][x + 1] + image[y + 1][x - 1]
				+ image[y + 1][x] + image[y + 1][x + 1];
	}

	public int getA(final int[][] image, final int x, final int y) {
		int transitions = 0;

		if (image[y - 1][x] == 0 && image[y - 1][x + 1] == 1) {
			++transitions;
		}

		if (image[y - 1][x + 1] == 0 && image[y][x + 1] == 1) {
			++transitions;
		}

		if (image[y][x + 1] == 0 && image[y + 1][x + 1] == 1) {
			++transitions;
		}

		if (image[y + 1][x + 1] == 0 && image[y + 1][x] == 1) {
			++transitions;
		}

		if (image[y + 1][x] == 0 && image[y + 1][x - 1] == 1) {
			++transitions;
		}

		if (image[y + 1][x - 1] == 0 && image[y][x - 1] == 1) {
			++transitions;
		}

		if (image[y][x - 1] == 0 && image[y - 1][x - 1] == 1) {
			++transitions;
		}

		if (image[y - 1][x - 1] == 0 && image[y - 1][x] == 1) {
			++transitions;
		}
		return transitions;
	}
}
