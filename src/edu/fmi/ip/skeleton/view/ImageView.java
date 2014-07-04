package edu.fmi.ip.skeleton.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class ImageView extends JPanel {

	/**
	 * {@value}
	 */
	private static final long serialVersionUID = -1578960967803893879L;

	/**
	 * {@value}
	 */
	private static final int MARGIN_SIDE = 10;

	private Image image;

	private final ImageObserver metricsObserver;

	private class MetricsImageObserver implements ImageObserver {
		@Override
		public boolean imageUpdate(final Image img, final int infoflags,
				final int x, final int y, final int width, final int height) {
			return width > 0 && height > 0;
		}
	}

	public ImageView(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		metricsObserver = new MetricsImageObserver();

		final Dimension dimension = new Dimension(
				PaneDimension.WIDTH_CONTAINER_IMAGE,
				PaneDimension.HEIGHT_CONTAINER_IMAGE);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		setSize(dimension);
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
			final Dimension draw = getDrawDimensions();
			graphics.drawImage(image,
					(PaneDimension.WIDTH_CONTAINER_IMAGE - draw.width) / 2,
					(PaneDimension.HEIGHT_CONTAINER_IMAGE - draw.height) / 2,
					draw.width, draw.height, Color.GRAY, null);
		}
	}

	private Dimension getDrawDimensions() {
		final int width = image.getWidth(metricsObserver);
		final int height = image.getHeight(metricsObserver);
		final Dimension result = new Dimension();

		if (width == 0 || height == 0) {
			return result;
		}

		final double aspectRatio = (double) width / height;
		int drawWidth = Math.min(width, PaneDimension.WIDTH_CONTAINER_IMAGE - 2
				* MARGIN_SIDE);
		int drawHeight = Math.min(height, (int) (drawWidth / aspectRatio));
		while (drawHeight > PaneDimension.HEIGHT_CONTAINER_IMAGE) {
			--drawWidth;
			drawHeight = Math.min(height, (int) (drawWidth / aspectRatio));
		}

		result.width = drawWidth;
		result.height = drawHeight;
		return result;
	}

	public void setImage(final Image image) {
		this.image = image;
		repaint();
	}

}
