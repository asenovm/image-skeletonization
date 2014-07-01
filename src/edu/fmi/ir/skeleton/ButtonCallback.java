package edu.fmi.ir.skeleton;

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
	
	void onSaveRequired();
}
