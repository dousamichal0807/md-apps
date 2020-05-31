package md.jgames.jchess.logic;

import md.jcore.IllegalStringFormatException;

public final class IllegalSquareException extends IllegalStringFormatException {
	private static final long serialVersionUID = 0x0100L;

	public IllegalSquareException(final String square) {
		super("Illegal square notation: '" + square + "'");
	}
}
