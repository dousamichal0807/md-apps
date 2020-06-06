package md.jgames.jchess.logic;

import mdlib.utils.IllegalStringFormatException;

public final class IllegalUCIMoveNotationException extends IllegalStringFormatException {
	private static final long serialVersionUID = 0x0100L;

	public IllegalUCIMoveNotationException(final String move) {
		super("Illegal UCI move notation: '" + move + "'");
	}
}
