package md.jgames.jchess.logic;

import java.util.*;

import md.jcore.Disposable;
import md.jcore.collections.BasicTree;
import md.jcore.collections.BasicTreeNode;
import md.jcore.collections.MDCollections;
import md.jcore.collections.MDTree;
import md.jcore.io.ExecutableProcess;

public final class AnalysisChessboard extends Chessboard implements MDTree {
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
        Disposable.checkIsNotDisposed(this);
        return startingFEN;
    }

    @Override
    public String getCurrentFEN() {
        Disposable.checkIsNotDisposed(this);
        return currentFEN;
    }

    @Override
    public int doneMovesCount() {
        Disposable.checkIsNotDisposed(this);
        return doneMoves.size();
    }

    @Override
    public List<Move> doneMoves() {
        Disposable.checkIsNotDisposed(this);
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
        Disposable.checkIsNotDisposed(this);
        Utilities.assertFENValidity(fen);
        moveTree.rootNode().childNodes().clear();
        doneMoves.clear();
        startingFEN = fen;

        update();
    }

    @Override
    public void undo() {
        Disposable.checkIsNotDisposed(this);
        if (!doneMoves.isEmpty()) {
            doneMoves.remove(doneMoves.size() - 1);
            update();
        }
    }

    @Override
    public void redo() {
        Disposable.checkIsNotDisposed(this);
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
        Disposable.checkIsNotDisposed(this);
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
        Disposable.checkIsNotDisposed(this);
        return Collections.unmodifiableSortedSet(possibleMoves);
    }

    @Override
    public byte[][] pieces() {
        Disposable.checkIsNotDisposed(this);
        byte[][] p = new byte[8][];
        for (int i = 0; i < 8; i++)
            p[i] = Arrays.copyOf(pieces[i], 8);
        return p;
    }

    @Override
    public byte pieceAt(final String square) {
        Disposable.checkIsNotDisposed(this);
        Utilities.assertSquareValidity(square);

        int rank = square.charAt(1) - '1';
        int file = square.charAt(2) - 'a';

        return pieces[rank][file];
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
        moveTree.setRootNode(new BasicTreeNode<>(null));
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
     * @throws md.jcore.AlreadyDisposedException if given chessboard is already
     *                                           disposed
     */
    public AnalysisChessboard(final GamePlayChessboard chessboard) {
        this(chessboard.getStartingFEN());
        for (Move move : chessboard.doneMoves())
            performMove(move);
    }

    @Override
    public void dispose() {
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
