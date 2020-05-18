package md.jgames.jchess.logic;

import java.util.*;

import md.jcore.Disposable;
import md.jcore.io.ExecutableProcess;

/**
 * Represents chessboard good for gameplay. If you want to analyze a game, use
 * {@link md.jgames.jchess.logic.AnalysisChessboard AnalysisChessboard} instead.
 * 
 * @author Michal Douša
 *
 * @see #GamePlayChessboard()
 * @see #GamePlayChessboard(String)
 * @see #GamePlayChessboard(SerializableGamePlayChessboard)
 * @see #reset()
 * @see SerializableGamePlayChessboard
 * @see AnalysisChessboard
 */
public final class GamePlayChessboard extends Chessboard {
	private ExecutableProcess stockfishProcess;
	private String startingFEN, currentFEN;
	private ArrayList<Move> moves;
	private TreeSet<Move> possibleMoves;
	private byte[][] pieces;
	private Integer movesDone;

	private void update() {
		md.jcore.debug.Debugger.info(getClass(), "Updating chessboard...");

		// Create Stockfish process if not created yet
		if (stockfishProcess == null) {
			md.jcore.debug.Debugger.info(getClass(), "Creating Stockfish process...");
			stockfishProcess = Utilities.createStockfishProcess();
			stockfishProcess.start();
		}
		
		md.jcore.debug.Debugger.info(getClass(), "Setting up position...");
		Utilities.setPosition(stockfishProcess, startingFEN, doneMoves());
	
		md.jcore.debug.Debugger.info(getClass(), "Computing current FEN...");
		currentFEN = Utilities.getPosition(stockfishProcess);
	
		md.jcore.debug.Debugger.info(getClass(), "Mapping current FEN...");
		pieces = Utilities.mapPieces(currentFEN);
	
		md.jcore.debug.Debugger.info(getClass(), "Mapping possible moves...");
		TreeMap<Move, Integer> mappedMoves = Utilities.getAllMovesRating(stockfishProcess, 1);
		possibleMoves.clear();
		possibleMoves.addAll(mappedMoves.keySet());
	
		md.jcore.debug.Debugger.info(getClass(), "Update of chessboard done.");
	}

	@Override
	public String getStartingFEN() {
		Disposable.checkIsNotDisposed(this);
		return startingFEN;
	}

	@Override
	public List<Move> doneMoves() {
		Disposable.checkIsNotDisposed(this);
		return Collections.unmodifiableList(moves.subList(0, movesDone));
	}
	
	@Override
	public int doneMovesCount() {
		Disposable.checkIsNotDisposed(this);
		return movesDone;
	}

	public List<Move> getAllMoves() {
		Disposable.checkIsNotDisposed(this);
		if (moves == null)
			throw new IllegalStateException("Chessboard is disposed");
		return Collections.unmodifiableList(moves);
	}

	@Override
	public String getCurrentFEN() {
		Disposable.checkIsNotDisposed(this);
		if (currentFEN == null)
			throw new IllegalStateException("Chessboard is disposed");
		return currentFEN;
	}

	@Override
	public SortedSet<Move> possibleMoves() {
		Disposable.checkIsNotDisposed(this);
		return Collections.unmodifiableSortedSet(possibleMoves);
	}

	@Override
	public byte[][] pieces() {
		Disposable.checkIsNotDisposed(this);
		byte[][] pieces = new byte[8][8];
		for (int i = 0; i < 8; i++)
			Arrays.copyOf(this.pieces[i], 8);
		return pieces;
	}
	
	@Override
	public byte pieceAt(String square) {
		// Checking state and validity
		Disposable.checkIsNotDisposed(this);
		Utilities.checkSquare(square);
		// Get rank and file
		int rank = square.charAt(1) - '1';
		int file = square.charAt(0) - 'a';
		// Return at specific rank and file
		return pieces[rank][file];
	}

	@Override
	public void reset(String fen) {
		Disposable.checkIsNotDisposed(this);
		Utilities.checkFEN(fen);
		moves.clear();
		startingFEN = fen;
		movesDone = 0;

		// Reset Stockfish if process exists
		if (stockfishProcess != null) {
			stockfishProcess.send("ucinewgame");
		}
		update();
	}

	@Override
	public void performMove(Move move) {
		Disposable.checkIsNotDisposed(this);
		if (move == null)
			throw new NullPointerException("Performed move cannot be null");
		if (!possibleMoves.contains(move))
			throw new IllegalArgumentException("Not a possible move on this chessboard!");
		ArrayList<Move> newMoves = new ArrayList<>();
		for (int i = 0; i < movesDone; i++)
			newMoves.add(moves.get(i));
		newMoves.add(move);

		moves = newMoves;
		movesDone++;

		update();

		ChessboardEvent event = new ChessboardEvent(this, move);
		for (ChessboardListener listener : this.getChessboardListeners()) {
			Thread thread = new Thread(() -> listener.moveDone(event));
			thread.start();
		}
	}

	public void setDoneMovesCount(int doneMoves) {
		Disposable.checkIsNotDisposed(this);
		if (doneMoves < 0 || doneMoves > moves.size())
			throw new IllegalArgumentException("Invalid range of number of move.");
		this.movesDone = doneMoves;
		this.update();
	}

	@Override
	public void undo() {
		Disposable.checkIsNotDisposed(this);
		if (movesDone > 0) {
			movesDone--;
			update();
			ChessboardEvent event = new ChessboardEvent(this, moves.get(movesDone));
			for (ChessboardListener listener : this.getChessboardListeners()) {
				Thread thread = new Thread(() -> listener.moveUndone(event));
				thread.start();
			}
		}
	}

	@Override
	public void redo() {
		Disposable.checkIsNotDisposed(this);
		if (movesDone < moves.size()) {
			movesDone++;
			update();
			ChessboardEvent event = new ChessboardEvent(this, moves.get(movesDone - 1));
			for (ChessboardListener listener : this.getChessboardListeners()) {
				Thread thread = new Thread(() -> listener.moveRedone(event));
				thread.start();
			}
		}
	}

	/**
	 * Creates chessboard with usual starting position.
	 * 
	 * @throws StockfishNotFoundException when Stockfish chess engine executable is
	 *                                    not found.
	 */
	public GamePlayChessboard() {
		this(Utilities.FEN_STARTING_POSITION);
	}

	/**
	 * Creates chessboard with starting position specified by the passed FEN.
	 * 
	 * @param fen the FEN to use as starting position
	 * @throws StockfishNotFoundException when Stockfish chess engine executable is
	 *                                    not found.
	 * 
	 * @throws IllegalArgumentException   if invalid FEN is passed
	 */
	public GamePlayChessboard(String fen) {
		Utilities.checkFEN(fen);
		moves = new ArrayList<>();
		possibleMoves = new TreeSet<>();
		startingFEN = fen;
		movesDone = 0;
		update();
	}

	/**
	 * Creates new instance using information of {@link SerializableGamePlayChessboard}
	 * instance.
	 *
	 * @param sboard {@link SerializableGamePlayChessboard} instance to be the
	 *               instance created from
	 *
	 * @throws NullPointerException if {@code null} is given as argument
	 */
	public GamePlayChessboard(SerializableGamePlayChessboard sboard) {
		if (sboard == null)
			throw new NullPointerException("Cannot pass null as argument");
		startingFEN = sboard.getStartingFEN();
		movesDone = sboard.getDoneMovesCount();
		moves = new ArrayList<>();
		possibleMoves = new TreeSet<>();
		short[] sbmoves = sboard.getMoves();
		for (short sbmove : sbmoves) {
			this.moves.add(new Move(sbmove));
		}

		update();
	}

	@Override
	public void dispose() {
		if(!isDisposed()) {
			moves = null;
			possibleMoves = null;
			movesDone = 0;
			startingFEN = null;
			currentFEN = null;
	
			stockfishProcess.close();
			stockfishProcess = null;
		}
	}

	@Override
	public boolean isDisposed() {
		return moves == null;
	}
}
