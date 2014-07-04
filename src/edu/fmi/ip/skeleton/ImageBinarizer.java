package edu.fmi.ip.skeleton;

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
		final BufferedImage grayImage = convertToGrayScale(image);
		return binarizeInternal(grayImage);
	}

	private int[] getHistogram(BufferedImage image) {
		int[] histogram = new int[INTENSITY_MAX];

		for (int i = 0; i < histogram.length; i++) {
			histogram[i] = 0;
		}

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int red = new Color(image.getRGB(i, j)).getRed();
				histogram[red]++;
			}
		}

		return histogram;

	}

	private int getBinarizationTreshold(BufferedImage original) {

		int[] histogram = getHistogram(original);
		double total = original.getHeight() * original.getWidth();

		double sum = 0;
		for (int i = 0; i < INTENSITY_MAX; i++) {
			sum += i * histogram[i];
		}

		double sumBackground = 0;
		double probabilityBackground = 0;
		double probabilityForeground = 0;

		double bestVariance = 0;
		int threshold = 0;

		for (int i = 0; i < INTENSITY_MAX; i++) {
			probabilityBackground += histogram[i] / total;

			if (probabilityBackground == 0) {
				continue;
			}

			probabilityForeground = 1 - probabilityBackground;

			if (probabilityForeground == 0) {
				break;
			}

			sumBackground += i * histogram[i];

			double expectationBackground = sumBackground / probabilityBackground;
			double expectationForeground = (sum - sumBackground)
					/ probabilityForeground;

			double variance = probabilityBackground * probabilityForeground
					* (expectationBackground - expectationForeground)
					* (expectationBackground - expectationForeground);

			if (variance > bestVariance) {
				bestVariance = variance;
				threshold = i;
			}
		}

		return threshold;

	}

	private BufferedImage binarizeInternal(BufferedImage image) {
		int red;
		int newPixel;
		int threshold = getBinarizationTreshold(image);

		BufferedImage binarized = new BufferedImage(image.getWidth(),
				image.getHeight(), image.getType());

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

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

	private BufferedImage convertToGrayScale(final BufferedImage image) {
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
