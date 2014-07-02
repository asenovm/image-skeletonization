package edu.fmi.ir.skeleton;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageSaver {

	/**
	 * {@value}
	 */
	private static final String TAG = ImageSaver.class.getSimpleName();

	public void save(final BufferedImage skeleton, final BufferedImage binarized) {
		final long timestamp = new Date().getTime();
		File skeletonFile = new File("skeleton" + timestamp + ".png");
		File binarizedFile = new File("binarized" + timestamp + ".png");
		try {
			ImageIO.write(skeleton, "png", skeletonFile);
			ImageIO.write(binarized, "png", binarizedFile);
		} catch (IOException e) {
			Logger.getLogger(TAG).severe(e.getMessage());
		}
	}
}
