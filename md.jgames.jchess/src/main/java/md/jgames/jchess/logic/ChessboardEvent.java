package md.jgames.jchess.logic;

public final class ChessboardEvent {
	private final Chessboard source;
	private final Move move;

	public Chessboard getSource() {
		return source;
	}

	public Move getMove() {
		return move;
	}

	ChessboardEvent(final Chessboard source, final Move move) {
		if (source == null)
			throw new NullPointerException("The source is set to null");
		this.source = source;
		this.move = move;
	}
}
