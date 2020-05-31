package md.jcore.material.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.MenuComponent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MaterialActivity extends JPanel {
	private static final long serialVersionUID = 0x0100L;

	private final ArrayList<WindowListener> windowListeners;
	
	private int defaultCloseOperation;
	private final JPanel contentPane;
	private MaterialAppBar appBar;

	public void addWindowListener(final WindowListener listener) {
		if(listener == null)
			throw new NullPointerException();
		if(!windowListeners.contains(listener))
			windowListeners.add(listener);
	}
	
	public void removeWindowListener(final WindowListener listener) {
		windowListeners.remove(listener);
	}
	
	public WindowListener[] getWindowListeners() {
		WindowListener[] listeners = new WindowListener[windowListeners.size()];
		for(int i = 0; i < listeners.length; i++)
			listeners[i] = windowListeners.get(i);
		return listeners;
	}
	
	public int getDefaultCloseOperation() {
		return defaultCloseOperation;
	}

	public void setDefaultCloseOperation(final int defaultCloseOperation) {
		switch(defaultCloseOperation) {
		case JFrame.EXIT_ON_CLOSE:
		case JFrame.DO_NOTHING_ON_CLOSE:
		case JFrame.DISPOSE_ON_CLOSE:
		case JFrame.HIDE_ON_CLOSE:
			this.defaultCloseOperation = defaultCloseOperation;
			break;
		default:
			throw new IllegalArgumentException("Invalid code of default close operation.");
		}
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public MaterialAppBar getAppBar() {
		return appBar;
	}

	public void setAppBar(final MaterialAppBar appBar) {
		if(this.appBar != null) super.remove(this.appBar);
		
		this.appBar = appBar;
		
		if(this.appBar != null) super.add(this.appBar, BorderLayout.PAGE_START);
		
		this.revalidate();
	}

	public MaterialActivity() {
		defaultCloseOperation = JFrame.EXIT_ON_CLOSE;
		contentPane = new JPanel(new BorderLayout());
		windowListeners = new ArrayList<>();
		
		super.setLayout(new BorderLayout());
		super.add(contentPane, BorderLayout.CENTER);
	}
	
	// THESE METHODS CANNOT BE USED!

	@Override
	public Component add(final Component comp) {
		return null;
	}

	@Override
	public Component add(final String name, final Component comp) {
		return null;
	}

	@Override
	public Component add(final Component comp, final int index) {
		return null;
	}

	@Override
	public void add(final Component comp, final Object constraints) {
	}

	@Override
	public void add(final Component comp, final Object constraints, final int index) {
	}

	@Override
	public void remove(final int index) {
	}

	@Override
	public void remove(final Component comp) {
	}

	@Override
	public void removeAll() {
	}

	@Override
	public void remove(final MenuComponent popup) {
	}

	@Override
	public void setLayout(final LayoutManager mgr) {
	}
}
