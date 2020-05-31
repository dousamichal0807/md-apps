package md.jgames.jchess;

import javax.swing.JOptionPane;

import md.jcore.io.ExecutableProcess;
import md.jcore.material.MaterialConstants;
import md.jcore.material.MaterialUtilities;
import md.jcore.material.MaterialWindow;
import md.jcore.material.activities.SplashScreenActivity;
import md.jgames.jchess.activities.MainActivity;
import md.jgames.jchess.logic.OpeningDatabase;
import md.jgames.jchess.logic.Utilities;
import md.jgames.jchess.resources.Resources;

/**
 * Entry point for the jChess application.
 * 
 * @author Michal Douša
 */
public final class Main {

	private Main() {
	}

	private static ExecutableProcess mainStockfishProcess;
	private static MaterialWindow mainWindow;
	
	public static ExecutableProcess getMainStockfishProcess() {
		if(mainStockfishProcess == null)
			mainStockfishProcess = new ExecutableProcess(Utilities.getStockfishPath(), "MainStockfish");
		return mainStockfishProcess;
	}
	
	public static MaterialWindow getMainWindow() {
		if(mainWindow == null) {
			mainWindow = new MaterialWindow();
			mainWindow.setTitle("MD jChess");
			mainWindow.setIconImage(Resources.loadImageResource("jchess-logo-256px.png"));
		}
		return mainWindow;
	}

	public static void main(final String[] args) {
		
		SplashScreenActivity splashScreen = null;
		
		try {
			MaterialUtilities.initializeLookAndFeel(MaterialConstants.THEME_LIGHT, Resources.COLOR_PRIMARY, Resources.COLOR_SECONDARY);
			
			// Construct SplashScreen -----------------------------------------
			
			splashScreen = new SplashScreenActivity();
			splashScreen.setIcon(Resources.loadIconResource("jchess-logo-256px.png"));
			splashScreen.setProgressBarMaximum(4);
			splashScreen.setLabelText(" ");
			
			Main.getMainWindow().switchActivity(splashScreen);
			Main.getMainWindow().setVisible(true);
			
			// Initialize Stockfish -------------------------------------------
			
			splashScreen.setLabelText("Please wait for Stockfish chess engine to be ready...");
			splashScreen.setProgressBarValue(1);
			 
			getMainStockfishProcess().start();
			md.jgames.jchess.logic.Utilities.waitForStockfishToBeReady(getMainStockfishProcess());
			
			// Load all openings ----------------------------------------------
			
			splashScreen.setLabelText("Loading chess openings database...");
			splashScreen.setProgressBarValue(2);
			
			OpeningDatabase.loadTSV(Resources.loadResource("db-openings.tsv"));
			
			// Everyting done -------------------------------------------------
			
			Main.getMainWindow().switchActivity(MainActivity.getInstance());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(splashScreen,
					"Initialization of application failed:\n\n" + e.getClass().getName() + "\n" + e.getMessage(),
					"MD jChess Init Error", JOptionPane.ERROR_MESSAGE);
			
			getMainWindow().setVisible(false);
			getMainWindow().dispose();
			System.exit(0);
		}
	}

}