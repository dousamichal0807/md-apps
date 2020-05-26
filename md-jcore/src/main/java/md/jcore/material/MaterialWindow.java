package md.jcore.material;

import md.jcore.material.components.MaterialActivity;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MaterialWindow extends JFrame implements WindowListener {
	private static final long serialVersionUID = 0x0100L;

	private MaterialActivity activity;

	public MaterialWindow() {
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(this);
	}

	public MaterialActivity getCurrentActivity() {
		return activity;
	}

	public void switchActivity(final MaterialActivity activity) {
		SwingUtilities.invokeLater(() -> {
			if (this.activity != null) {
				remove(this.activity);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

			this.activity = activity;

			if (this.activity != null) {
				add(this.activity, BorderLayout.CENTER);
				setDefaultCloseOperation(this.activity.getDefaultCloseOperation());
			}

			revalidate();
			repaint();
		});
	}

	@Override
	public void windowOpened(final WindowEvent e) {
		if (activity != null) for (int i = 0; i < activity.getWindowListeners().length; i++)
			activity.getWindowListeners()[i].windowOpened(e);
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		if (activity != null) for (int i = 0; i < activity.getWindowListeners().length; i++)
			activity.getWindowListeners()[i].windowClosing(e);
	}

	@Override
	public void windowClosed(final WindowEvent e) {
		if (activity != null) for (int i = 0; i < activity.getWindowListeners().length; i++)
			activity.getWindowListeners()[i].windowClosed(e);
	}

	@Override
	public void windowIconified(final WindowEvent e) {
		if (activity != null) for (int i = 0; i < activity.getWindowListeners().length; i++)
			activity.getWindowListeners()[i].windowIconified(e);
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
		if (activity != null) for (int i = 0; i < activity.getWindowListeners().length; i++)
			activity.getWindowListeners()[i].windowDeiconified(e);
	}

	@Override
	public void windowActivated(final WindowEvent e) {
		if (activity != null) for (int i = 0; i < activity.getWindowListeners().length; i++)
			activity.getWindowListeners()[i].windowActivated(e);
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
		if (activity != null) for (int i = 0; i < activity.getWindowListeners().length; i++)
			activity.getWindowListeners()[i].windowDeactivated(e);
	}
}
