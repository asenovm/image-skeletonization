package edu.fmi.ip.skeleton.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import edu.fmi.ip.skeleton.callback.ImageProcessingCallback;

public class FileReader {

	/**
	 * {@value}
	 */
	private static final String TAG = FileReader.class.getSimpleName();

	private ImageProcessingCallback callback;

	public static class SimpleImageProcessingCallback implements
			ImageProcessingCallback {
		@Override
		public void onImageRead(final File imageFile, final Image image) {
			// blank
		}

		@Override
		public void onImageSkeletized(final BufferedImage binarized,
				final BufferedImage skeletize, final BufferedImage distanceMap) {
			// blank
		}

		@Override
		public void onImageRestored(BufferedImage restored, int match,
				int falsePositive, int falseNegative) {
			// blank
		}

		@Override
		public void onOriginalImageRead(File originalImage, Image image) {
			// blank
		}

		@Override
		public void onImageThinned(BufferedImage thinned,
				BufferedImage binarized) {
			// blank
		}
	}

	public FileReader() {
		this.callback = new SimpleImageProcessingCallback();
	}

	public void onFileSelected(File selected) {
		try {
			callback.onImageRead(selected, ImageIO.read(selected));
		} catch (IOException e) {
			Logger.getLogger(TAG).severe(e.getMessage());
		}
	}

	public void onOriginalFileSelected(final File originalImage) {
		try {
			callback.onOriginalImageRead(originalImage,
					ImageIO.read(originalImage));
		} catch (IOException e) {
			Logger.getLogger(TAG).severe(e.getMessage());
		}
	}

	public void setCallback(final ImageProcessingCallback callback) {
		this.callback = callback;
	}

}
