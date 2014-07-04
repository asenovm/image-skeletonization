package edu.fmi.ip.skeleton;

import static edu.fmi.ip.skeleton.util.ColorUtil.INTENSITY_MAX;
import static edu.fmi.ip.skeleton.util.ColorUtil.LUMINANCE_BLUE;
import static edu.fmi.ip.skeleton.util.ColorUtil.LUMINANCE_GREEN;
import static edu.fmi.ip.skeleton.util.ColorUtil.LUMINANCE_RED;

import java.awt.Color;
import java.awt.image.BufferedImage;

import edu.fmi.ip.skeleton.util.ColorUtil;

public class ImageBinarizer {

	/**
	 * Binarizes the given image, using Otsu's method for finding binarization
	 * threshold
	 * 
	 * @param image
	 *            the image that is to be binarized
	 * @return the binarized image
	 */
	public BufferedImage binarize(final BufferedImage image) {
		final BufferedImage grayImage = convertToGrayScale(image);
		return binarizeInternal(grayImage);
	}

	private int[] getHistogram(BufferedImage image) {
		int[] histogram = new int[INTENSITY_MAX + 1];

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
		for (int i = 0; i < histogram.length; i++) {
			sum += i * histogram[i];
		}

		double sumBackground = 0;
		double probabilityBackground = 0;
		double probabilityForeground = 0;

		double bestVariance = 0;
		int threshold = 0;

		for (int i = 0; i < histogram.length; i++) {
			probabilityBackground += histogram[i] / total;

			if (probabilityBackground == 0) {
				continue;
			}

			probabilityForeground = 1 - probabilityBackground;

			if (probabilityForeground == 0) {
				break;
			}

			sumBackground += i * histogram[i];

			double expectationBackground = sumBackground
					/ probabilityBackground;
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
					newPixel = INTENSITY_MAX;
				} else {
					newPixel = 0;
				}
				newPixel = ColorUtil.toRGB(newPixel, newPixel, newPixel);
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

				red = (int) (LUMINANCE_RED * red + LUMINANCE_GREEN * green + LUMINANCE_BLUE
						* blue);
				newPixel = ColorUtil.toRGB(red, red, red);
				lum.setRGB(i, j, newPixel);
			}
		}
		return lum;
	}

}
