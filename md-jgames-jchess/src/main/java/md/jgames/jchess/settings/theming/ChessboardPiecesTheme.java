package md.jgames.jchess.settings.theming;

import java.awt.Image;
import java.util.HashMap;

public final class ChessboardPiecesTheme {
	private String id, name, desc;
	private HashMap<Character, Image> piecesImages;
	
	public static ChessboardPiecesTheme load(String id) {
		return null;
	}

	public String getID() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return desc;
	}
	public Image getImageForPiece(char p) {
		return piecesImages.get(p);
	}
}
