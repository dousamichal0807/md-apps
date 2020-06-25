package md.jgames.jchess.logic;

public interface ChessboardListener {

    /**
     * This method is called when a move is done on the chessboard.
     *
     * @param evt {@link ChessboardEvent} object
     */
    void moveDone(ChessboardEvent evt);

    /**
     * This method is called when {@link Chessboard#undo()} is called on the chessboard.
     *
     * @param evt {@link ChessboardEvent} object
     */
    void moveUndone(ChessboardEvent evt);

    /**
     * This method is called when {@link Chessboard#undo()} is called on the chessboard.
     *
     * @param evt {@link ChessboardEvent} object
     */
    void moveRedone(ChessboardEvent evt);
}
