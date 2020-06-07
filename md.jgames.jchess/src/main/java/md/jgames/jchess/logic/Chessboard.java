package md.jgames.jchess.logic;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import mdlib.utils.Disposable;

/**
 * Base class for all classes implementing a chessboard for chess game.
 * 
 * @author Michal Douša
 *
 */
public abstract class Chessboard implements Disposable {
	private final Vector<ChessboardListener> chessboardListeners = new Vector<>();

	/**
	 * Represents a square where is no piece.
	 */
	public static final byte PIECE_NONE = 0;

	/**
	 * A constant representing White's pawn.
	 */
	public static final byte PIECE_WHITE_PAWN = 1;

	/**
	 * A constant representing White's knight.
	 */
	public static final byte PIECE_WHITE_KNIGHT = 2;

	/**
	 * A constant representing White's bishop.
	 */
	public static final byte PIECE_WHITE_BISHOP = 3;

	/**
	 * A constant representing White's rook.
	 */
	public static final byte PIECE_WHITE_ROOK = 4;

	/**
	 * A constant representing White's queen.
	 */
	public static final byte PIECE_WHITE_QUEEN = 5;

	/**
	 * A constant representing White's king.
	 */
	public static final byte PIECE_WHITE_KING = 6;

	/**
	 * A constant representing Black's pawn.
	 */
	public static final byte PIECE_BLACK_PAWN = 7;

	/**
	 * A constant representing Black's knight.
	 */
	public static final byte PIECE_BLACK_KNIGHT = 8;

	/**
	 * A constant representing White's bishop.
	 */
	public static final byte PIECE_BLACK_BISHOP = 9;

	/**
	 * A constant representing Black's rook.
	 */
	public static final byte PIECE_BLACK_ROOK = 10;

	/**
	 * A constant representing Black's queen.
	 */
	public static final byte PIECE_BLACK_QUEEN = 11;

	/**
	 * A constant representing Black's king.
	 */
	public static final byte PIECE_BLACK_KING = 12;

	/**
	 * Attaches a {@linkplain ChessboardListener} to this chessboard.
	 * 
	 * @param listener the listener to be added
	 * 
	 * @throws NullPointerException when {@code null} is passed
	 */
	public final void addChessboardListener(final ChessboardListener listener) {
		Disposable.checkIsNotDisposed(this);
		if (listener == null)
			throw new NullPointerException("Added listener is null object");
		chessboardListeners.add(listener);
	}

	/**
	 * Deassociates a {@linkplain ChessboardListener} from this chessboard.
	 * 
	 * @param listener the listener to be removed
	 * 
	 * @return {@code false}, if {@code null} is passed or the listener was not
	 *         attached to the chessboard object; if yes, then returns {@code true}
	 */
	public final boolean removeChessboardListener(final ChessboardListener listener) {
		Disposable.checkIsNotDisposed(this);
		return chessboardListeners.removeElement(listener);
	}

	/**
	 * Returns all chessboard listeners attached to this Chessboard in an array.
	 * 
	 * @return all chessboard listeners as specified above
	 */
	public final ChessboardListener[] getChessboardListeners() {
		Disposable.checkIsNotDisposed(this);
		ChessboardListener[] a = new ChessboardListener[chessboardListeners.size()];
		for (int i = 0; i < a.length; i++)
			a[i] = chessboardListeners.get(i);
		return a;
	}

	/**
	 * Used to get FEN of the position when the chessboard was initialized.
	 * 
	 * @return FEN of the position when initialized
	 * 
	 * @see #getCurrentFEN()
	 * @see #possibleMoves()
	 */
	public abstract String getStartingFEN();

	/**
	 * Used to get FEN of the current position on the chessboard.
	 * 
	 * @return FEN of current position
	 * 
	 * @see #getStartingFEN()
	 * @see #possibleMoves()
	 */
	public abstract String getCurrentFEN();

	/**
	 * Returns the number of done moves.
	 * 
	 * @return # of done moves.
	 * 
	 * @see #doneMoves()
	 * @see #getCurrentFEN()
	 * @see #possibleMoves()
	 * @see #undo()
	 * @see #redo()
	 */
	public abstract int doneMovesCount();

	/**
	 * Returns unmodifiable {@link List} of done moves.
	 * 
	 * @return unmodifiable {@link List} of done moves
	 */
	public abstract List<Move> doneMoves();

	/**
	 * Resets the chessbord to the usual starting position. All previous data stored
	 * in this object will be discarded.
	 */
	public void reset() {
		reset(Utilities.FEN_STARTING_POSITION);
	}

	/**
	 * Resets the chessbord to a position notated with the specified FEN. All
	 * previous data stored in this object will be discarded.
	 * 
	 * @param fen the FEN of the new position
	 */
	public abstract void reset(String fen);

	/**
	 * Undoes one move, if possible. Otherwise, calling this method has no effect on
	 * the chessboard.
	 */
	public abstract void undo();

	/**
	 * Redoes one move, if possible. Otherwise, calling this method has no effect on
	 * the chessboard.
	 */
	public abstract void redo();

	/**
	 * Performs move on the chessboard. Every class implementing this metod must
	 * trigger the {@link ChessboardListener#moveDone(ChessboardEvent)} method and
	 * throw {@link IllegalArgumentException} if the move cannot be done on the
	 * particular chessboard
	 * 
	 * @param move {@code Move} to be done
	 * 
	 * @throws IllegalArgumentException if the move cannot be done on the chessboard
	 */
	public abstract void performMove(Move move);

	/**
	 * Gets all possible moves in a {@link SortedSet} on the curent position.
	 * 
	 * @return all possible moves on the chessboard
	 */
	public abstract SortedSet<Move> possibleMoves();

	/**
	 * Gets all possible moves in an unmodifiable {@link SortedSet} of piece on
	 * particular square on the curent position. If entered square with no piece at
	 * all, empty set is returned.
	 * 
	 * @return possible moves for piece on particular square
	 */
	public SortedSet<Move> possibleMovesFor(final String sq) {
		Disposable.checkIsNotDisposed(this);

		TreeSet<Move> moveset = new TreeSet<>();
		for (Move move : possibleMoves())
			if (move.getSquareFrom().equals(sq))
				moveset.add(move);

		return Collections.unmodifiableSortedSet(moveset);
	}

	/**
	 * Returns an 2D array with 8 rows and 8 columns. As first index, number of rank
	 * is used (0 is first and 7 is eighth rank), and for second index, number of
	 * file is used (0 is a-file and 7 is h-file).
	 * 
	 * @return 2D array as specified above
	 */
	public abstract byte[][] pieces();

	/**
	 * Returns piece at particular square. Metho returns appropriate constant from
	 * {@link Chessboard} class.
	 * @param square the square to return the piece sitting on
	 * @return the piece sitting on the given square represented by a constant from
	 *         {@link Chessboard} class
	 */
	public abstract byte pieceAt(String square);

}
