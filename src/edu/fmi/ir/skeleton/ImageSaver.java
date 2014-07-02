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

	public void save(final BufferedImage skeleton) {
		File outputfile = new File("skeleton" + new Date().getTime() + ".png");
		try {
			ImageIO.write(skeleton, "png", outputfile);
		} catch (IOException e) {
			Logger.getLogger(TAG).severe(e.getMessage());
		}
	}
}
