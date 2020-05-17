package md.jcore.frames;

import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JFrame {
	private static final long serialVersionUID = 1L;

	private final JProgressBar progressBar;
	private final JLabel label;
    private final JLabel imageContainer;

	public SplashScreen() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		imageContainer = new JLabel();
		this.add(imageContainer, gbc);

		gbc.gridy++;
		gbc.weighty = 0.0;
		label = new JLabel();
		this.add(label, gbc);

		gbc.gridy++;
		progressBar = new JProgressBar();
		this.add(progressBar, gbc);

		this.setResizable(false);
		this.setUndecorated(true);
		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
	}

	public void setProgressBarMinimum(int n) {
		progressBar.setMinimum(n);
	}

	public int getProgressBarMinimum() {
		return progressBar.getMinimum();
	}

	public void setProgressBarMaximum(int n) {
		progressBar.setMaximum(n);
	}

	public int getProgressBarMaximum() {
		return progressBar.getMaximum();
	}

	public void setProgressBarValue(int n) {
		progressBar.setValue(n);
	}

	public int getProgressBarValue() {
		return progressBar.getValue();
	}

	public void addToProgressBarValue(int n) {
		progressBar.setValue(n + progressBar.getValue());
	}
	
	public void setLabelText(String text) {
		label.setText(text);
	}
	
	public String getLabelText() {
		return label.getText();
	}
}
