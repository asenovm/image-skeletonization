package edu.fmi.ir.skeleton;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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

		final byte[] pixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();

		int[][] result = new int[height][width];
		final int pixelLength = 3;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
			if (((pixels[pixel] & 0xff) == 255)
					&& ((pixels[pixel + 1] & 0xff) == 255)
					&& ((pixels[pixel + 2] & 0xff) == 255)) {
				result[row][col] = 1;
			} else {
				result[row][col] = 0;
			}

			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}

		return result;
	}

}
