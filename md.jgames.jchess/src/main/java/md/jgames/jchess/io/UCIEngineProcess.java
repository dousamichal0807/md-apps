package md.jgames.jchess.io;

import md.jgames.jchess.logic.Move;
import mdlib.utils.Disposable;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class UCIEngineProcess implements Disposable {

    protected final Process process;
    protected final Scanner standardStream, errorStream;
    protected final PrintStream commandStream;

    /**
     * Creates a {@link UCIEngineProcess} wrapper class instance from given {@link ProcessBuilder} instance.
     *
     * @param processBuilder the process to create an instance of this class from
     * @throws NullPointerException if {@code null} is given instead of a non-null instance
     */
    public UCIEngineProcess(final ProcessBuilder processBuilder) throws IOException {
        // Assert processBuider != null
        Objects.requireNonNull(processBuilder, "Cannot create new instance from null");
        // Create process
        process = processBuilder.start();
        // Initialization
        standardStream = new Scanner(process.getInputStream());
        errorStream = new Scanner(process.getErrorStream());
        commandStream = new PrintStream(process.getOutputStream());
    }

    /**
     * Tells the chess engine that new game will start.
     */
    public final synchronized void startNewGame() {
        commandStream.println("ucinewgame");
    }

    /**
     * Sets the position in the chess engine process via initial position and done moves from that position provided by
     * given {@link Iterator}.
     *
     * @param fen          the intial FEN position
     * @param moveIterator an instance of {@link Iterator} iterating over a collection of {@link Move}s.
     * @throws NullPointerException if given FEN is {@code null}
     * @see #setPosition(String, Iterable)
     * @see #setPosition(String, Move...)
     */
    public final synchronized void setPosition(final String fen, final Iterator<Move> moveIterator) {
        StringBuilder command = new StringBuilder();
        command.append("position fen ");
        command.append(fen);
        if (moveIterator != null && moveIterator.hasNext()) {
            command.append(" moves");
            while (moveIterator.hasNext()) {
                command.append(' ');
                command.append(moveIterator.next());
            }
        }

        commandStream.println(command);
    }

    /**
     * Sets the position in the chess engine process via initial position and done moves from that position.
     *
     * @param fen   the initial FEN position
     * @param moves done moves from the initial position
     * @throws NullPointerException if given FEN is {@code null}
     * @see #setPosition(String, Iterator)
     * @see #setPosition(String, Move...)
     */
    public final synchronized void setPosition(final String fen, final Iterable<Move> moves) {
        setPosition(fen, moves.iterator());
    }

    /**
     * Sets the position in the chess engine process via initial position and done moves from that position.
     *
     * @param fen   the initial FEN position
     * @param moves done moves from the initial position
     * @throws NullPointerException if given FEN is {@code null}
     * @see #setPosition(String, Iterable)
     * @see #setPosition(String, Iterator)
     */
    public final synchronized void setPosition(final String fen, final Move... moves) {
        setPosition(fen, Arrays.stream(moves));
    }

    /**
     * Sets the position in the chess engine process via initial position and done moves from that position.
     *
     * @param fen        the initial FEN position
     * @param moveStream a {@link Stream} of done moves from the initial position
     * @throws NullPointerException if given FEN is {@code null}
     * @see #setPosition(String, Iterable)
     * @see #setPosition(String, Iterator)
     */
    public final synchronized void setPosition(final String fen, final Stream<Move> moveStream) {
        setPosition(fen, moveStream.iterator());
    }

    /**
     * Waits while UCI engine is nor ready.
     */
    public final synchronized void waitForReady() {
        boolean readyok = false;
        commandStream.println("isready");

        while (!readyok) {
            // Waiting for 'readyok'
            String line = standardStream.nextLine().trim();
            readyok = line.equals("readyok");
        }
    }

    @Override
    public final synchronized void close() {
        commandStream.println("quit");
        process.destroy();
    }

    @Override
    public final boolean isDisposed() {
        return !process.isAlive();
    }
}
