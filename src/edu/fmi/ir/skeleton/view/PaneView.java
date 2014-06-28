package edu.fmi.ir.skeleton.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import edu.fmi.ir.skeleton.FilePickerCallback;
import edu.fmi.ir.skeleton.ImageProcessingCallback;

public class PaneView extends JPanel implements ActionListener,
		ImageProcessingCallback {

	/**
	 * {@value}
	 */
	private static final String SAVE_IMAGE = "Запази";

	/**
	 * {@value}
	 */
	private static final String BROWSE_IMAGE = "Избери картинка";

	/**
	 * {@value}
	 */
	private static final long serialVersionUID = -9067644135288203859L;

	private final JButton openButton;

	private final JButton saveButton;

	private final JFileChooser fileChooser;

	private final ImageView imageView;

	private FilePickerCallback fileCallback;

	public static class SimpleFileCallback implements FilePickerCallback {
		@Override
		public void onFileSelected(File selected) {
			// blank
		}
	}

	public PaneView() {
		super(new BorderLayout());

		fileChooser = new JFileChooser();

		saveButton = createButton(SAVE_IMAGE);
		openButton = createButton(BROWSE_IMAGE);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(openButton);
		buttonPanel.add(saveButton);

		add(buttonPanel, BorderLayout.PAGE_END);

		final Dimension layoutDimension = new Dimension(PaneDimension.WIDTH,
				PaneDimension.HEIGHT);
		setPreferredSize(layoutDimension);
		setMinimumSize(layoutDimension);
		setMaximumSize(layoutDimension);

		fileCallback = new SimpleFileCallback();

		imageView = new ImageView();
		add(imageView);
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
				fileCallback.onFileSelected(fileChooser.getSelectedFile());
			}
		} else if (e.getSource() == saveButton) {
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				System.out.println("saving " + file.getName());
			} else {
				System.out.println("save cancelled");
			}
		}
	}

	public void setFileCallback(final FilePickerCallback callback) {
		this.fileCallback = callback;
	}

	@Override
	public void onImageProcessed(Image image) {
		imageView.onImageProcessed(image);
	}

}
