package md.jgames.jchess.logic;

public class StockfishNotFoundException extends IllegalStateException {
	private static final long serialVersionUID = 0x0100L;

	public StockfishNotFoundException() {
		super("Stockfish executable was not found. Expected path: " + Utilities.getStockfishPath());
	}
}
