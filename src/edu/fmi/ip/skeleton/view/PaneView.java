package edu.fmi.ip.skeleton.view;

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

import edu.fmi.ip.skeleton.callback.ButtonCallback;
import edu.fmi.ip.skeleton.callback.ImageProcessingCallback;

public class PaneView extends JPanel implements ActionListener,
		ImageProcessingCallback {

	/**
	 * {@value}
	 */
	private static final long serialVersionUID = -9067644135288203859L;

	/**
	 * {@value}
	 */
	private static final String SAVE_IMAGE = "Запази";

	/**
	 * {@value}
	 */
	private static final String SKELETONIZE_IMAGE = "Скелетизирай";

	/**
	 * {@value}
	 */
	private static final String BROWSE_IMAGE = "Избери";

	/**
	 * {@value}
	 */
	private static final String RESTORE_IMAGE = "Възстанови";

	/**
	 * {@value}
	 */
	private static final String ORIGINAL_IMAGE = "Оригинал";

	/**
	 * {@value}
	 */
	private static final String VECTORIZE_IMAGE = "Векторизирай";

	/**
	 * {@value}
	 */
	private static final String THIN_IMAGE = "Изтъни";

	private final JButton openButton;

	private final JButton skeletonizeButton;

	private final JButton saveButton;

	private final JButton restoreButton;

	private final JButton originalButton;

	private final JButton vectorizeButton;

	private final JButton thinButton;

	private final JFileChooser fileChooser;

	private final ImageView imageView;

	private final JPanel imagePanel;

	private final StatisticsPanel statisticsPanel;

	private ButtonCallback buttonCallback;

	private BufferedImage skeletizedImage;

	private BufferedImage binarizedImage;

	private BufferedImage restoredImage;

	private Image originalImage;

	private File imageFile;

	private File originalImageFile;

	private Image image;

	private final JPanel buttonPanel;

	public static class SimpleFileCallback implements ButtonCallback {
		@Override
		public void onFileSelected(final File selected) {
			// blank
		}

		@Override
		public void onSkeletonRequired(final File image) {
			// blank
		}

		@Override
		public void onSaveRequired(final String filenamePrefix,
				final BufferedImage skeleton, final BufferedImage binarized,
				final BufferedImage restored) {
			// blank
		}

		@Override
		public void onRestoreRequired(final File image, final File originalImage) {
			// blank
		}

		@Override
		public void onOriginalFileSelected(File originalImage) {
			// blank
		}

		@Override
		public void onVectorizationRequired(final BufferedImage skeletized,
				final String savePath) {
			// blank
		}

		@Override
		public void onThinRequired(File imageFile) {
			// blank
		}
	}

	public PaneView() {
		super(new BorderLayout());

		fileChooser = new JFileChooser();

		statisticsPanel = new StatisticsPanel();

		openButton = createButton(BROWSE_IMAGE);
		skeletonizeButton = createButton(SKELETONIZE_IMAGE);
		saveButton = createButton(SAVE_IMAGE);
		restoreButton = createButton(RESTORE_IMAGE);
		originalButton = createButton(ORIGINAL_IMAGE);
		vectorizeButton = createButton(VECTORIZE_IMAGE);
		thinButton = createButton(THIN_IMAGE);

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(openButton);
		buttonPanel.add(originalButton);
		buttonPanel.add(skeletonizeButton);
		buttonPanel.add(restoreButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(vectorizeButton);
		buttonPanel.add(thinButton);

		hideAllButtons();
		openButton.setVisible(true);

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
		} else if (e.getSource() == saveButton) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				buttonCallback.onSaveRequired(fileChooser.getSelectedFile()
						.getAbsolutePath(), skeletizedImage, binarizedImage,
						restoredImage);
			}
		} else if (e.getSource() == restoreButton) {
			buttonCallback.onRestoreRequired(imageFile, originalImageFile);
		} else if (e.getSource() == originalButton) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				buttonCallback.onOriginalFileSelected(fileChooser
						.getSelectedFile());
			}
		} else if (e.getSource() == vectorizeButton) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				buttonCallback.onVectorizationRequired(skeletizedImage,
						fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if (e.getSource() == thinButton) {
			buttonCallback.onThinRequired(imageFile);
		}
	}

	public void setButtonCallback(final ButtonCallback callback) {
		this.buttonCallback = callback;
	}

	@Override
	public void onImageRead(final File imageFile, final Image image) {
		final Dimension layoutDimension = new Dimension(PaneDimension.WIDTH,
				PaneDimension.HEIGHT);
		imagePanel.setMinimumSize(layoutDimension);
		imagePanel.setMaximumSize(layoutDimension);
		imagePanel.setPreferredSize(layoutDimension);
		imagePanel.setSize(layoutDimension);

		imagePanel.removeAll();
		imagePanel.add(imageView);

		this.imageFile = imageFile;
		this.image = image;
		imageView.setImage(image);

		hideAllButtons();
		openButton.setVisible(true);
		skeletonizeButton.setVisible(true);
		originalButton.setVisible(true);
		thinButton.setVisible(true);
	}

	@Override
	public void onImageSkeletized(final BufferedImage binarized,
			final BufferedImage skeletized, final BufferedImage distanceMap) {
		imagePanel.removeAll();

		final ImageView binarizedView = new ImageView();
		imagePanel.add(imageView);
		imagePanel.add(binarizedView);

		final ImageView skeletizedView = new ImageView();
		imagePanel.add(skeletizedView);

		final ImageView distanceView = new ImageView();
		imagePanel.add(distanceView);

		binarizedView.setImage(binarized);
		skeletizedView.setImage(skeletized);
		distanceView.setImage(distanceMap);
		imageView.setImage(image);

		skeletizedImage = skeletized;
		binarizedImage = binarized;

		hideAllButtons();
		openButton.setVisible(true);
		saveButton.setVisible(true);
		vectorizeButton.setVisible(true);
	}

	@Override
	public void onImageRestored(BufferedImage restored, int matching,
			int falsePositives, int falseNegatives) {
		imagePanel.removeAll();
		final ImageView binarizedView = new ImageView();
		imagePanel.add(imageView);
		imagePanel.add(binarizedView);

		final ImageView skeletizedView = new ImageView();
		imagePanel.add(skeletizedView);

		skeletizedView.setImage(restored);
		binarizedView.setImage(originalImage);
		imageView.setImage(image);

		statisticsPanel.init(matching, falsePositives, falseNegatives);
		imagePanel.add(statisticsPanel);

		hideAllButtons();
		openButton.setVisible(true);
		saveButton.setVisible(true);

		restoredImage = restored;
	}

	@Override
	public void onOriginalImageRead(File originalImageFile, Image originalImage) {
		imagePanel.removeAll();

		final ImageView binarizedView = new ImageView();
		imagePanel.add(imageView);
		imagePanel.add(binarizedView);

		final ImageView skeletizedView = new ImageView();
		imagePanel.add(skeletizedView);

		binarizedView.setImage(originalImage);
		imageView.setImage(image);

		this.originalImage = originalImage;
		this.originalImageFile = originalImageFile;

		hideAllButtons();
		openButton.setVisible(true);
		originalButton.setVisible(true);
		restoreButton.setVisible(true);
	}

	private void hideAllButtons() {
		skeletonizeButton.setVisible(false);
		restoreButton.setVisible(false);
		originalButton.setVisible(false);
		vectorizeButton.setVisible(false);
		saveButton.setVisible(false);
		thinButton.setVisible(false);
	}

	@Override
	public void onImageThinned(BufferedImage thinned, BufferedImage binarized) {
		imagePanel.removeAll();

		final ImageView binarizedView = new ImageView();
		imagePanel.add(imageView);
		imagePanel.add(binarizedView);

		final ImageView skeletizedView = new ImageView();
		imagePanel.add(skeletizedView);

		final ImageView distanceView = new ImageView();
		imagePanel.add(distanceView);

		binarizedView.setImage(binarized);
		skeletizedView.setImage(thinned);
		imageView.setImage(image);

		skeletizedImage = thinned;
		binarizedImage = binarized;

		hideAllButtons();
		openButton.setVisible(true);
		saveButton.setVisible(true);
		vectorizeButton.setVisible(true);

	}
}
