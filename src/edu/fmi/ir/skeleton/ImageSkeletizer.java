package edu.fmi.ir.skeleton;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageSkeletizer {

	/**
	 * {@value}
	 */
	private static final String TAG = ImageSkeletizer.class.getSimpleName();

	private final ImageBinarizer binarizer;

	private ImageProcessingCallback imageProcessingCallback;

	public ImageSkeletizer() {
		binarizer = new ImageBinarizer();
	}

	public void skeletize(File imageFile) {
		try {
			final BufferedImage image = ImageIO.read(imageFile);
			final BufferedImage binarized = binarizer.binarize(image);
			final int[][] colors = convert(binarized);
			final ImageThinner service = new ImageThinner();
			final int[][] thinnedColors = service.getThinnedImage(colors);
			final BufferedImage thinned = new BufferedImage(
					thinnedColors[0].length, thinnedColors.length,
					BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < thinnedColors.length; ++i) {
				for (int j = 0; j < thinnedColors[0].length; ++j) {
					int rgb = thinnedColors[i][j];
					if (rgb == 1) {
						rgb = new Color(255, 255, 255).getRGB();
					}
					thinned.setRGB(j, i, rgb);
				}
			}
			imageProcessingCallback.onImageSkeletized(binarized, thinned);
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
