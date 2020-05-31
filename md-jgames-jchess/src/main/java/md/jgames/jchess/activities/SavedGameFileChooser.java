package md.jgames.jchess.activities;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SavedGameFileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;
	private static SavedGameFileChooser instance;

	public static SavedGameFileChooser getInstance() {
		if (instance == null)
			instance = new SavedGameFileChooser();
		return instance;
	}

	private SavedGameFileChooser() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MD jChess Saved Game (*.md-jchess-savedgame)",
				"md-jchess-savedgame");
		this.setFileFilter(filter);
		this.addChoosableFileFilter(filter);
	}

	public File getSelectedFile() {
		File file = super.getSelectedFile();
		if(file == null)
			return null;
		if (this.getDialogType() == JFileChooser.SAVE_DIALOG) {
			String path = file.getAbsolutePath();
			if (!path.toLowerCase().endsWith(".md-jchess-savedgame")) file = new File(path + ".md-jchess-savedgame");
		}
		return file;
	}
}
