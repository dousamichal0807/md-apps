package md.jgames.jchess.logic;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import md.jcore.debug.Debugger;
import md.jcore.io.ExecutableProcess;

/**
 * Contains some useful static methods and constants.
 * 
 * @author Michal Douša
 * 
 * @see ExecutableProcess
 * @see Chessboard
 */
public final class Utilities {

	private static Path stockfishPath = null;
	private static int createdStockfishProcessesCount = 0;

	/**
	 * The FEN notation of the typical starting position.
	 */
	public static final String FEN_STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	/**
	 * Regex pattern for Forsyth-Edwards Notation. For testing, if FEN is valid use
	 * {@link #isValidFEN(String)} method instead, because this regex canot cover
	 * all rules of FEN notation.
	 * 
	 * @see #isValidFEN(String)
	 * @see #checkFEN(String)
	 */
	public static final Pattern PATTERN_FEN = Pattern.compile(
			"((?:[prnbqkPRNBQK1-8]+/){7}[prnbqkPRNBQK1-8]+) ([wb]) (-|KQ?k?q?|K?Qk?q?|K?Q?kq?|K?Q?k?q) (-|[a-h][36]) ([0-9])+ ([0-9])+");

	/**
	 * Stockfish regex pattern for read lines in
	 * {@link #getAllMovesRating(ExecutableProcess, int)} method.
	 * 
	 * @see #getAllMovesRating(ExecutableProcess, int)
	 */
	public static final Pattern PATTERN_STOCKFISH_MOVE_RATING = Pattern
			.compile("([a-h][1-8][a-h][1-8][qbnr]?): ([0-9]+)");

	/**
	 * Regex pattern for UCI move notation.
	 * 
	 * @see Move
	 * @see #isValidUCIMoveNotation(String)
	 */
	public static final Pattern PATTERN_UCI_MOVE = Pattern.compile("([a-h][1-8])([a-h][1-8])([qrbn])?");

	public static final Pattern PATTERN_SQUARE = Pattern.compile("[a-h][1-8]");
	/**
	 * Method testing if given input is a valid chessboard square notation.
	 * 
	 * @param square input string
	 * 
	 * @return a {@code boolean} value, if given input is a valid chessboard square
	 *         notation
	 */
	public static boolean isValidSquare(final String square) {
		return PATTERN_SQUARE.matcher(square).matches();
	}

	/**
	 * Method testing if the specified string is a notation that use chess engines,
	 * for example Stockfish to annotate a move.
	 * 
	 * @param uciNotation input to be tested
	 * 
	 * @return a {@code boolean} value, if given input is valid UCI move notation
	 */
	public static boolean isValidUCIMoveNotation(final String uciNotation) {
		return PATTERN_UCI_MOVE.matcher(uciNotation).matches();
	}

	/**
	 * Method testing if the given {@code String} is valid Forsyth-Edwards Notation
	 * of a position on the chessboard
	 * 
	 * @param fen the FEN notation
	 * 
	 * @return {@code boolean} value, if given input is valid FEN notation
	 * 
	 * @see #mapPieces(String)
	 * @see #getCurrentFEN(ExecutableProcess)
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
		 * Count of: bk - Black's king(s); bp - Black's pawns; bo - Black's pieces other
		 * than pawn and king; wk - White's king(s); wp - White's pawns; wo - White's
		 * pieces other than pawn and king
		 */
		byte bk = 0, bp = 0, bo = 0, wk = 0, wp = 0, wo = 0;

		for (String rank : ranks) {
			int f = 0;
			for (char ch : rank.toCharArray()) {
				char chlower = Character.toLowerCase(ch);
				boolean isBlackPiece = ch == chlower;
				f++;
				switch (chlower) {
				case 'k':
					if (isBlackPiece)
						bk++;
					else
						wk++;
					break;
				case 'p':
					if (isBlackPiece)
						bp++;
					else
						wp++;
					break;
				case 'q':
				case 'b':
				case 'n':
				case 'r':
					if (isBlackPiece)
						bo++;
					else
						wo++;
					break;
				default:
					f--;
					int s = (int) chlower - '0';
					if (s < 1 || s > 8)
						return false;
					f += s;
					break;
				}
			}
			if (f != 8 || bk > 1 || wk > 1 || bp > 8 || wp > 8 || bp + bo > 15 || wp + wo > 15)
				return false;
		}
		if (bk != 1 || wk != 1)
			return false;

		return true;
	}

	/**
	 * Checks, if square is valid. If not, automatically throws
	 * {@link IllegalSquareException}
	 * 
	 * @param square square notation to check
	 * 
	 * @throws IllegalSquareException when square notation is illegal
	 * 
	 * @see #checkFEN(String)
	 */
	public static void checkSquare(String square) {
		if (!isValidSquare(square))
			throw new IllegalSquareException(square);
	}

	/**
	 * Checks, if FEN notation is valid. If not, automatically throws
	 * {@link IllegalFENException}
	 * 
	 * @param fen the FEN notation to check
	 * 
	 * @throws IllegalFENException when FEN notation is illegal
	 * 
	 * @see #checkSquare(String)
	 * @see #mapPieces(String)
	 */
	public static void checkFEN(String fen) {
		if (!isValidFEN(fen))
			throw new IllegalFENException(fen);
	}

	/**
	 * Maps pieces to 8x8 2D array. Uses constants from {@link Chessboard} class.
	 * Number of rank is used as first index and number of column is used as second
	 * index.
	 * 
	 * @param fen FEN notation to map pieces from
	 * @return {@code byte[][]} as specified above
	 * 
	 * @throws IllegalFENException if invalid FEN is given
	 */
	public static byte[][] mapPieces(String fen) {
		Utilities.checkFEN(fen);
		byte[][] pieces = new byte[8][8];
		String[] ranks = fen.split("/");

		for (int rankIndex = 0; rankIndex < 8; rankIndex++) {
			String rank = ranks[7 - rankIndex];
			for (int fileIndex = 0, rankCharIndex = 0; rankCharIndex < rank.length()
					&& fileIndex < 8; rankCharIndex++) {
				char rankChar = rank.charAt(rankCharIndex);
				if (rankChar >= '1' && rankChar <= '8') {
					fileIndex += rankChar - '0';
				} else {
					pieces[rankIndex][fileIndex] = pieceCharToConstant(rankChar);
					fileIndex++;
				}
			}
		}

		return pieces;
	}

	/**
	 * Converts {@code char}s representing pieces to equivalent {@code byte}
	 * constants in {@link Chessboard} class.
	 * 
	 * @param piece the {@code char} to be converted
	 * @return equivalent {@code byte} constant from {@link Chessboard} class
	 * 
	 * @throws IllegalArgumentException if none of these {@code char}s is given as
	 *                                  argument: {@code 'p'}, {@code 'r'},
	 *                                  {@code 'n'}, {@code 'b'}, {@code 'q'},
	 *                                  {@code 'k'}, {@code 'P'}, {@code 'R'},
	 *                                  {@code 'N'}, {@code 'B'}, {@code 'Q'},
	 *                                  {@code 'K'}
	 */
	public static byte pieceCharToConstant(char piece) {
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
			for (Character firstRankPiece : firstRankPieces) firstRankBuilder.append(firstRankPiece);
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
	 * Returns path to Stockfish chess engine as {@link File} object.
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

			stockfishPath = md.jcore.Utilities.getCWD(Utilities.class).resolve("native/stockfish")
					.resolve(filename.toString());
		}
		return stockfishPath;
	}

	/**
	 * Creates an {@link ExecutableProcess} of Stockfish chess engine. Throws
	 * {@link StockfishNotFoundException} if chess engine executable was not found.
	 * 
	 * @return an {@link ExecutableProcess} of Stockfish chess engine
	 * 
	 * @throws StockfishNotFoundException if chess engine executable was not found
	 * 
	 * @see #setPosition(ExecutableProcess, String, List)
	 * @see #setPosition(ExecutableProcess, String, Move...)
	 * @see #setOption(ExecutableProcess, String, Object)
	 * @see #setOptions(ExecutableProcess, Map)
	 * @see #waitForStockfishToBeReady(ExecutableProcess)
	 * @see #getBestMove(ExecutableProcess, int)
	 * @see #getCurrentFEN(ExecutableProcess)
	 * @see #getAllMovesRating(ExecutableProcess, int)
	 */
	public static ExecutableProcess createStockfishProcess() throws StockfishNotFoundException {
		Path stockfishPath = getStockfishPath();
		if (!Files.isRegularFile(stockfishPath))
			throw new StockfishNotFoundException();
		return new ExecutableProcess(stockfishPath, "StockfishProc" + createdStockfishProcessesCount++);
	}

	public static String getCurrentFEN(final ExecutableProcess process) {
		synchronized (process) {
			AtomicReference<String> currFEN = new AtomicReference<>(null);
			process.send("d");
			process.read(line -> {
				md.jcore.debug.Debugger.info(Utilities.class, "getCurrentFEN: accepted line: '" + line + "'");
				if (line.startsWith("Fen: ")) {
					currFEN.set(line.substring(5));
					return false;
				} else
					return true;
			});
			while (currFEN.get() == null)
				;
			return currFEN.get();
		}
	}

	/**
	 * Sets an option of Stockfish chess engine
	 * 
	 * @param process the Stockfish process which you want to set an option on
	 * @param name    option name
	 * @param value   option value
	 * 
	 * @see #setOptions(ExecutableProcess, Map)
	 */
	public static void setOption(final ExecutableProcess process, final String name, final Object value) {
		synchronized (process) {
			process.send("setoption name " + name + " value " + value);
		}
	}

	/**
	 * Sets options of Stockfish chess engine from a {@link Map}. As key use option
	 * name and as value use option value.
	 * 
	 * @param process the Stockfish process which you want to set options on
	 * @param options map, where key is option name and value is option value
	 * 
	 * @see #setOption(ExecutableProcess, String, Object)
	 */
	public static void setOptions(final ExecutableProcess process, final Map<String, Object> options) {
		synchronized (process) {
			for (String k : options.keySet()) {
				Object v = options.get(k);
				setOption(process, k, v);
			}
		}
	}

	/**
	 * Sets the position on a Stockfish process via initial position and done moves
	 * from that position. If no move was done, pass {@code null} or an empty array.
	 * You can use a {@link List} in the
	 * {@link #setPosition(ExecutableProcess, String, List)} method.
	 * 
	 * @param process the Stockfish process which you want to set the position on
	 * @param fen     the initial FEN position
	 * @param moves   done moves from the initial position
	 * 
	 * @see #getCurrentFEN(ExecutableProcess)
	 * @see #setPosition(ExecutableProcess, String, List)
	 */
	public static void setPosition(final ExecutableProcess process, final String fen, final Move... moves) {
		synchronized (process) {
			Utilities.checkFEN(fen);
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
			process.send(cmd.toString());
		}
	}

	/**
	 * Works same as {@link #setPosition(ExecutableProcess, String, Move...)}, but
	 * you can pass {@link List} instead of array.
	 * 
	 * @param process the Stockfish process which you want to set the position on
	 * @param fen     the initial FEN position
	 * @param moves   done moves from the initial position
	 * 
	 * @see #getCurrentFEN(ExecutableProcess)
	 * @see #setPosition(ExecutableProcess, String, List)
	 */
	public static void setPosition(final ExecutableProcess process, final String fen, final List<Move> moves) {
		Utilities.checkFEN(fen);
		StringBuilder cmd = new StringBuilder();
		cmd.append("position fen ");
		cmd.append(fen);
		if (moves != null && !moves.isEmpty()) {
			cmd.append(" moves");
			for (Move move : moves) {
				cmd.append(' ');
				cmd.append(move.toString());
			}
		}
		process.send(cmd.toString());
	}

	/**
	 * Sends a {@code go} command to the Stockfish engine. Once sent, you cannot
	 * interrupt the action. First, to set position, use the
	 * {@link #setPosition(ExecutableProcess, String, Move...)} method.
	 * 
	 * @param process the Stockfish process which you want to send {@code go}
	 *                command to
	 * @param depth   the depth, for more information about UCI protocol, see
	 *                http://wbec-ridderkerk.nl/html/UCIProtocol.html
	 * 
	 * @return the best move that chess engine returned
	 */
	public static Move getBestMove(final ExecutableProcess process, final int depth) {
		synchronized (process) {
			AtomicReference<Move> bestMove = new AtomicReference<>();

			process.read(line -> {
				if (line.startsWith("bestmove")) {
					String[] splitted = line.split(" ");
					bestMove.set(new Move(splitted[1]));
					return false;
				}
				return true;
			});

			process.send("go depth " + depth);
			while (bestMove.get() == null) {
			}
			return bestMove.get();
		}
	}

	/**
	 * Similar to {@link #getBestMove(ExecutableProcess, int)}, but it rates every
	 * single move, if it is good or not. Returns {@link TreeMap}, where key is
	 * particular move and value is its rating by Stockfish engine. To do so, this
	 * method uses <code>go perft <i>&lt;depth&gt;</i></code> UCI command.
	 * 
	 * @param process the Stockfish process which you want to send {@code go}
	 *                command to
	 * @param depth   the depth, for more information about UCI protocol, see
	 *                http://wbec-ridderkerk.nl/html/UCIProtocol.html
	 * 
	 * @return a {@link TreeMap} as described above
	 */
	public static TreeMap<Move, Integer> getAllMovesRating(final ExecutableProcess process, final int depth) {
		synchronized (process) {
			TreeMap<Move, Integer> map = new TreeMap<>();
			AtomicReference<Boolean> done = new AtomicReference<>(false);

			process.read(line -> {
				Debugger.info(Utilities.class, "getAllMovesRating() - accepted line '" + line + "'");
				if (line.isEmpty()) {
					return true;
				} else if (line.startsWith("Nodes searched:")) {
					done.set(true);
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
			while (!done.get()) {
			}
			return map;
		}
	}

	/**
	 * Sends the {@code isready} command and waits until chess engine sends
	 * {@code readyok} back
	 * 
	 * @param process the Stockfish process to wait for
	 */
	public static void waitForStockfishToBeReady(ExecutableProcess process) {
		synchronized (process) {
			AtomicReference<Boolean> ready = new AtomicReference<>(false);
			process.read(line -> {
				boolean rdy = line.equals("readyok");
				ready.set(rdy);
				return !rdy;
			});
			process.send("isready");

			while (!ready.get()) {
			}
		}
	}
}
