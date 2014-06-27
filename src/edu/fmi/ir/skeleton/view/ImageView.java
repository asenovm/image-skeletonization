package edu.fmi.ir.skeleton.view;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class ImageView extends JPanel {

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
}
