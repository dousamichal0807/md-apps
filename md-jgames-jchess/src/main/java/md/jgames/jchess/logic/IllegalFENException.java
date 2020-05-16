package md.jgames.jchess.logic;

import md.jcore.IllegalStringFormatException;

public class IllegalFENException extends IllegalStringFormatException {
	private static final long serialVersionUID = 0x0100L;

	public IllegalFENException(String fen) {
		super("Illegal FEN notation: \'" + fen + "\'");
	}
}
