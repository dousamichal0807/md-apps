package md.jgames.jchess.components;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import md.jgames.jchess.settings.theming.ChessboardPiecesTheme;

public class ChessboardPiecesThemeSelect extends JList<ChessboardPiecesTheme> {
	private static final long serialVersionUID = 1L;
	
	public ChessboardPiecesThemeSelect() {
		renderer = new ChessboardPiecesThemeSelectCellRenderer();
	}

	private final ChessboardPiecesThemeSelectCellRenderer renderer;

	@Override
	public ListCellRenderer<? super ChessboardPiecesTheme> getCellRenderer() {
		return renderer;
	}
	
}
