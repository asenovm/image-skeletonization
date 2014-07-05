package edu.fmi.ip.skeleton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ImageThinner {

	/**
	 * {@value}
	 */
	private static final int RADIUS_START = 1;

	private class Point {
		private int x;

		private int y;

		public Point(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
	}

	public int[][] getThinnedImage(final int[][] image) {
		final int[][] map = ImageMapRetriever.getDistanceMap(image);
		boolean hasDeleted = true;
		while (hasDeleted) {
			hasDeleted = false;

			hasDeleted = firstPass(image);
			hasDeleted = secondPass(image) || hasDeleted;
		}

		for (int i = 0; i < image.length; ++i) {
			for (int j = 0; j < image[i].length; ++j) {
				if (image[i][j] > 0) {
					image[i][j] = map[i][j];
				}
			}
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

	public int[][] getMedialAxis(final int[][] image) {
		int radius = RADIUS_START;

		final int[][] ballMap = new int[image.length][image[0].length];
		for (int i = 0; i < ballMap.length; ++i) {
			ballMap[i] = Arrays.copyOf(image[i], image[i].length);
		}

		boolean canInscribeBall = true;

		while (canInscribeBall) {
			canInscribeBall = false;
			for (int y = 1; y < image.length - 1; ++y) {
				for (int x = 1; x < image[y].length - 1; ++x) {
					if (canInscribeBall(image, x, y, radius)) {
						canInscribeBall = true;
						ballMap[y][x] = radius;
						for (int i = 1; i < ballMap.length - 1; ++i) {
							for (int j = 1; j < ballMap[i].length - 1; ++j) {
								final int radiusDelta = ballMap[y][x]
										- ballMap[i][j];
								final int distance = getChebyshovDistance(x, y,
										j, i);
								if (radiusDelta >= distance && i != y && j != x) {
									ballMap[i][j] = 0;
								}
							}
						}
					}
				}
			}
			++radius;
		}
		return ballMap;
	}

	public int[][] getReconstructedImage(final int[][] ballMap) {
		for (int i = 0; i < ballMap.length; ++i) {
			for (int j = 0; j < ballMap[i].length; ++j) {
				if (ballMap[i][j] > 1) {
					restoreBall(ballMap, j, i);
				}
			}
		}

		for (int i = 0; i < ballMap.length; ++i) {
			for (int j = 0; j < ballMap[i].length; ++j) {
				if (ballMap[i][j] > 1) {
					ballMap[i][j] = 1;
				}
			}
		}

		return ballMap;
	}

	private void restoreBall(final int[][] ballMap, final int x, final int y) {
		final int radius = ballMap[y][x];
		final int startX = x - radius + 1;
		final int endX = x + radius - 1;
		final int startY = y - radius + 1;
		final int endY = y + radius - 1;
		for (int i = startY; i <= endY; ++i) {
			for (int j = startX; j <= endX; ++j) {
				if (i >= 0 && i < ballMap.length && j >= 0
						&& j < ballMap[i].length && ballMap[i][j] == 0) {
					ballMap[i][j] = 1;
				}
			}
		}
	}

	private int getChebyshovDistance(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	private boolean canInscribeBall(int[][] image, int x, int y, int radius) {
		int startX = x - radius + 1;
		int endX = x + radius - 1;
		int startY = y - radius + 1;
		int endY = y + radius - 1;
		for (int i = startY; i <= endY; ++i) {
			for (int j = startX; j <= endX; ++j) {
				if (i < 0 || j < 0 || i >= image.length || j >= image[i].length
						|| image[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

}
