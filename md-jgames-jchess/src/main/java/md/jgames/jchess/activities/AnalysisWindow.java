package md.jgames.jchess.activities;

import javax.swing.JFrame;

public class AnalysisWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static AnalysisWindow instance;
	public static AnalysisWindow getInstance() {
		if(instance == null)
			instance = new AnalysisWindow();
		return instance;
	}
	
	// ...
	
	private AnalysisWindow() {}
}
