package md.jgames.jchess.logic;

import md.jcore.IllegalStringFormatException;

public class IllegalUCIMoveNotation extends IllegalStringFormatException {
	private static final long serialVersionUID = 0x0100L;

	public IllegalUCIMoveNotation(String move) {
		super("Illegal UCI move notation: \'" + move + "\'");
	}
}
