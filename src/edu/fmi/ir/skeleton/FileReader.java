package edu.fmi.ir.skeleton;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class FileReader {

	/**
	 * {@value}
	 */
	private static final String TAG = FileReader.class.getSimpleName();

	private ImageProcessingCallback callback;

	public static class SimpleImageProcessingCallback implements
			ImageProcessingCallback {
		@Override
		public void onImageProcessed(final File imageFile, final Image image) {
			// blank
		}
	}

	public FileReader() {
		this.callback = new SimpleImageProcessingCallback();
	}

	public void onFileSelected(File selected) {
		try {
			callback.onImageProcessed(selected, ImageIO.read(selected));
		} catch (IOException e) {
			Logger.getLogger(TAG).severe(e.getMessage());
		}
	}

	public void setCallback(final ImageProcessingCallback callback) {
		this.callback = callback;
	}

}
