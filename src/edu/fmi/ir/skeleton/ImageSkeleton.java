package edu.fmi.ir.skeleton;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.fmi.ir.skeleton.view.PaneView;

public class ImageSkeleton implements ButtonCallback {

	/**
	 * {@value}
	 */
	private static final String TITLE_APP = "Image Skeleton";

	private final FileReader reader;

	private final ImageSkeletizer skeletizer;

	public ImageSkeleton() {
		reader = new FileReader();
		skeletizer = new ImageSkeletizer();

		JFrame frame = new JFrame(TITLE_APP);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final PaneView paneView = new PaneView();
		paneView.setButtonCallback(this);
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

	@Override
	public void onFileSelected(File selected) {
		reader.onFileSelected(selected);
	}

	@Override
	public void onSkeletonRequired(File image) {
		skeletizer.skeletize(image);
	}
}