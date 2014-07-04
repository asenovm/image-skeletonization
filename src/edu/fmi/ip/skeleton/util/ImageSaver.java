package edu.fmi.ip.skeleton.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageSaver {

	/**
	 * {@value}
	 */
	private static final String TAG = ImageSaver.class.getSimpleName();

	/**
	 * {@value}
	 */
	private static final String SUFFIX_IMAGE_RESTORED = "_restored.png";

	/**
	 * {@value}
	 */
	private static final String SUFFIX_IMAGE_BINARIZED = "_binarized.png";

	/**
	 * {@value}
	 */
	private static final String TYPE_IMAGE_SAVED = "png";

	/**
	 * {@value}
	 */
	private static final String SUFFIX_IMAGE_SKELETON = "_skeleton.png";

	public void save(final String filenamePrefix, final BufferedImage skeleton,
			final BufferedImage binarized, final BufferedImage restored) {
		File skeletonFile = new File(filenamePrefix + SUFFIX_IMAGE_SKELETON);
		File binarizedFile = new File(filenamePrefix + SUFFIX_IMAGE_BINARIZED);
		File restoredFile = new File(filenamePrefix + SUFFIX_IMAGE_RESTORED);
		try {
			if (skeleton != null) {
				ImageIO.write(skeleton, TYPE_IMAGE_SAVED, skeletonFile);
			}
			if (binarized != null) {
				ImageIO.write(binarized, TYPE_IMAGE_SAVED, binarizedFile);
			}
			if (restored != null) {
				ImageIO.write(restored, TYPE_IMAGE_SAVED, restoredFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger(TAG).severe(e.getMessage());
		}
	}

	public void saveVector(final String savePath, final String vector) {
		try {
			final PrintWriter writer = new PrintWriter(new File(savePath));
			writer.println(vector);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Logger.getLogger(TAG).severe(e.getMessage());
		}
	}
}
