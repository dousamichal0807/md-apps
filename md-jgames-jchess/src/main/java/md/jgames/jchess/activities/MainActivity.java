package md.jgames.jchess.activities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import md.jcore.material.components.MaterialActivity;
import md.jgames.jchess.Main;
import md.jgames.jchess.logic.GamePlayChessboard;
import md.jgames.jchess.logic.SerializableGamePlayChessboard;
import md.jgames.jchess.resources.Resources;

public final class MainActivity extends MaterialActivity {
	private static final long serialVersionUID = 1L;

	private static MainActivity instance;

	public static MainActivity getInstance() {
		if (instance == null)
			instance = new MainActivity();
		return instance;
	}

	private final JLabel iconLabel;
	private final JToolBar buttonContainer;
	private final JButton newGameButton, openGameButton, analyzeGameButton, showStatisticsButton, settingsButton,
			aboutButton, helpButton;

	private MainActivity() {

		getContentPane().setLayout(new GridBagLayout());
		getContentPane().setBackground(Resources.COLOR_PRIMARY);
		getContentPane().setForeground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = 0;

		iconLabel = new JLabel();
		iconLabel.setIcon(Resources.loadIconResource("jchess-logo-256px.png"));
		iconLabel.setHorizontalTextPosition(JLabel.CENTER);
		iconLabel.setVerticalTextPosition(JLabel.BOTTOM);
		iconLabel.setAlignmentX(.5f);
		getContentPane().add(iconLabel, gbc);

		buttonContainer = new JToolBar();
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.PAGE_AXIS));
		buttonContainer.setFloatable(false);

		buttonContainer.add(Box.createGlue());

		newGameButton = new JButton("New game");
		newGameButton.addActionListener(event -> {
			NewGameActivity.getInstance().reset();
			Main.getMainWindow().switchActivity(NewGameActivity.getInstance());
		});
		buttonContainer.add(newGameButton);

		openGameButton = new JButton("Play an existing game");
		openGameButton.addActionListener(event -> {
			int opt = SavedGameFileChooser.getInstance().showOpenDialog(this);
			if (opt == JFileChooser.APPROVE_OPTION)
				try {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(SavedGameFileChooser.getInstance().getSelectedFile()));
					GamePlayChessboard ch = new GamePlayChessboard((SerializableGamePlayChessboard) ois.readObject());
					ois.close();
					GamePlayActivity.getInstance().setChessboard(ch, true);
					Main.getMainWindow().switchActivity(GamePlayActivity.getInstance());
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(this,
							"Error during reading the file:\n\n" + exc.getClass().getName() + "\n" + exc.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
					exc.printStackTrace();
				}
		});
		buttonContainer.add(openGameButton);

		analyzeGameButton = new JButton("Analysis");
		buttonContainer.add(analyzeGameButton);

		showStatisticsButton = new JButton("Statistics");
		buttonContainer.add(showStatisticsButton);

		settingsButton = new JButton("Settings");
		settingsButton.addActionListener(event -> {
			// TODO settings
		});
		buttonContainer.add(settingsButton);

		aboutButton = new JButton("About");
		aboutButton.addActionListener(event -> new AboutDialog(Main.getMainWindow()).setVisible(true));
		buttonContainer.add(aboutButton);

		helpButton = new JButton("Help");
		buttonContainer.add(helpButton);

		buttonContainer.add(Box.createGlue());

		buttonPaneInit(newGameButton);
		buttonPaneInit(openGameButton);
		buttonPaneInit(analyzeGameButton);
		buttonPaneInit(showStatisticsButton);
		buttonPaneInit(settingsButton);
		buttonPaneInit(aboutButton);
		buttonPaneInit(helpButton);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 1;
		gbc.gridy = 0;

		getContentPane().add(buttonContainer, gbc);
	}

	private static void buttonPaneInit(JComponent b) {
		b.setAlignmentX(0f);
		b.setMaximumSize(new Dimension(300, b.getPreferredSize().height));
	}
}
