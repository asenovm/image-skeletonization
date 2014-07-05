package edu.fmi.ip.skeleton.callback;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ButtonCallback {
	/**
	 * A callback that is fired when the user has finished navigating up to a
	 * file using the file picker
	 * 
	 * @param selected
	 *            the file that has been selected by the user from the file
	 *            picker tool
	 */
	void onFileSelected(final File selected);

	/**
	 * A callback fired when skeletization of the selected <tt>image</tt> is
	 * required
	 * 
	 * @param image
	 *            the image that is to be skeletized
	 */
	void onSkeletonRequired(final File image);

	/**
	 * A callback fired when the generated items need to be saved on the local
	 * file system
	 * 
	 * @param filenamePrefix
	 *            the prefix with which the names of the saved files will start
	 * @param skeleton
	 *            the skeletized input image
	 * @param binarized
	 *            the binarized input image
	 * @param restored
	 *            the restored (from the skeleton) input image
	 */
	void onSaveRequired(final String filenamePrefix,
			final BufferedImage skeleton, final BufferedImage binarized,
			final BufferedImage restored);

	/**
	 * A callback fired when the original image needs to be restored
	 * 
	 * @param image
	 *            the image file from which the image is to be restored
	 *            (skeleton)
	 * @param original
	 *            the original image file, needed for comparison
	 */
	void onRestoreRequired(final File image, final File original);

	/**
	 * A callback fired when the image, representing the original file with
	 * which the restored image will be compared has been selected
	 * 
	 * @param originalImage
	 *            the file, representing the original image
	 */
	void onOriginalFileSelected(final File originalImage);

	/**
	 * A callback fired when the skeletized image is to be vectorized
	 * 
	 * @param skeletized
	 *            the image skeleton that is to be vectorized
	 * @param savePath
	 *            the path on the local file system where the vectorized image
	 *            needs to be saved
	 */
	void onVectorizationRequired(final BufferedImage skeletized,
			final String savePath);

	void onThinRequired(final File imageFile);
}
