package md.jgames.jchess.activities;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public final class AboutDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JButton closeButton;
	private final JPanel bottom;
	private final JTextArea contentInner;
	private final JScrollPane contentOuter;

	public AboutDialog(final Window win) {
		super(win);
		
		contentInner = new JTextArea();
		
		contentOuter = new JScrollPane(contentInner);
		contentOuter.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(event -> {
			this.setVisible(false);
			this.dispose();
		});
		
		bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
		bottom.add(Box.createGlue());
		bottom.add(closeButton);
		bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		this.setLayout(new BorderLayout());
		this.add(contentOuter, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.PAGE_END);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setSize(400, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(win);
	}
}
