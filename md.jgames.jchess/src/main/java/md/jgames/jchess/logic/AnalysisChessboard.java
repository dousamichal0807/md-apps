package md.jgames.jchess.logic;

import mdlib.utils.Disposable;
import mdlib.utils.collections.BasicTree;
import mdlib.utils.collections.BasicTreeNode;
import mdlib.utils.collections.MDCollections;
import mdlib.utils.collections.MDTree;
import mdlib.utils.io.ExecutableProcess;

import java.util.*;

public final class AnalysisChessboard extends Chessboard implements MDTree, Disposable {
    private String startingFEN, currentFEN;
    private BasicTree<Move> moveTree;
    private Vector<Integer> doneMoves;
    private ExecutableProcess stockfishProcess;
    private TreeSet<Move> possibleMoves;
    private byte[][] pieces;

    private void update() {
        if (stockfishProcess == null) {
            stockfishProcess = Utilities.createStockfishProcess();
            stockfishProcess.start();
        }

        // Set position
        Utilities.setPosition(stockfishProcess, startingFEN, doneMoves());

        // Get current FEN
        currentFEN = Utilities.getPosition(stockfishProcess);

        // Get all possible moves
        TreeMap<Move, Integer> moves = Utilities.getAllMovesRating(stockfishProcess, 1);
        possibleMoves.clear();
        possibleMoves.addAll(moves.keySet());

        // Map pieces
        pieces = Utilities.mapPieces(currentFEN);
    }

    @Override
    public String getStartingFEN() {
        Disposable.requireNotDisposed(this);
        return startingFEN;
    }

    @Override
    public String getCurrentFEN() {
        Disposable.requireNotDisposed(this);
        return currentFEN;
    }

    @Override
    public int doneMovesCount() {
        Disposable.requireNotDisposed(this);
        return doneMoves.size();
    }

    @Override
    public List<Move> doneMoves() {
        Disposable.requireNotDisposed(this);
        ArrayList<Move> moves = new ArrayList<>(doneMoves.size());
        BasicTreeNode<Move> node = moveTree.rootNode();
        for (int i = 0; i < doneMoves.size(); i++) {
            node = node.childNodes().get(i);
            moves.add(node.getValue());
        }
        return Collections.unmodifiableList(moves);
    }

    @Override
    public void reset(final String fen) {
        Disposable.requireNotDisposed(this);
        Utilities.assertFENValidity(fen);
        moveTree.rootNode().childNodes().clear();
        doneMoves.clear();
        startingFEN = fen;

        update();
    }

    @Override
    public void undo() {
        Disposable.requireNotDisposed(this);
        if (!doneMoves.isEmpty()) {
            doneMoves.remove(doneMoves.size() - 1);
            update();
        }
    }

    @Override
    public void redo() {
        Disposable.requireNotDisposed(this);
        // Get the current node and check if it has at least one subnode
        BasicTreeNode<Move> currentNode = moveTree.rootNode();
        for (int i : doneMoves)
            currentNode = currentNode.childNodes().get(i);
        // If it has no children, then, then, cannot redo move
        if (!currentNode.childNodes().isEmpty())
            doneMoves.add(0);
    }

    @Override
    public void performMove(final Move move) {
        Disposable.requireNotDisposed(this);
        // Move cannot be null
        if (move == null)
            throw new NullPointerException("Move to perform cannot be null");

        // Is this move possible?
        if (!possibleMoves.contains(move))
            throw new IllegalStateException("Cannot perform given move - it is not a possible move");

        // Get the current node where we are
        BasicTreeNode<Move> currentNode = moveTree.rootNode();
        for (int i : doneMoves)
            currentNode = currentNode.childNodes().get(i);

        // Is this move already present in the tree? If yes, don't add it again.
        for (int i = 0; i < currentNode.childNodes().size(); i++) {
            BasicTreeNode<Move> node = currentNode.childNodes().get(i);
            if (node.getValue().equals(move)) {
                // We have found that move
                doneMoves.add(i);
                return;
            }
        }

        // Otherwise, create new node and add it
        doneMoves.add(currentNode.childNodes().size());
        currentNode.childNodes().add(new BasicTreeNode<>(move));
    }

    @Override
    public SortedSet<Move> possibleMoves() {
        Disposable.requireNotDisposed(this);
        return Collections.unmodifiableSortedSet(possibleMoves);
    }

    @Override
    public byte[][] pieces() {
        // The chessboard cannot be disposed
        Disposable.requireNotDisposed(this);
        byte[][] p = new byte[8][];
        for (int i = 0; i < 8; i++)
            p[i] = Arrays.copyOf(pieces[i], 8);
        return p;
    }

    @Override
    public byte pieceAt(final Square square) {
        // The chessboard cannot be disposed
        Disposable.requireNotDisposed(this);
        // Require square to be non-null value
        Objects.requireNonNull(square, "Passing null as argument is illegal in this method");
        // Return piece
        return pieces[square.rank()][square.file()];
    }

    @Override
    public BasicTreeNode.Unmodifiable<Move> rootNode() {
        return MDCollections.unmodifiableTree(moveTree).rootNode();
    }

    /**
     * Creates an instance of this class with initial position set to standard
     * starting position.
     */
    public AnalysisChessboard() {
        this(Utilities.FEN_STARTING_POSITION);
    }

    /**
     * Creates an instance of this class with initial position annotated with FEN
     * given as parameter.
     * @param fen FEN of the initial position
     */
    public AnalysisChessboard(final String fen) {
        moveTree = new BasicTree<>();
        doneMoves = new Vector<>();
        possibleMoves = new TreeSet<>();

        if (stockfishProcess != null)
            stockfishProcess.send("ucinewgame");

        reset(fen);
    }

    /**
     * Creates an instance of {@link AnalysisChessboard} from
     * {@link GamePlayChessboard} instance.
     *
     * @param chessboard the chessboard to be new instance created from
     *
     * @throws NullPointerException if {@code null} is given as argument
     */
    public AnalysisChessboard(final GamePlayChessboard chessboard) {
        this(chessboard.getStartingFEN());
        for (Move move : chessboard.doneMoves())
            performMove(move);
    }

    @Override
    public void close() {
        if (!isDisposed()) {
            if (stockfishProcess != null && stockfishProcess.isAlive())
                stockfishProcess.close();

            stockfishProcess = null;
            startingFEN = null;
            currentFEN = null;
            moveTree = null;
            doneMoves = null;
            possibleMoves = null;
            pieces = null;
        }
    }

    @Override
    public boolean isDisposed() {
        return moveTree == null;
    }
}
