package edu.fmi.ir.skeleton;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

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
			final int[][] ballMap = new int[image.getHeight()][image.getWidth()];
			for (int i = 0; i < image.getHeight(); ++i) {
				for (int j = 0; j < image.getWidth(); ++j) {
					final int rgb = image.getRGB(j, i);
					final Color color = new Color(rgb);
					if (!Color.BLACK.equals(color)) {
						ballMap[i][j] = 255 - color.getRed();
					}
				}
			}
			final ImageThinner thinner = new ImageThinner();
			final int[][] reconstructed = thinner
					.getReconstructedImage(ballMap);
			final BufferedImage restoredImage = new BufferedImage(
					image.getWidth(), image.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

			for (int i = 0; i < reconstructed.length; ++i) {
				for (int j = 0; j < reconstructed[i].length; ++j) {
					int rgb = 0;
					if (reconstructed[i][j] == 1) {
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
						rgb = new Color(255 - rgb, 255 - rgb, 255 - rgb, 255)
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

	public String getChainCode(final BufferedImage skeleton) {
		final int[][] colors = new int[skeleton.getHeight()][skeleton
				.getWidth()];
		for (int i = 0; i < colors.length; ++i) {
			for (int j = 0; j < colors[i].length; ++j) {
				if (Color.BLACK.getRGB() == skeleton.getRGB(j, i)) {
					colors[i][j] = 0;
				} else {
					colors[i][j] = 1;
				}
			}
		}
		int startX = 0;
		int startY = 0;

		for (int i = 0; i < colors.length && startX == 0 && startY == 0; ++i) {
			for (int j = 0; j < colors[i].length && startX == 0 && startY == 0; ++j) {
				if (colors[i][j] == 0) {
					startX = j;
					startY = i;
					break;
				}
			}
		}

		int currentX = startX;
		int currentY = startY;

		boolean isMoving = true;
		final StringBuilder code = new StringBuilder();

		while (colors[currentY][currentX] == 0 && isMoving) {
			isMoving = false;
			colors[currentY][currentX] = -1;
			if (currentX < colors.length - 1
					&& colors[currentY][currentX + 1] == 0) {
				isMoving = true;
				code.append("0");
				++currentX;
			} else if (currentY < colors.length - 1
					&& currentX < colors[0].length - 1
					&& colors[currentY + 1][currentX + 1] == 0) {
				isMoving = true;
				code.append("7");
				++currentX;
				++currentY;
			} else if (currentY < colors.length - 1
					&& colors[currentY + 1][currentX] == 0) {
				isMoving = true;
				code.append("6");
				++currentY;
			} else if (currentY < colors.length - 1 && currentX >= 1
					&& colors[currentY + 1][currentX - 1] == 0) {
				isMoving = true;
				code.append("5");
				++currentY;
				--currentX;
			} else if (currentX >= 1 && currentY >= 1
					&& colors[currentY - 1][currentX - 1] == 0) {
				isMoving = true;
				code.append("3");
				--currentX;
				--currentY;
			} else if (currentX >= 1 && colors[currentY][currentX - 1] == 0) {
				isMoving = true;
				code.append("4");
				--currentX;
			} else if (currentX >= 1 && currentY >= 1
					&& colors[currentY - 1][currentX - 1] == 0) {
				isMoving = true;
				code.append("3");
				--currentX;
				--currentY;
			} else if (currentY >= 1 && colors[currentY - 1][currentX] == 0) {
				isMoving = true;
				code.append("2");
				--currentY;
			} else if (currentY >= 1 && currentX < colors.length - 1
					&& colors[currentY - 1][currentX + 1] == 0) {
				isMoving = true;
				code.append("1");
				--currentY;
				++currentX;
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
					result[i][j] = 1;
				} else {
					result[i][j] = 0;
				}
			}
		}
		return result;
	}

}
