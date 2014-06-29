package edu.fmi.ir.skeleton;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageBinarizer {

	/**
	 * {@value}
	 */
	private static final double COEF_LUMINANCE_BLUE = 0.07;

	/**
	 * {@value}
	 */
	private static final double COEF_LUMINANCE_GREEN = 0.71;

	/**
	 * {@value}
	 */
	private static final double COEF_LUMINANCE_RED = 0.21;

	/**
	 * {@value}
	 */
	private static final int INTENSITY_MAX = 256;

	public BufferedImage binarize(final BufferedImage image) {
		final BufferedImage grayImage = grayImage(image);
		return binarizeInternal(grayImage);
	}

	private int[] imageHistogram(BufferedImage image) {
		int[] histogram = new int[INTENSITY_MAX];

		for (int i = 0; i < histogram.length; i++)
			histogram[i] = 0;

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int red = new Color(image.getRGB(i, j)).getRed();
				histogram[red]++;
			}
		}

		return histogram;

	}

	private int otsuTreshold(BufferedImage original) {

		int[] histogram = imageHistogram(original);
		int total = original.getHeight() * original.getWidth();

		float sum = 0;
		for (int i = 0; i < INTENSITY_MAX; i++)
			sum += i * histogram[i];

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		int threshold = 0;

		for (int i = 0; i < INTENSITY_MAX; i++) {
			wB += histogram[i];
			if (wB == 0)
				continue;
			wF = total - wB;

			if (wF == 0)
				break;

			sumB += (float) (i * histogram[i]);
			float mB = sumB / wB;
			float mF = (sum - sumB) / wF;

			float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = i;
			}
		}

		return threshold;

	}

	private BufferedImage binarizeInternal(BufferedImage image) {
		int red;
		int newPixel;
		int threshold = otsuTreshold(image);

		BufferedImage binarized = new BufferedImage(image.getWidth(),
				image.getHeight(), image.getType());

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				// Get pixels
				red = new Color(image.getRGB(i, j)).getRed();
				if (red > threshold) {
					newPixel = 255;
				} else {
					newPixel = 0;
				}
				newPixel = toRGB(newPixel, newPixel, newPixel);
				binarized.setRGB(i, j, newPixel);

			}
		}

		return binarized;

	}

	private BufferedImage grayImage(final BufferedImage image) {
		int red, green, blue;
		int newPixel;
		BufferedImage lum = new BufferedImage(image.getWidth(),
				image.getHeight(), image.getType());
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				final Color pixelColor = new Color(image.getRGB(i, j));

				red = pixelColor.getRed();
				green = pixelColor.getGreen();
				blue = pixelColor.getBlue();

				red = (int) (COEF_LUMINANCE_RED * red + COEF_LUMINANCE_GREEN
						* green + COEF_LUMINANCE_BLUE * blue);
				newPixel = toRGB(red, red, red);
				lum.setRGB(i, j, newPixel);
			}
		}
		return lum;
	}

	private int toRGB(int red, int green, int blue) {
		return new Color(red, green, blue).getRGB();
	}
}
