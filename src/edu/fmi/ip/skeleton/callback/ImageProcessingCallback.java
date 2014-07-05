package edu.fmi.ip.skeleton.callback;

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
			final BufferedImage skeletized, final BufferedImage distanceMap);

	/**
	 * A callback fired when the skeletonized image has been restored to its
	 * original dimensions
	 * 
	 * @param restored
	 *            the restored from the skeleton image
	 */
	void onImageRestored(final BufferedImage restored, final int match,
			final int falsePositive, final int falseNegative);

	/**
	 * A callback fired when the original image that is to be compared with the
	 * version, retrieved from the skeleton has been read
	 * 
	 * @param originalImage
	 *            the file representation of the image
	 * @param image
	 *            the {@link Image} representation of the image
	 */
	void onOriginalImageRead(final File originalImage, final Image image);

	void onImageThinned(final BufferedImage thinned,
			final BufferedImage binarized);

}
