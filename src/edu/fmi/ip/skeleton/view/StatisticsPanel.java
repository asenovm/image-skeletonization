package edu.fmi.ip.skeleton.view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatisticsPanel extends JPanel {

	/**
	 * {@value}
	 */
	private static final long serialVersionUID = 7548945838549726830L;

	private final JLabel matchingLabel;

	private final JLabel falsePositivesLabel;

	private final JLabel falseNegativesLabel;

	public StatisticsPanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		matchingLabel = new JLabel();
		falsePositivesLabel = new JLabel();
		falseNegativesLabel = new JLabel();

		add(matchingLabel);
		add(falseNegativesLabel);
		add(falsePositivesLabel);
	}

	public void init(final int matching, final int falsePositives,
			final int falseNegatives) {
		matchingLabel.setText("Matching:" + matching);
		falsePositivesLabel.setText("False Positives:" + falsePositives);
		falseNegativesLabel.setText("False Negatives:" + falseNegatives);
	}

}
