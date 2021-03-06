package md.jgames.jchess.logic;

import mdlib.utils.ClasspathUtilities;
import mdlib.utils.io.ExecutableProcess;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains some useful static methods and constants.
 *
 * @author Michal Douša
 * @see ExecutableProcess
 * @see Chessboard
 */
public final class Utilities {

    // Do not create any instance
    private Utilities() {
    }

    // Initialize Stockfish path when it is requested through getStockfishPath() method
    private static Path stockfishPath = null;

    // Count of already created Stockfish processes. Used in createStockfishProcess() method.
    private static int createdStockfishProcessesCount = 0;

    /**
     * The FEN notation of the typical starting position.
     */
    public static final String FEN_STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**
     * Regex pattern for Forsyth-Edwards Notation. For testing, if FEN is valid use {@link #isValidFEN(String)} method
     * instead, because this regex does not cover all rules of FEN notation.
     *
     * @see #isValidFEN(String)
     * @see #assertFENValidity(String)
     */
    public static final Pattern PATTERN_FEN = Pattern.compile("((?:[prnbqkPRNBQK1-8]+/){7}[prnbqkPRNBQK1-8]+) ([wb]) (-|KQ?k?q?|K?Qk?q?|K?Q?kq?|K?Q?k?q) (-|[a-h][36]) ([0-9])+ ([0-9])+");

    /**
     * Regex pattern for read lines in {@link #getAllMovesRating(ExecutableProcess, int)} method. For more information,
     * see <code>go perft <em>&lt;depth&gt;</em></code> Stockfish command.
     *
     * @see #getAllMovesRating(ExecutableProcess, int)
     */
    private static final Pattern PATTERN_STOCKFISH_MOVE_RATING = Pattern.compile("([a-h][1-8][a-h][1-8][qbnr]?): ([0-9]+)");

    /**
     * Method testing if the specified string is a notation that use chess engines, for example Stockfish to annotate a
     * move.
     *
     * @param uciNotation input to be tested
     * @return a {@code boolean} value, if given input is valid UCI move notation
     */
    public static boolean isValidUCIMoveNotation(final String uciNotation) {
        return Move.PATTERN_UCI_MOVE.matcher(uciNotation).matches();
    }

    /**
     * Method testing if the given {@code String} is valid Forsyth-Edwards Notation of a position on the chessboard
     *
     * @param fen the FEN notation
     * @return {@code boolean} value, if given input is valid FEN notation
     *
     * @see #mapPieces(String)
     * @see #getPosition(ExecutableProcess)
     */
    public static boolean isValidFEN(final String fen) {
        Matcher matcher = PATTERN_FEN.matcher(fen);
        if (!matcher.matches())
            return false;

        String position = matcher.group(1);
        int halfmoves = Integer.parseInt(matcher.group(5));
        int fullmoves = Integer.parseInt(matcher.group(6));

        if (halfmoves < 0 && fullmoves <= 0)
            return false;

        String[] ranks = position.split("/");

        /*
         * Count of:
         * bk - Black's king(s)
         * bp - Black's pawns
         * bo - Black's pieces other than pawn and king
         * wk - White's king(s)
         * wp - White's pawns
         * wo - White's pieces other than pawn and king
         */
        byte bk = 0, bp = 0, bo = 0, wk = 0, wp = 0, wo = 0;

        for (int rankNo = 0; rankNo < ranks.length; rankNo++) {
            String rank = ranks[7 - rankNo];
            int f = 0;
            for (char ch : rank.toCharArray()) {
                f++;
                switch (ch) {
                    // King
                    case 'K':
                        wk++;
                        break;
                    case 'k':
                        bk++;
                        break;
                    // Pawn
                    case 'P':
                        if (rankNo == 7)
                            return false;
                        wp++;
                        break;
                    case 'p':
                        if (rankNo == 0)
                            return false;
                        bp++;
                        break;
                    // Other pieces
                    case 'Q':
                    case 'B':
                    case 'N':
                    case 'R':
                        wo++;
                        break;
                    case 'q':
                    case 'b':
                    case 'n':
                    case 'r':
                        bo++;
                        break;
                    default:
                        int s = (int) ch - '1';
                        if (s < 0 || s > 7)
                            return false;
                        f += s;
                        break;
                }
            }
            if (f != 8 || bk > 1 || wk > 1 || bp > 8 || wp > 8 || bp + bo > 15 || wp + wo > 15)
                return false;
        }
        //noinspection RedundantIfStatement
        if (bk != 1 || wk != 1)
            return false;

        return true;
    }

    /**
     * Checks, if FEN notation is valid. If not, automatically throws {@link IllegalFENException}
     *
     * @param fen the FEN notation to check
     * @throws IllegalFENException when FEN notation is illegal
     * @see #mapPieces(String)
     */
    public static void assertFENValidity(final String fen) {
        if (!isValidFEN(fen))
            throw new IllegalFENException(fen);
    }

    /**
     * Maps pieces to 8x8 2D array. Uses {@code byte} constants from {@link Chessboard} class. Number of rank is used as
     * first index and number of column is used as second index of the returned 2D array.
     *
     * @param fen FEN notation to map pieces from
     * @return {@code byte[][]} 2D array as specified above
     *
     * @throws IllegalFENException if invalid FEN is given
     */
    public static byte[][] mapPieces(final String fen) {
        Utilities.assertFENValidity(fen);
        byte[][] pieces = new byte[8][8];
        String[] ranks = fen.split("/");

        for (byte rankIndex = 0; rankIndex < 8; rankIndex++) {
            String rank = ranks[7 - rankIndex];
            for (byte fileIndex = 0, rankCharIndex = 0; rankCharIndex < rank.length() && fileIndex < 8; rankCharIndex++) {
                char rankChar = rank.charAt(rankCharIndex);
                if (rankChar >= '1' && rankChar <= '8')
                    fileIndex += rankChar - '0';
                else {
                    pieces[rankIndex][fileIndex] = pieceCharToConstant(rankChar);
                    fileIndex++;
                }
            }
        }

        return pieces;
    }

    /**
     * Converts {@code char}s representing pieces to equivalent {@code byte} constants in {@link Chessboard} class.
     *
     * @param piece the {@code char} to be converted
     * @return equivalent {@code byte} constant from {@link Chessboard} class
     *
     * @throws IllegalArgumentException if none of these {@code char}s is given as argument: {@code 'p'}, {@code 'r'},
     *                                  {@code 'n'}, {@code 'b'}, {@code 'q'}, {@code 'k'}, {@code 'P'}, {@code 'R'},
     *                                  {@code 'N'}, {@code 'B'}, {@code 'Q'}, {@code 'K'}
     */
    public static byte pieceCharToConstant(final char piece) {
        switch (piece) {
            case 'P':
                return Chessboard.PIECE_WHITE_PAWN;
            case 'R':
                return Chessboard.PIECE_WHITE_ROOK;
            case 'N':
                return Chessboard.PIECE_WHITE_KNIGHT;
            case 'B':
                return Chessboard.PIECE_WHITE_BISHOP;
            case 'Q':
                return Chessboard.PIECE_WHITE_QUEEN;
            case 'K':
                return Chessboard.PIECE_WHITE_KING;
            case 'p':
                return Chessboard.PIECE_BLACK_PAWN;
            case 'r':
                return Chessboard.PIECE_BLACK_ROOK;
            case 'n':
                return Chessboard.PIECE_BLACK_KNIGHT;
            case 'b':
                return Chessboard.PIECE_BLACK_BISHOP;
            case 'q':
                return Chessboard.PIECE_BLACK_QUEEN;
            case 'k':
                return Chessboard.PIECE_BLACK_KING;
            default:
                throw new IllegalArgumentException("Illegal piece: '" + piece + "'");
        }
    }

    /**
     * Generates a random Chess960 position.
     *
     * @return the FEN notation of some Chess960 position
     */
    public static String generateChess960FEN() {
        List<Character> firstRankPieces = Arrays.asList('r', 'n', 'b', 'q', 'k', 'b', 'n', 'r');
        String firstRank = null;
        while (firstRank == null) {
            Collections.shuffle(firstRankPieces);
            StringBuilder firstRankBuilder = new StringBuilder(8);
            for (Character firstRankPiece : firstRankPieces)
                firstRankBuilder.append(firstRankPiece);
            firstRank = firstRankBuilder.toString();
            if (!firstRank.matches(".*r.*k.*r.*") || !firstRank.matches(".*b(..|....|......|)b.*"))
                firstRank = null;
        }
        StringBuilder fenBuilder = new StringBuilder();
        fenBuilder.append(firstRank);
        fenBuilder.append("/pppppppp/8/8/8/8/PPPPPPPP/");
        fenBuilder.append(firstRank.toUpperCase());
        fenBuilder.append(" w KQkq - 0 1");
        return fenBuilder.toString();
    }

    /**
     * Returns path to Stockfish chess engine as {@link Path} object.
     *
     * @return path to Stockfish engine
     */
    public static Path getStockfishPath() {
        if (stockfishPath == null) {
            StringBuilder filename = new StringBuilder();
            filename.append("stockfish");
            filename.append(System.getProperty("sun.arch.data.model").equals("64") ? "64" : "32");
            if (System.getProperty("os.name").toLowerCase().startsWith("win"))
                filename.append(".exe");

            stockfishPath = ClasspathUtilities.currentWorkingDirectory(Utilities.class).resolve("native/stockfish").resolve(filename.toString());
        }
        return stockfishPath;
    }

    /**
     * Creates an {@link ExecutableProcess} of Stockfish chess engine. Throws {@link StockfishNotFoundException} if
     * chess engine executable was not found.
     *
     * @return an {@link ExecutableProcess} of Stockfish chess engine
     *
     * @throws StockfishNotFoundException if chess engine executable was not found
     * @see #setPosition(ExecutableProcess, String, List)
     * @see #setPosition(ExecutableProcess, String, Move...)
     * @see #setOption(ExecutableProcess, String, Object)
     * @see #setOptions(ExecutableProcess, Map)
     * @see #waitForReady(ExecutableProcess)
     * @see #getBestMove(ExecutableProcess, int)
     * @see #getPosition(ExecutableProcess)
     * @see #getAllMovesRating(ExecutableProcess, int)
     */
    public static ExecutableProcess createStockfishProcess() throws StockfishNotFoundException {
        Path stockfishPath = getStockfishPath();
        if (!Files.isRegularFile(stockfishPath))
            throw new StockfishNotFoundException();
        return new ExecutableProcess(stockfishPath, "StockfishProc" + createdStockfishProcessesCount++);
    }

    /**
     * Gets FEN of the current position set in Stockfish engine process.
     *
     * @param process the Stockfish process to be got the position from
     * @return current FEN set in the given Stockfish process
     *
     * @throws NullPointerException if {@code process} is {@code null}
     * @see #setPosition(ExecutableProcess, String, Move...)
     * @see #setPosition(ExecutableProcess, String, List)
     */
    public static String getPosition(final ExecutableProcess process) {
        Objects.requireNonNull(process, "null given instead of ExecutableProcess");
        Semaphore semaphore = new Semaphore(0);
        AtomicReference<String> currFEN = new AtomicReference<>(null);
        synchronized (process) {
            process.read(line -> {
                if (line.startsWith("Fen: ")) {
                    currFEN.set(line.substring(5));
                    semaphore.release();
                    return false;
                } else
                    return true;
            });

            process.send("d");
            semaphore.acquireUninterruptibly();
            return currFEN.get();
        }
    }

    /**
     * Sets an option of Stockfish chess engine
     *
     * @param process the Stockfish process which you want to set an option on
     * @param name    option name
     * @param value   option value
     * @throws NullPointerException if {@code process} is {@code null}
     * @see #setOptions(ExecutableProcess, Map)
     */
    public static void setOption(final ExecutableProcess process, final String name, final Object value) {
        Objects.requireNonNull(process, "null given instead of ExecutableProcess");
        Objects.requireNonNull(name, "Option name cannot be null");
        Objects.requireNonNull(value, "Option value cannot be null");

        StringBuilder command = new StringBuilder();
        command.append("setoption name ");
        command.append(name);
        command.append(" value ");
        command.append(value);

        synchronized (process) {
            process.send(command.toString());
        }
    }

    /**
     * Sets options of Stockfish chess engine from a {@link Map}. As key use option name and as value use option value.
     *
     * @param process the Stockfish process which you want to set options on
     * @param options map, where key is option name and value is option value
     * @throws NullPointerException if {@code process} is {@code null}
     * @see #setOption(ExecutableProcess, String, Object)
     */
    public static void setOptions(final ExecutableProcess process, final Map<String, Object> options) {
        Objects.requireNonNull(process, "null given instead of ExecutableProcess instance");
        Objects.requireNonNull(options, "null given instead of options");

        synchronized (process) {
            for (String k : options.keySet()) {
                Object v = options.get(k);
                setOption(process, k, v);
            }
        }
    }

    /**
     * Sets the position on a Stockfish process via initial position and done moves from that position. If no move was
     * done, pass {@code null} or an empty array. You can use a {@link List} in the {@link
     * #setPosition(ExecutableProcess, String, List)} method.
     *
     * @param process the Stockfish process which you want to set the position on
     * @param fen     the initial FEN position
     * @param moves   done moves from the initial position
     * @throws NullPointerException if {@code process} is {@code null}
     * @see #getPosition(ExecutableProcess)
     * @see #setPosition(ExecutableProcess, String, List)
     */
    public static void setPosition(final ExecutableProcess process, final String fen, final Move... moves) {
        Objects.requireNonNull(process, "null given instead of ExecutableProcess instance");
        Utilities.assertFENValidity(fen);

        StringBuilder cmd = new StringBuilder();
        cmd.append("position fen ");
        cmd.append(fen);
        if (moves != null && moves.length != 0) {
            cmd.append(" moves");
            for (Move move : moves) {
                cmd.append(' ');
                cmd.append(move);
            }
        }

        synchronized (process) {
            process.send(cmd.toString());
        }
    }

    /**
     * Works same as {@link #setPosition(ExecutableProcess, String, Move...)}, but you can pass {@link List} instead of
     * array.
     *
     * @param process the Stockfish process which you want to set the position on
     * @param fen     the initial FEN position
     * @param moves   done moves from the initial position
     * @throws NullPointerException if {@code process} is {@code null}
     * @see #getPosition(ExecutableProcess)
     * @see #setPosition(ExecutableProcess, String, Move...)
     */
    public static void setPosition(final ExecutableProcess process, final String fen, final List<Move> moves) {
        Objects.requireNonNull(process, "null given instead of ExecutableProcess instance");
        Utilities.assertFENValidity(fen);

        StringBuilder cmd = new StringBuilder();
        cmd.append("position fen ");
        cmd.append(fen);
        if (moves != null && !moves.isEmpty()) {
            cmd.append(" moves");
            for (Move move : moves) {
                cmd.append(' ');
                cmd.append(move);
            }
        }

        synchronized (process) {
            process.send(cmd.toString());
        }
    }

    /**
     * Sends a {@code go} command to the Stockfish engine. Once sent, you cannot interrupt the action. First, to set
     * position, use the {@link #setPosition(ExecutableProcess, String, Move...)} method.
     *
     * @param process the Stockfish process which you want to send {@code go} command to
     * @param depth   the depth, for more information about UCI protocol, see http://wbec-ridderkerk.nl/html/UCIProtocol.html
     * @return the best move that chess engine returned
     *
     * @throws NullPointerException if {@code process} is {@code null}
     */
    public static Move getBestMove(final ExecutableProcess process, final int depth) {
        Objects.requireNonNull(process, "null given instead of ExecutableProcess instance");

        Semaphore semaphore = new Semaphore(0);
        AtomicReference<Move> bestMove = new AtomicReference<>();

        synchronized (process) {
            process.read(line -> {
                if (line.startsWith("bestmove")) {
                    String[] splitted = line.split(" ");
                    bestMove.set(new Move(splitted[1]));
                    semaphore.release();
                    return false;
                }
                return true;
            });

            process.send("go depth " + depth);
            semaphore.acquireUninterruptibly();
            return bestMove.get();
        }
    }

    /**
     * Similar to {@link #getBestMove(ExecutableProcess, int)}, but it rates every single move, if it is good or not.
     * Returns {@link TreeMap}, where key is particular move and value is its rating by Stockfish engine. To do so, this
     * method uses <code>go perft <i>&lt;depth&gt;</i></code> UCI command.
     *
     * @param process the Stockfish process which you want to send {@code go} command to
     * @param depth   the depth, for more information about UCI protocol, see http://wbec-ridderkerk.nl/html/UCIProtocol.html
     * @return a {@link TreeMap} as described above
     *
     * @throws NullPointerException if {@code process} is {@code null}
     */
    public static TreeMap<Move, Integer> getAllMovesRating(final ExecutableProcess process, final int depth) {
        Objects.requireNonNull(process, "null given instead of ExecutableProcess instance");

        Semaphore semaphore = new Semaphore(0);
        TreeMap<Move, Integer> map = new TreeMap<>();

        synchronized (process) {
            process.read(line -> {
                if (line.isEmpty())
                    return true;
                else if (line.startsWith("Nodes searched:")) {
                    semaphore.release();
                    return false;
                }
                Matcher matcher = PATTERN_STOCKFISH_MOVE_RATING.matcher(line);
                if (matcher.matches()) {
                    Move move = new Move(matcher.group(1));
                    int rating = Integer.parseInt(matcher.group(2));
                    map.put(move, rating);
                }
                return true;
            });

            process.send("go perft " + depth);
            semaphore.acquireUninterruptibly();
            return map;
        }
    }

    /**
     * Sends the {@code isready} command and waits until chess engine sends {@code readyok} back
     *
     * @param process the Stockfish process to wait for
     * @throws NullPointerException if {@code process} is {@code null}
     */
    public static void waitForReady(final ExecutableProcess process) {
        Objects.requireNonNull(process, "null given instead of ExecutableProcess instance");

        Semaphore semaphore = new Semaphore(0);

        synchronized (process) {
            process.read(line -> {
                boolean rdy = line.equals("readyok");
                semaphore.release();
                return !rdy;
            });
            process.send("isready");

            semaphore.acquireUninterruptibly();
        }
    }
}
