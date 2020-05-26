package md.jcore.material.components;

import md.jcore.material.MaterialConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

/**
 * Represets an app bar used by Material Design. More at
 * https://material.io/components/app-bars-top
 * 
 * @author dousamichal
 *
 */
public final class MaterialAppBar extends JPanel {
	private static final long serialVersionUID = 1L;

	private int elevation;
	private final JToolBar leftPanel;
    private final JToolBar centerPanel;
    private final JToolBar rightPanel;

	public MaterialAppBar() {
		this.setBackground(UIManager.getColor("AppBar.background"));
		this.setForeground(UIManager.getColor("AppBar.foreground"));
		this.setElevation(MaterialConstants.ELEVATION_ELEVATED);

		this.leftPanel = new JToolBar();
		this.centerPanel = new JToolBar();
		this.rightPanel = new JToolBar();

		this.setLayout(new BorderLayout());
		this.add(leftPanel, BorderLayout.LINE_START);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.LINE_END);
	}

	public int getElevation() {
		return elevation;
	}

	public void setElevation(int elevation) {
		switch (elevation) {
		case MaterialConstants.ELEVATION_FLAT:
		case MaterialConstants.ELEVATION_OUTLINED:
		case MaterialConstants.ELEVATION_ELEVATED:
			this.elevation = elevation;
		default:
			throw new IllegalArgumentException();
		}
	}

	public JToolBar getLeftPanel() {
		return leftPanel;
	}

	public JToolBar getRightPanel() {
		return rightPanel;
	}

	public JToolBar getCenterPanel() {
		return centerPanel;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(leftPanel.getPreferredSize().width + centerPanel.getPreferredSize().width
				+ rightPanel.getPreferredSize().width, 56);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, 56);
	}
}
