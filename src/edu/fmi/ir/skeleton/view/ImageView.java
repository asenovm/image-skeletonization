package edu.fmi.ir.skeleton.view;

import java.awt.Color;
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

	private Image image;

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
		if (image != null) {
			graphics.drawImage(image, 20, 20, 360, 560, Color.GRAY, null);
		}
	}

	@Override
	public void onImageProcessed(Image image) {
		this.image = image;
		repaint();
	}
}
