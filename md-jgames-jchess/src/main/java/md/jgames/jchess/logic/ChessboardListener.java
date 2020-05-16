package md.jgames.jchess.logic;

public interface ChessboardListener {
	public void moveDone(ChessboardEvent evt);
	public void moveUndone(ChessboardEvent evt);
	public void moveRedone(ChessboardEvent evt);
}
