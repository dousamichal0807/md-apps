package md.jcore.material.activities;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import md.jcore.material.MaterialActivity;

public class SplashScreenActivity extends MaterialActivity {
	
	private static final long serialVersionUID = 0x0100L;
	
	private final JLabel iconLabel;
	private final JProgressBar progressBar;
	private final JLabel progressLabel;
	
	public SplashScreenActivity() {
		iconLabel = new JLabel();
		iconLabel.setAlignmentX(.5f);
		
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		progressBar.setMaximum(0);
		progressBar.setMaximumSize(new Dimension(400, 20));
		progressBar.setAlignmentX(.5f);
		
		progressLabel = new JLabel();
		progressLabel.setAlignmentX(.5f);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.getContentPane().setBorder(new EmptyBorder(100, 100, 100, 100));
		
		this.getContentPane().add(Box.createGlue());
		this.getContentPane().add(iconLabel);
		this.getContentPane().add(Box.createGlue());
		this.getContentPane().add(progressBar);
		this.getContentPane().add(progressLabel);
		this.getContentPane().add(Box.createGlue());
	}

	public void setProgressBarMaximum(final int n) {
		progressBar.setMaximum(n);
	}

	public void setProgressBarValue(final int n) {
		progressBar.setValue(n);
	}

	public void setLabelText(final String text) {
		progressLabel.setText(text);
	}

	public void setIcon(final Icon icon) {
		iconLabel.setIcon(icon);
	}
	
	
}
