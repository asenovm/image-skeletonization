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

	private static final String SAVE_IMAGE = "Запази";

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

	private final JButton openButton;

	private final JButton skeletonizeButton;

	private final JButton saveButton;

	private final JButton restoreButton;

	private final JButton originalButton;

	private final JButton vectorizeButton;

	private final JFileChooser fileChooser;

	private final ImageView imageView;

	private final JPanel imagePanel;

	private final StatisticsPanel statisticsPanel;

	private ButtonCallback buttonCallback;

	private BufferedImage skeletizedImage;

	private BufferedImage binarizedImage;

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
				final BufferedImage skeleton, final BufferedImage binarized) {
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

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(openButton);
		buttonPanel.add(originalButton);
		buttonPanel.add(skeletonizeButton);
		buttonPanel.add(restoreButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(vectorizeButton);

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
						.getAbsolutePath(), skeletizedImage, binarizedImage);
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
				PaneDimension.WIDTH_BINARIZED, PaneDimension.HEIGHT_BINARIZED);
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

		skeletizedImage = skeletized;
		binarizedImage = binarized;
	}

	@Override
	public void onImageRestored(BufferedImage restored, int matching,
			int falsePositives, int falseNegatives) {
		final Dimension dimension = new Dimension(
				PaneDimension.WIDTH_BINARIZED, PaneDimension.HEIGHT_BINARIZED);
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

		skeletizedView.setImage(restored);
		binarizedView.setImage(originalImage);
		imageView.setImage(image);

		statisticsPanel.init(matching, falsePositives, falseNegatives);
		imagePanel.add(statisticsPanel);
	}

	@Override
	public void onOriginalImageRead(File originalImageFile, Image originalImage) {
		final Dimension dimension = new Dimension(
				PaneDimension.WIDTH_BINARIZED, PaneDimension.HEIGHT_BINARIZED);
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

		binarizedView.setImage(originalImage);
		imageView.setImage(image);

		this.originalImage = originalImage;
		this.originalImageFile = originalImageFile;
	}
}
