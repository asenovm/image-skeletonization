package edu.fmi.ir.skeleton;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageProcessingCallback {
	void onImageRead(final File imageFile, final Image image);

	void onImageBinarized(final BufferedImage binarized);
}
