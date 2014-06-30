package edu.fmi.ir.skeleton.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import edu.fmi.ir.skeleton.ButtonCallback;
import edu.fmi.ir.skeleton.ImageProcessingCallback;

public class PaneView extends JPanel implements ActionListener,
		ImageProcessingCallback {

	/**
	 * {@value}
	 */
	private static final long serialVersionUID = -9067644135288203859L;

	/**
	 * {@value}
	 */
	private static final String SKELETONIZE_IMAGE = "Скелетизирай";

	/**
	 * {@value}
	 */
	private static final String BROWSE_IMAGE = "Избери";

	private final JButton openButton;

	private final JButton skeletonizeButton;

	private final JFileChooser fileChooser;

	private final ImageView imageView;

	private final JPanel imagePanel;

	private ButtonCallback buttonCallback;

	private File imageFile;

	private Image image;

	public static class SimpleFileCallback implements ButtonCallback {
		@Override
		public void onFileSelected(File selected) {
			// blank
		}

		@Override
		public void onSkeletonRequired(File image) {
			// blank
		}
	}

	public PaneView() {
		super(new BorderLayout());

		fileChooser = new JFileChooser();

		openButton = createButton(BROWSE_IMAGE);
		skeletonizeButton = createButton(SKELETONIZE_IMAGE);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(openButton);
		buttonPanel.add(skeletonizeButton);

		add(buttonPanel, BorderLayout.PAGE_END);

		final Dimension layoutDimension = new Dimension(PaneDimension.WIDTH,
				PaneDimension.HEIGHT);
		setPreferredSize(layoutDimension);
		setMinimumSize(layoutDimension);
		setMaximumSize(layoutDimension);

		buttonCallback = new SimpleFileCallback();

		imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		imageView = new ImageView();
		imagePanel.add(imageView);

		add(imagePanel);
	}

	public JButton createButton(final String title) {
		final JButton result = new JButton(title);
		result.addActionListener(this);
		final Dimension buttonDimension = new Dimension(
				PaneDimension.WIDTH_BUTTON, PaneDimension.HEIGHT_BUTTON);
		result.setPreferredSize(buttonDimension);
		result.setMinimumSize(buttonDimension);
		result.setMaximumSize(buttonDimension);
		return result;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openButton) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				buttonCallback.onFileSelected(fileChooser.getSelectedFile());
			}
		} else if (e.getSource() == skeletonizeButton) {
			buttonCallback.onSkeletonRequired(imageFile);
		}
	}

	public void setButtonCallback(final ButtonCallback callback) {
		this.buttonCallback = callback;
	}

	@Override
	public void onImageRead(final File imageFile, final Image image) {
		this.imageFile = imageFile;
		this.image = image;
		imageView.setImage(image);
	}

	@Override
	public void onImageSkeletized(final BufferedImage binarized,
			final BufferedImage skeletized) {
		final Dimension dimension = new Dimension(
				PaneDimension.WIDTH_BINARIZED, PaneDimension.HEIGHT);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		setSize(dimension);

		imagePanel.removeAll();
		imagePanel.setPreferredSize(dimension);
		imagePanel.setMinimumSize(dimension);
		imagePanel.setMaximumSize(dimension);
		imagePanel.setSize(dimension);

		imagePanel.removeAll();

		final ImageView binarizedView = new ImageView();
		imagePanel.add(imageView);
		imagePanel.add(binarizedView);

		final ImageView skeletizedView = new ImageView();
		imagePanel.add(skeletizedView);

		binarizedView.setImage(binarized);
		skeletizedView.setImage(skeletized);
		imageView.setImage(image);
	}
}
