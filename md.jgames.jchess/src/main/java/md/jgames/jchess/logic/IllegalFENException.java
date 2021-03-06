package md.jgames.jchess.logic;


import mdlib.utils.IllegalStringFormatException;

public final class IllegalFENException extends IllegalStringFormatException {
    private static final long serialVersionUID = 0x0100L;

    public IllegalFENException(final String fen) {
        super("Illegal FEN notation: '" + fen + "'");
    }
}
