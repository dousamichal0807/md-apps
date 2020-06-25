package md.jgames.jchess.logic;

import mdlib.utils.Disposable;
import mdlib.utils.io.ExecutableProcess;

import java.util.*;

/**
 * Represents chessboard good for gameplay. If you want to analyze a game, use {@link
 * md.jgames.jchess.logic.AnalysisChessboard AnalysisChessboard} instead.
 *
 * @author Michal Douša
 * @see #GamePlayChessboard()
 * @see #GamePlayChessboard(String)
 * @see #GamePlayChessboard(SerializableGamePlayChessboard)
 * @see #reset()
 * @see SerializableGamePlayChessboard
 * @see AnalysisChessboard
 */
public final class GamePlayChessboard extends Chessboard implements Disposable {

    private final ExecutableProcess stockfishProcess;
    private String startingFEN, currentFEN;
    private final ArrayList<Move> moves;
    private final TreeSet<Move> possibleMoves;
    private byte[][] pieces;
    private int movesDone;

    private void update() {
        Utilities.setPosition(stockfishProcess, startingFEN, doneMoves());
        currentFEN = Utilities.getPosition(stockfishProcess);
        pieces = Utilities.mapPieces(currentFEN);
        TreeMap<Move, Integer> mappedMoves = Utilities.getAllMovesRating(stockfishProcess, 1);
        possibleMoves.clear();
        possibleMoves.addAll(mappedMoves.keySet());
    }

    @Override
    public String startingFEN() {
        Disposable.requireNotDisposed(this);
        return startingFEN;
    }

    @Override
    public List<Move> doneMoves() {
        Disposable.requireNotDisposed(this);
        return Collections.unmodifiableList(moves.subList(0, movesDone));
    }

    @Override
    public int doneMovesCount() {
        Disposable.requireNotDisposed(this);
        return movesDone;
    }

    public List<Move> getAllMoves() {
        Disposable.requireNotDisposed(this);
        if (moves == null)
            throw new IllegalStateException("Chessboard is disposed");
        return Collections.unmodifiableList(moves);
    }

    @Override
    public String currentFEN() {
        Disposable.requireNotDisposed(this);
        if (currentFEN == null)
            throw new IllegalStateException("Chessboard is disposed");
        return currentFEN;
    }

    @Override
    public SortedSet<Move> possibleMoves() {
        Disposable.requireNotDisposed(this);
        return Collections.unmodifiableSortedSet(possibleMoves);
    }

    @Override
    public byte[][] pieces() {
        Disposable.requireNotDisposed(this);
        byte[][] pieces = new byte[8][8];
        for (int i = 0; i < 8; i++)
            pieces[i] = Arrays.copyOf(this.pieces[i], 8);
        return pieces;
    }

    @Override
    public byte pieceAt(final Square square) {
        // Require non-null
        Objects.requireNonNull(square, "Illegaly r;eceived null as argument");
        // Return at specific rank and file
        return pieces[square.rank()][square.file()];
    }

    @Override
    public void reset(final String fen) {
        Disposable.requireNotDisposed(this);
        Utilities.assertFENValidity(fen);
        moves.clear();
        startingFEN = fen;
        movesDone = 0;

        // Always send 'ucinewgame' if we are overwriting chessboard with new game
        // For more information see Universal Chess Interface (UCI) standard.
        stockfishProcess.send("ucinewgame");

        update();
    }

    @Override
    public void performMove(final Move move) {
        Disposable.requireNotDisposed(this);
        if (move == null)
            throw new NullPointerException("Performed move cannot be null");
        if (!possibleMoves.contains(move))
            throw new IllegalArgumentException("Not a possible move on this chessboard!");
        ArrayList<Move> newMoves = new ArrayList<>();
        for (int i = 0; i < movesDone; i++)
            newMoves.add(moves.get(i));
        newMoves.add(move);

        moves.clear();
        moves.addAll(newMoves);
        movesDone++;

        update();

        ChessboardEvent event = new ChessboardEvent(this, move);
        for (ChessboardListener listener : this.getChessboardListeners()) {
            Thread thread = new Thread(() -> listener.moveDone(event));
            thread.start();
        }
    }

    /**
     * Sets how many moves should be done.
     *
     * @param doneMoves how many moves should be done
     * @throws IllegalArgumentException if value less than 0 or more than all moves count is passed
     */
    public void setDoneMovesCount(final int doneMoves) {
        Disposable.requireNotDisposed(this);
        if (doneMoves < 0 || doneMoves > moves.size())
            throw new IllegalArgumentException("Invalid # of moves to be done");
        this.movesDone = doneMoves;
        this.update();
    }

    @Override
    public void undo() {
        Disposable.requireNotDisposed(this);
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
        Disposable.requireNotDisposed(this);
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
     * @throws StockfishNotFoundException when Stockfish chess engine executable is not found.
     */
    public GamePlayChessboard() {
        this(Utilities.FEN_STARTING_POSITION);
    }

    /**
     * Creates chessboard with starting position specified by the passed FEN.
     *
     * @param fen the FEN to use as starting position
     * @throws StockfishNotFoundException when Stockfish chess engine executable is not found.
     * @throws IllegalArgumentException   if invalid FEN is passed
     */
    public GamePlayChessboard(final String fen) {
        Utilities.assertFENValidity(fen);
        moves = new ArrayList<>();
        possibleMoves = new TreeSet<>();
        startingFEN = fen;
        movesDone = 0;
        stockfishProcess = Utilities.createStockfishProcess();
        stockfishProcess.start();
        update();
    }

    /**
     * Creates new instance using information of {@link SerializableGamePlayChessboard} instance.
     *
     * @param sboard {@link SerializableGamePlayChessboard} instance to be the instance created from
     * @throws NullPointerException if {@code null} is given as argument
     */
    public GamePlayChessboard(final SerializableGamePlayChessboard sboard) {
        if (sboard == null)
            throw new NullPointerException("Cannot pass null as argument");
        startingFEN = sboard.getStartingFEN();
        movesDone = sboard.getDoneMovesCount();
        moves = new ArrayList<>();
        possibleMoves = new TreeSet<>();
        short[] sbmoves = sboard.getMoves();
        stockfishProcess = Utilities.createStockfishProcess();
        stockfishProcess.start();
        for (short sbmove : sbmoves)
            this.moves.add(new Move(sbmove));

        update();
    }

    @Override
    public void close() {
        if (!isDisposed()) {
            moves.clear();
            possibleMoves.clear();
            movesDone = 0;
            startingFEN = null;
            currentFEN = null;
            stockfishProcess.close();
        }
    }

    @Override
    public boolean isDisposed() {
        return moves == null;
    }
}
