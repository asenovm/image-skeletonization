package edu.fmi.ir.skeleton.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import edu.fmi.ir.skeleton.FileCallback;

public class PaneView extends JPanel implements ActionListener {

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

	private FileCallback fileCallback;

	public static class SimpleFileCallback implements FileCallback {
		@Override
		public void onFileSelected(File selected) {
			// blank
		}
	}

	public PaneView() {
		super(new BorderLayout());

		fileChooser = new JFileChooser();

		openButton = new JButton(BROWSE_IMAGE);
		openButton.addActionListener(this);

		saveButton = new JButton(SAVE_IMAGE);
		saveButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(openButton);
		buttonPanel.add(saveButton);

		add(buttonPanel, BorderLayout.PAGE_END);

		final Dimension layoutDimension = new Dimension(400, 600);
		setPreferredSize(layoutDimension);
		setMinimumSize(layoutDimension);
		setMaximumSize(layoutDimension);

		fileCallback = new SimpleFileCallback();
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

}
