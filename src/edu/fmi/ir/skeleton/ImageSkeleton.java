package edu.fmi.ir.skeleton;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.fmi.ir.skeleton.view.PaneView;

public class ImageSkeleton implements ButtonCallback, ImageProcessingCallback {

	/**
	 * {@value}
	 */
	private static final String TITLE_APP = "Image Skeleton";

	private final FileReader reader;

	private final ImageSkeletizer skeletizer;

	private final PaneView layout;

	private final JFrame frame;

	public ImageSkeleton() {
		reader = new FileReader();
		skeletizer = new ImageSkeletizer();

		frame = new JFrame(TITLE_APP);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layout = new PaneView();
		layout.setButtonCallback(this);
		reader.setCallback(layout);
		skeletizer.setCallback(this);

		frame.add(layout);
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

	@Override
	public void onImageRead(File imageFile, Image image) {
		layout.onImageRead(imageFile, image);
	}

	@Override
	public void onImageBinarized(BufferedImage binarized) {
		layout.onImageBinarized(binarized);
		frame.pack();
	}
}