package edu.fmi.ip.skeleton;

import static edu.fmi.ip.skeleton.util.ColorUtil.INTENSITY_BACKGROUND;
import static edu.fmi.ip.skeleton.util.ColorUtil.INTENSITY_FOREGROUND;
import static edu.fmi.ip.skeleton.util.ColorUtil.INTENSITY_MAX;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import edu.fmi.ip.skeleton.callback.ImageProcessingCallback;

public class ImageSkeletizer {

	/**
	 * {@value}
	 */
	private static final String TAG = ImageSkeletizer.class.getSimpleName();

	private final ImageBinarizer binarizer;

	private ImageProcessingCallback imageProcessingCallback;

	private CallbackRunnable callbackRunnable;

	private class CallbackRunnable implements Runnable {

		private BufferedImage binarized;

		private BufferedImage thinned;

		@Override
		public void run() {
			imageProcessingCallback.onImageSkeletized(binarized, thinned);
		}
	}

	public ImageSkeletizer() {
		binarizer = new ImageBinarizer();
		callbackRunnable = new CallbackRunnable();
	}

	public void restore(File imageFile, File originalImageFile) {
		try {
			final BufferedImage image = ImageIO.read(imageFile);
			final int[][] ballMap = DistanceMapGenerator.getDistanceMap(image);

			final ImageThinner thinner = new ImageThinner();
			final int[][] reconstructed = thinner
					.getReconstructedImage(ballMap);
			final BufferedImage restoredImage = new BufferedImage(
					image.getWidth(), image.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

			for (int i = 0; i < reconstructed.length; ++i) {
				for (int j = 0; j < reconstructed[i].length; ++j) {
					int rgb = 0;
					if (reconstructed[i][j] == INTENSITY_FOREGROUND) {
						rgb = Color.WHITE.getRGB();
					} else {
						rgb = Color.BLACK.getRGB();
					}
					restoredImage.setRGB(j, i, rgb);
				}
			}

			int match = 0;
			int falsePositive = 0;
			int falseNegative = 0;
			final int blackRGB = Color.BLACK.getRGB();

			final BufferedImage original = ImageIO.read(originalImageFile);
			for (int i = 0; i < original.getHeight(); ++i) {
				for (int j = 0; j < original.getWidth(); ++j) {
					final int restoredRGB = restoredImage.getRGB(j, i);
					final int originalRGB = original.getRGB(j, i);
					if ((restoredRGB > blackRGB && originalRGB > blackRGB)
							|| (restoredRGB == blackRGB && originalRGB == blackRGB)) {
						++match;
					}

					if (restoredRGB > blackRGB && originalRGB == blackRGB) {
						++falsePositive;
					}

					if (restoredRGB == blackRGB && originalRGB > blackRGB) {
						++falseNegative;
					}
				}
			}
			imageProcessingCallback.onImageRestored(restoredImage, match,
					falsePositive, falseNegative);
		} catch (IOException e) {
			Logger.getLogger(TAG).severe(e.getMessage());
		}

	}

	public void skeletize(File imageFile) {
		try {
			final BufferedImage image = ImageIO.read(imageFile);
			final BufferedImage binarized = binarizer.binarize(image);
			final int[][] colors = convert(binarized);
			final ImageThinner thinner = new ImageThinner();
			final int[][] thinnedColors = thinner.getThinnedImage(colors);
			final BufferedImage thinned = new BufferedImage(
					thinnedColors[0].length, thinnedColors.length,
					BufferedImage.TYPE_INT_ARGB);
			for (int i = 0; i < thinnedColors.length; ++i) {
				for (int j = 0; j < thinnedColors[0].length; ++j) {
					int rgb = thinnedColors[i][j];
					if (rgb > 0) {
						rgb = new Color(INTENSITY_MAX - rgb, INTENSITY_MAX
								- rgb, INTENSITY_MAX - rgb, INTENSITY_MAX)
								.getRGB();
					} else {
						rgb = Color.BLACK.getRGB();
					}
					thinned.setRGB(j, i, rgb);
				}
			}

			callbackRunnable.binarized = binarized;
			callbackRunnable.thinned = thinned;
			SwingUtilities.invokeLater(callbackRunnable);
		} catch (IOException ex) {
			Logger.getLogger(TAG).severe(ex.getMessage());
		}
	}

	public void setCallback(final ImageProcessingCallback callback) {
		imageProcessingCallback = callback;
	}

	private Point getChainStartPosition(final int[][] image) {
		for (int i = 0; i < image.length; ++i) {
			for (int j = 0; j < image[i].length; ++j) {
				if (image[i][j] > 0) {
					return new Point(j, i);
				}
			}
		}
		return null;
	}

	public String getChainCode(final BufferedImage skeleton) {
		final int[][] map = DistanceMapGenerator.getDistanceMap(skeleton);
		final StringBuilder code = new StringBuilder();

		Point start;
		while ((start = getChainStartPosition(map)) != null) {
			int currentX = start.x;
			int currentY = start.y;

			boolean isMoving = true;
			boolean appendNewLine = false;

			while (isMoving) {
				isMoving = false;
				map[currentY][currentX] = -1;
				if (currentX < map.length - 1
						&& map[currentY][currentX + 1] > 0) {
					isMoving = true;
					appendNewLine = true;
					code.append("0");
					++currentX;
				} else if (currentY < map.length - 1
						&& currentX < map[0].length - 1
						&& map[currentY + 1][currentX + 1] > 0) {
					isMoving = true;
					appendNewLine = true;
					code.append("7");
					++currentX;
					++currentY;
				} else if (currentY < map.length - 1
						&& map[currentY + 1][currentX] > 0) {
					isMoving = true;
					appendNewLine = true;
					code.append("6");
					++currentY;
				} else if (currentY < map.length - 1 && currentX >= 1
						&& map[currentY + 1][currentX - 1] > 0) {
					isMoving = true;
					appendNewLine = true;
					code.append("5");
					++currentY;
					--currentX;
				} else if (currentX >= 1 && map[currentY][currentX - 1] > 0) {
					isMoving = true;
					appendNewLine = true;
					code.append("4");
					--currentX;
				} else if (currentX >= 1 && currentY >= 1
						&& map[currentY - 1][currentX - 1] > 0) {
					isMoving = true;
					appendNewLine = true;
					code.append("3");
					--currentX;
					--currentY;
				} else if (currentY >= 1 && map[currentY - 1][currentX] > 0) {
					isMoving = true;
					appendNewLine = true;
					code.append("2");
					--currentY;
				} else if (currentY >= 1 && currentX < map.length - 1
						&& map[currentY - 1][currentX + 1] > 0) {
					isMoving = true;
					appendNewLine = true;
					code.append("1");
					--currentY;
					++currentX;
				}
			}

			if (appendNewLine) {
				appendNewLine = false;
				code.append("\n");
			}
		}

		return code.toString();
	}

	private static int[][] convert(BufferedImage image) {

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
