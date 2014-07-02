package edu.fmi.ir.skeleton;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageProcessingCallback {

	/**
	 * A callback fired when the image, located at the given <tt>imageFile</tt>
	 * has been read
	 * 
	 * @param imageFile
	 *            the file where the image being read is located
	 * @param image
	 *            the image that has been read from the <tt>imageFile</tt>
	 */
	void onImageRead(final File imageFile, final Image image);

	/**
	 * A callback fired when the selected image has been skeletized
	 * 
	 * @param binarized
	 *            the binary image that was produced in the processes of
	 *            skeletonizing
	 * @param skeletized
	 *            the resultant image skeleton
	 */
	void onImageSkeletized(final BufferedImage binarized,
			final BufferedImage skeletized);

	/**
	 * A callback fired when the skeletonized image has been restored to its
	 * original dimensions
	 * 
	 * @param restored
	 *            the restored from the skeleton image
	 */
	void onImageRestored(final BufferedImage restored);
}
