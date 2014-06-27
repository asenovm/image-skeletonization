package edu.fmi.ir.skeleton;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.fmi.ir.skeleton.view.PaneView;

public class ImageSkeleton {

	/**
	 * {@value}
	 */
	private static final String TITLE_APP = "Image Skeleton";

	public ImageSkeleton() {
		final FileReader reader = new FileReader();

		JFrame frame = new JFrame(TITLE_APP);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final PaneView paneView = new PaneView();
		paneView.setFileCallback(reader);
		reader.setCallback(paneView);

		frame.add(paneView);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@SuppressWarnings("unused")
			public void run() {
				final ImageSkeleton skeleton = new ImageSkeleton();
			}
		});
	}
}