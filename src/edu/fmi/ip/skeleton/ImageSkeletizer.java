package edu.fmi.ip.skeleton;

import static edu.fmi.ip.skeleton.util.ColorUtil.INTENSITY_FOREGROUND;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import edu.fmi.ip.skeleton.callback.ImageProcessingCallback;
import edu.fmi.ip.skeleton.util.ColorUtil;

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

		private BufferedImage distanceMap;

		@Override
		public void run() {
			imageProcessingCallback.onImageSkeletized(binarized, thinned,
					distanceMap);
		}
	}

	public ImageSkeletizer() {
		binarizer = new ImageBinarizer();
		callbackRunnable = new CallbackRunnable();
	}

	/**
	 * Restores the compressed image in <tt>imageFile</tt> and callbacks the
	 * differences between it and the original file
	 * 
	 * @param imageFile
	 *            the compressed image file
	 * 
	 * @param originalImageFile
	 *            the original image file for comparison
	 */
	public void restore(File imageFile, File originalImageFile) {
		try {
			final BufferedImage image = ImageIO.read(imageFile);
			final int[][] ballMap = ImageMapRetriever.getBallMap(image);

			final ImageThinner thinner = new ImageThinner();
			final int[][] reconstructed = thinner
					.getReconstructedImage(ballMap);

			final BufferedImage restoredImage = new BufferedImage(
					image.getWidth(), image.getHeight(), TYPE_INT_ARGB);

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

	public void thin(File imageFile) {
		try {
			final BufferedImage image = ImageIO.read(imageFile);
			final BufferedImage binarized = binarizer.binarize(image);
			final int[][] colors = ColorUtil.convertToBinaryColors(binarized);

			final ImageThinner thinner = new ImageThinner();
			final int[][] thinnedColors = thinner.getThinnedImage(colors);

			final BufferedImage thinnedImage = new BufferedImage(
					thinnedColors[0].length, thinnedColors.length,
					TYPE_INT_ARGB);

			for (int i = 0; i < thinnedColors.length; ++i) {
				for (int j = 0; j < thinnedColors[0].length; ++j) {
					int rgb = thinnedColors[i][j];
					if (rgb > 0) {
						rgb = ColorUtil.distanceToColor(rgb);
					} else {
						rgb = Color.BLACK.getRGB();
					}
					thinnedImage.setRGB(j, i, rgb);
				}
			}

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					imageProcessingCallback.onImageThinned(thinnedImage,
							binarized);
				}
			});
		} catch (IOException e) {
			Logger.getLogger(TAG).severe(e.getMessage());
		}

	}

	public void skeletize(File imageFile) {
		try {
			final BufferedImage image = ImageIO.read(imageFile);
			final BufferedImage binarized = binarizer.binarize(image);
			final int[][] colors = ColorUtil.convertToBinaryColors(binarized);

			final ImageThinner thinner = new ImageThinner();
			final int[][] ballMap = thinner.getMedialAxis(colors);

			final BufferedImage thinned = new BufferedImage(ballMap[0].length,
					ballMap.length, TYPE_INT_ARGB);

			for (int i = 0; i < ballMap.length; ++i) {
				for (int j = 0; j < ballMap[0].length; ++j) {
					int rgb = ballMap[i][j];
					if (rgb > 0) {
						rgb = ColorUtil.distanceToColor(rgb);
					} else {
						rgb = Color.BLACK.getRGB();
					}
					thinned.setRGB(j, i, rgb);
				}
			}

			final int[][] distances = ImageMapRetriever.getDistanceMap(colors);
			final BufferedImage distanceMap = new BufferedImage(
					ballMap[0].length, ballMap.length, TYPE_INT_ARGB);

			for (int i = 0; i < distances.length; ++i) {
				for (int j = 0; j < distances[0].length; ++j) {
					int rgb = Color.BLACK.getRGB();
					if (distances[i][j] > 0) {
						rgb = new Color(Math.max(ColorUtil.INTENSITY_MAX - 5
								* distances[i][j], 0), Math.max(
								ColorUtil.INTENSITY_MAX - 5 * distances[i][j],
								0), Math.max(ColorUtil.INTENSITY_MAX - 5
								* distances[i][j], 0)).getRGB();
					}
					distanceMap.setRGB(j, i, rgb);
				}
			}

			callbackRunnable.distanceMap = distanceMap;
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

	private void appendChainCode(final int[][] map, final int x, final int y,
			final StringBuilder code) {

		map[y][x] = -1;

		if (x < map[y].length - 1 && map[y][x + 1] > 0) {
			code.append("0");
			appendChainCode(map, x + 1, y, code);
		}

		if (y >= 1 && x < map[y].length - 1 && map[y - 1][x + 1] > 0) {
			code.append("1");
			appendChainCode(map, x + 1, y - 1, code);
		}

		if (y >= 1 && map[y - 1][x] > 0) {
			code.append("2");
			appendChainCode(map, x, y - 1, code);
		}

		if (x >= 1 && y >= 1 && map[y - 1][x - 1] > 0) {
			code.append("3");
			appendChainCode(map, x - 1, y - 1, code);
		}

		if (x >= 1 && map[y][x - 1] > 0) {
			code.append("4");
			appendChainCode(map, x - 1, y, code);
		}

		if (y < map.length - 1 && x >= 1 && map[y + 1][x - 1] > 0) {
			code.append("5");
			appendChainCode(map, x - 1, y + 1, code);
		}

		if (y < map.length - 1 && map[y + 1][x] > 0) {
			code.append("6");
			appendChainCode(map, x, y + 1, code);
		}

		if (y < map.length - 1 && x < map[y].length - 1
				&& map[y + 1][x + 1] > 0) {
			code.append("7");
			appendChainCode(map, x + 1, y + 1, code);
		}

	}

	public String getChainCode(final BufferedImage skeleton) {
		final int[][] map = ImageMapRetriever.getBallMap(skeleton);
		final StringBuilder code = new StringBuilder();

		Point start = getChainStartPosition(map);
		while ((start = getChainStartPosition(map)) != null) {
			appendChainCode(map, start.x, start.y, code);
		}

		return code.toString();
	}

}
