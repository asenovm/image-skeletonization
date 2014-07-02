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

	public void restore(File imageFile) {
		try {
			final BufferedImage image = ImageIO.read(imageFile);
			final int[][] ballMap = new int[image.getHeight()][image.getWidth()];
			for (int i = 0; i < image.getHeight(); ++i) {
				for (int j = 0; j < image.getWidth(); ++j) {
					final int rgb = image.getRGB(j, i);
					final Color color = new Color(rgb);
					if (!Color.BLACK.equals(color)) {
						ballMap[j][i] = 255 - color.getRed();
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
					if (reconstructed[j][i] == 1) {
						rgb = Color.WHITE.getRGB();
					} else {
						rgb = Color.BLACK.getRGB();
					}
					restoredImage.setRGB(j, i, rgb);
				}
			}
			imageProcessingCallback.onImageRestored(restoredImage);
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
