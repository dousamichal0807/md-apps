package md.jgames.jchess.activities;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import md.jcore.material.components.MaterialActivity;
import md.jcore.material.components.MaterialButton;
import md.jcore.material.MaterialConstants;
import md.jgames.jchess.Main;
import md.jgames.jchess.components.ChessboardSetupSelect;
import md.jgames.jchess.logic.GamePlayChessboard;

public class NewGameActivity extends MaterialActivity {
	private static final long serialVersionUID = 1L;
	
	private static NewGameActivity instance;
	public static NewGameActivity getInstance() {
		if(instance == null) {
			instance = new NewGameActivity();
		}
		return instance;
	}

	private GamePlayChessboard chessboard;
	
	// Board Setup
	private final JButton backButton;
	private final JButton nextButton;
	private final JPanel buttonPane;
	private final JPanel playerSetupContainer;
	private final JPanel mainContent;
	private final ChessboardSetupSelect chessboardSetupSelect;
	private final CardLayout mainContentLayout;
	private int mainContentCardIndex;
	
	private NewGameActivity() {
		md.jcore.debug.Debugger.info(getClass(), "Creating activity...");

		chessboardSetupSelect = new ChessboardSetupSelect();
		playerSetupContainer = new JPanel(new GridBagLayout());
		
		mainContentLayout = new CardLayout();
		mainContentCardIndex = 0;
		mainContent = new JPanel(mainContentLayout);
		mainContent.add(chessboardSetupSelect, "0");
		mainContent.add(playerSetupContainer, "1");
		
		buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.setBorder(new EmptyBorder(10, 20, 20, 20));

		nextButton = new MaterialButton("Next", true, MaterialConstants.ELEVATION_ELEVATED);
		nextButton.addActionListener(event -> {
			if(mainContentCardIndex < mainContent.getComponentCount() - 1) {
				if(mainContentCardIndex == mainContent.getComponentCount() - 2)
					nextButton.setText("Finish");
				mainContentCardIndex++;
				mainContentLayout.show(mainContent, Integer.toString(mainContentCardIndex));
			}
			else {
				GamePlayActivity.getInstance().setChessboard(chessboardSetupSelect.getChessboard(), true);
				Main.getMainWindow().switchActivity(GamePlayActivity.getInstance());
			}
		});
		
		backButton = new MaterialButton("Back", false, MaterialConstants.ELEVATION_ELEVATED);
		backButton.addActionListener(event -> {
			if (mainContentCardIndex > 0) {
				mainContentCardIndex--;
				mainContentLayout.show(mainContent, Integer.toString(mainContentCardIndex));
				nextButton.setText("Next");
			}
			else
				Main.getMainWindow().switchActivity(MainActivity.getInstance());
		});
		
		buttonPane.add(backButton);
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(nextButton);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainContent, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		md.jcore.debug.Debugger.info(getClass(), "Activity created");
	}

	public GamePlayChessboard getChessboard() {
		return chessboard;
	}
	
	public void reset() {
		mainContentCardIndex = 0;
		mainContentLayout.show(mainContent, "0");
		nextButton.setText("Next");
		chessboardSetupSelect.reset();
	}
}
