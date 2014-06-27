package edu.fmi.ir.skeleton;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.fmi.ir.skeleton.view.PaneView;

public class ImageSkeleton {

	/**
	 * {@value}
	 */
	private static final String TITLE_APP = "Image Skeleton";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame(TITLE_APP);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				frame.add(new PaneView());
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}