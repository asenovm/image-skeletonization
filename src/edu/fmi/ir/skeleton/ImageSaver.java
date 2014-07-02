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

	public void save(final String filenamePrefix, final BufferedImage skeleton,
			final BufferedImage binarized) {
		System.out.println("prefix is  " + filenamePrefix);
		File skeletonFile = new File(filenamePrefix + "_skeleton.png");
		File binarizedFile = new File(filenamePrefix + "_binarized.png");
		try {
			ImageIO.write(skeleton, "png", skeletonFile);
			ImageIO.write(binarized, "png", binarizedFile);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger(TAG).severe(e.getMessage());
		}
	}
}
