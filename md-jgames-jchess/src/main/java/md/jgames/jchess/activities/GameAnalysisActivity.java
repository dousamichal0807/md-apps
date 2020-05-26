package md.jgames.jchess.activities;

import md.jcore.material.components.MaterialActivity;
import md.jgames.jchess.components.ChessboardView;
import md.jgames.jchess.logic.AnalysisChessboard;

import java.awt.*;

public class GameAnalysisActivity extends MaterialActivity {
	private static final long serialVersionUID = 1L;

	private static GameAnalysisActivity instance;

	/**
	 * Returns the first and the last instance of this activity ever created.
	 * @return the only instance of {@link GameAnalysisActivity} class
	 */
	public static GameAnalysisActivity getInstance() {
		if(instance == null)
			instance = new GameAnalysisActivity();
		return instance;
	}

	private final ChessboardView chessboardView;

	// TODO ctor
	private GameAnalysisActivity() {
		chessboardView = new ChessboardView(new AnalysisChessboard());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(chessboardView, BorderLayout.CENTER);
	}
}
