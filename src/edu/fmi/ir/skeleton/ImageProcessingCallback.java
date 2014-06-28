package edu.fmi.ir.skeleton;

import java.awt.Image;
import java.io.File;

public interface ImageProcessingCallback {
	void onImageProcessed(final File imageFile, final Image image);
}
