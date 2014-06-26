package edu.fmi.ir.skeleton;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import edu.fmi.ir.skeleton.view.PaneView;

/*
 * FileChooserDemo.java uses these files:
 *   images/Open16.gif
 *   images/Save16.gif
 */
public class ImageSkeleton {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);

				JFrame frame = new JFrame("FileChooserDemo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				frame.add(new PaneView());
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}