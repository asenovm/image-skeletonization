package edu.fmi.ir.skeleton;

import java.io.File;

public interface FileCallback {
	/**
	 * A callback that is fired when the user has finished navigating up to a
	 * file using the file picker
	 * 
	 * @param selected
	 *            the file that has been selected by the user from the file
	 *            picker tool
	 */
	void onFileSelected(final File selected);
}
