package edu.fmi.ir.skeleton;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.fmi.ir.skeleton.view.PaneView;

public class ImageSkeleton implements ButtonCallback, ImageProcessingCallback {

	/**
	 * {@value}
	 */
	private static final String TITLE_APP = "Морфологична Скелетизация";

	private final ImageSaver saver;

	private final FileReader reader;

	private final ImageSkeletizer skeletizer;

	private final PaneView layout;

	private final JFrame frame;

	private final ExecutorService executor;

	private final SkeletonRunnable skeletonRunnable;

	private final RestoreRunnable restoreRunnable;

	private class RestoreRunnable implements Runnable {

		private File originalImage;

		private File image;

		@Override
		public void run() {
			skeletizer.restore(image, originalImage);
		}

	}

	private class SkeletonRunnable implements Runnable {

		private File image;

		@Override
		public void run() {
			skeletizer.skeletize(image);
		}
	}

	public ImageSkeleton() {
		reader = new FileReader();
		skeletizer = new ImageSkeletizer();
		saver = new ImageSaver();

		frame = new JFrame(TITLE_APP);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layout = new PaneView();
		layout.setButtonCallback(this);
		reader.setCallback(layout);
		skeletizer.setCallback(this);

		frame.add(layout);
		frame.pack();
		frame.setVisible(true);

		executor = Executors.newSingleThreadExecutor();
		skeletonRunnable = new SkeletonRunnable();
		restoreRunnable = new RestoreRunnable();
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
		skeletonRunnable.image = image;
		executor.execute(skeletonRunnable);
	}

	@Override
	public void onImageRead(File imageFile, Image image) {
		layout.onImageRead(imageFile, image);
	}

	@Override
	public void onImageSkeletized(final BufferedImage binarized,
			final BufferedImage skeletized) {
		layout.onImageSkeletized(binarized, skeletized);
		frame.pack();
	}

	@Override
	public void onSaveRequired(final BufferedImage skeleton,
			final BufferedImage binarized) {
		saver.save(skeleton, binarized);
	}

	@Override
	public void onRestoreRequired(File image, final File originalImage) {
		restoreRunnable.image = image;
		restoreRunnable.originalImage = originalImage;
		executor.execute(restoreRunnable);
	}

	@Override
	public void onImageRestored(BufferedImage restored, int match,
			int falsePositive, int falseNegative) {
		layout.onImageRestored(restored, match, falsePositive, falseNegative);
		frame.pack();
	}

	@Override
	public void onOriginalFileSelected(File originalImage) {
		reader.onOriginalFileSelected(originalImage);
		frame.pack();
	}

	@Override
	public void onOriginalImageRead(File originalImage, Image image) {
		layout.onOriginalImageRead(originalImage, image);
	}
}