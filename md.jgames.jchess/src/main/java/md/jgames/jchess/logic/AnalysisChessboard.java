package md.jgames.jchess.logic;

import mdlib.utils.Disposable;
import mdlib.utils.collections.BasicTree;
import mdlib.utils.collections.BasicTreeNode;
import mdlib.utils.collections.MDCollections;
import mdlib.utils.collections.Tree;
import mdlib.utils.collections.TreeNode.UnmodifiableNode;
import mdlib.utils.io.ExecutableProcess;

import java.util.*;

/**
 * Represents a chessboard good for analyzing a game. Its counterpart, {@link GamePlayChessboard} is good for playing a
 * game. The moves are organised in a tree because of move variations, not in a list, as usually should be done. When
 * calling {@link #redo()}, the main line is automatically used as it is there as a first child of the tree/a node.
 */
public final class AnalysisChessboard extends Chessboard implements Tree<Move, UnmodifiableNode<Move>>, Disposable {

    private String startingFEN, currentFEN;
    private BasicTree<Move> doneMovesTree;
    private Vector<Integer> doneMovesIndices;
    private ExecutableProcess stockfishProcess;
    private TreeSet<Move> possibleMoves;
    private byte[][] pieces;

    private void update() {
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
    public String startingFEN() {
        Disposable.requireNotDisposed(this);
        return startingFEN;
    }

    @Override
    public String currentFEN() {
        Disposable.requireNotDisposed(this);
        return currentFEN;
    }

    @Override
    public int doneMovesCount() {
        Disposable.requireNotDisposed(this);
        return doneMovesIndices.size();
    }

    @Override
    public List<Move> doneMoves() {
        // Chessboard cannot be disposed
        Disposable.requireNotDisposed(this);
        // Create list with moves
        ArrayList<Move> moves = new ArrayList<>(doneMovesIndices.size());
        // Start with the root node then navigate down as specified in `this.doneMoves`
        BasicTreeNode<Move> node = doneMovesTree.getRootNode();
        for (Integer doneMove : doneMovesIndices) {
            node = node.childNodes().get(doneMove);
            moves.add(node.getValue());
        }
        // Return as unmodifiable list
        return Collections.unmodifiableList(moves);
    }

    @Override
    public void reset(final String fen) {
        Disposable.requireNotDisposed(this);
        Utilities.assertFENValidity(fen);
        stockfishProcess.send("ucinewgame");
        doneMovesTree.getRootNode().clearChildren();
        doneMovesIndices.clear();
        startingFEN = fen;

        update();
    }

    @Override
    public void undo() {
        Disposable.requireNotDisposed(this);
        if (!doneMovesIndices.isEmpty()) {
            doneMovesIndices.remove(doneMovesIndices.size() - 1);
            update();
        }
    }

    @Override
    public void redo() {
        Disposable.requireNotDisposed(this);
        // Get the current node and check if it has at least one subnode
        BasicTreeNode<Move> currentNode = doneMovesTree.getRootNode();
        for (int i : doneMovesIndices)
            currentNode = currentNode.childNodes().get(i);
        // If it has no children, then, then, cannot redo move
        if (!currentNode.childNodes().isEmpty())
            doneMovesIndices.add(0);
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
        BasicTreeNode<Move> currentNode = doneMovesTree.getRootNode();
        for (int i : doneMovesIndices)
            currentNode = currentNode.childNodes().get(i);

        // Is this move already present in the tree? If yes, don't add it again.
        for (int i = 0; i < currentNode.childNodes().size(); i++) {
            BasicTreeNode<Move> node = currentNode.childNodes().get(i);
            if (node.getValue().equals(move)) {
                // We have found that move
                doneMovesIndices.add(i);
                // update() and return
                update();
                return;
            }
        }

        // Otherwise, create new node and add it
        doneMovesIndices.add(currentNode.childNodes().size());
        currentNode.addChild(new BasicTreeNode<>(move));
        // Update chessboard
        update();
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
    public UnmodifiableNode<Move> getRootNode() {
        return MDCollections.unmodifiableTree(doneMovesTree).getRootNode();
    }

    /**
     * Creates an instance of this class with initial position set to standard starting position.
     */
    public AnalysisChessboard() {
        this(Utilities.FEN_STARTING_POSITION);
    }

    /**
     * Creates an instance of this class with initial position annotated with FEN given as parameter.
     *
     * @param fen FEN of the initial position
     */
    public AnalysisChessboard(final String fen) {
        doneMovesTree = new BasicTree<>();
        doneMovesIndices = new Vector<>();
        possibleMoves = new TreeSet<>();
        stockfishProcess = Utilities.createStockfishProcess();
        stockfishProcess.start();

        reset(fen);
    }

    /**
     * Creates an instance of {@link AnalysisChessboard} from {@link GamePlayChessboard} instance.
     *
     * @param chessboard the chessboard to be new instance created from
     * @throws NullPointerException if {@code null} is given as argument
     */
    public AnalysisChessboard(final GamePlayChessboard chessboard) {
        this(chessboard.startingFEN());
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
            doneMovesTree = null;
            doneMovesIndices = null;
            possibleMoves = null;
            pieces = null;
        }
    }

    @Override
    public boolean isDisposed() {
        return doneMovesTree == null;
    }
}
