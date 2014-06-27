package edu.fmi.ir.skeleton.view;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import edu.fmi.ir.skeleton.ImageProcessingCallback;

public class ImageView extends JPanel implements ImageProcessingCallback {

	/**
	 * {@value}
	 */
	private static final long serialVersionUID = -1578960967803893879L;

	public ImageView(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public ImageView() {
		this(new FlowLayout(), true);
	}

	public ImageView(boolean isDoubleBuffered) {
		this(new FlowLayout(), isDoubleBuffered);

	}

	public ImageView(LayoutManager layout) {
		this(layout, true);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
	}

	@Override
	public void onImageProcessed(Image image) {
		System.out.println("on image processed is called with " + image);
	}
}
