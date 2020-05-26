package md.jgames.jchess.activities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import md.jcore.material.components.MaterialActivity;
import md.jcore.material.components.MaterialButton;
import md.jcore.material.components.MaterialCard;
import md.jcore.material.MaterialUtilities;
import md.jgames.jchess.Main;
import md.jgames.jchess.components.ChessboardView;
import md.jgames.jchess.logic.Chessboard;
import md.jgames.jchess.logic.ChessboardEvent;
import md.jgames.jchess.logic.ChessboardListener;
import md.jgames.jchess.logic.GamePlayChessboard;
import md.jgames.jchess.logic.Move;
import md.jgames.jchess.logic.SerializableGamePlayChessboard;
import md.jgames.jchess.logic.Utilities;

public class GamePlayActivity extends MaterialActivity
		implements ChessboardListener, WindowListener, ComponentListener {

	private static final long serialVersionUID = 1L;
	private static GamePlayActivity instance;

	public static GamePlayActivity getInstance() {
		if (instance == null)
			instance = new GamePlayActivity();
		return instance;
	}

	// Components -------------------------------------------------------------

	private final JSplitPane root;
	private final JPanel rightPanel, rightActionCard, rightInfoCard, rightMovesCard;
	private final JLabel racPrimaryText, racSecondaryText, rmcPrimaryText;
	private final JToolBar racToolbar;
	private final JButton toolbarSave, toolbarUndo, toolbarRedo, toolbarHelp, toolbarRotate;
	private final ChessboardView chessboardView;

	public GamePlayActivity() {
		chessboardView = new ChessboardView(new GamePlayChessboard());
		chessboardView.setChessboardReversed(false);
		chessboardView.setBorder(new EmptyBorder(20, 20, 20, 10));
		chessboardView.reset();

		racPrimaryText = MaterialUtilities.createHeadingTextLabel(3);
		racSecondaryText = MaterialUtilities.createHeadingSecondaryTextLabel(3);

		toolbarSave = new MaterialButton();
		toolbarSave.addActionListener(event -> {
			try {
				save();
				saved = true;
			} catch (IOException exc) {
				StringBuilder message = new StringBuilder();
				message.append("Errors during saving the file:\n\n");
				message.append(exc.getClass().getName());
				message.append("\n\n");
				message.append(exc.getLocalizedMessage());
				JOptionPane.showMessageDialog(this, message.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			refresh();
		});

		toolbarUndo = new MaterialButton();
		toolbarUndo.addActionListener(event -> {
			getChessboard().undo();
			chessboardView.repaint();
		});

		toolbarRedo = new MaterialButton();
		toolbarRedo.addActionListener(event -> {
			getChessboard().redo();
			chessboardView.repaint();
		});

		toolbarHelp = new MaterialButton();
		toolbarHelp.addActionListener((ActionEvent evt) -> {
			Thread thread = new Thread(() -> {
				setToolbarEnabled(false);
				Utilities.setPosition(Main.getMainStockfishProcess(), getChessboard().getCurrentFEN());
				Move move = Utilities.getBestMove(Main.getMainStockfishProcess(), 20);
				JOptionPane.showMessageDialog(null, move);
				setToolbarEnabled(true);
			});
			thread.start();
		});

		toolbarRotate = new MaterialButton();
		toolbarRotate.addActionListener((ActionEvent evt) -> {
			chessboardView.flipChessboard();
			chessboardView.repaint();
		});

		racToolbar = new JToolBar();
		racToolbar.setFloatable(false);
		racToolbar.setAlignmentX(0);
		racToolbar.add(toolbarSave);
		racToolbar.addSeparator();
		racToolbar.add(toolbarUndo);
		racToolbar.add(toolbarRedo);
		racToolbar.addSeparator();
		racToolbar.add(toolbarHelp);
		racToolbar.addSeparator();
		racToolbar.add(toolbarRotate);

		rightActionCard = new MaterialCard();
		rightActionCard.setAlignmentX(0f);
		rightActionCard.setLayout(new BoxLayout(rightActionCard, BoxLayout.PAGE_AXIS));
		rightActionCard.add(racPrimaryText);
		rightActionCard.add(racSecondaryText);
		rightActionCard.add(racToolbar);

		rightInfoCard = new MaterialCard();
		rightInfoCard.setAlignmentX(0f);
		rightInfoCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

		rmcPrimaryText = MaterialUtilities.createHeadingTextLabel(3);
		rmcPrimaryText.setText("Moves");
		rmcPrimaryText.setAlignmentX(0f);

		rightMovesCard = new MaterialCard();
		rightMovesCard.setAlignmentX(0f);
		rightMovesCard.setLayout(new BoxLayout(rightMovesCard, BoxLayout.PAGE_AXIS));
		rightMovesCard.add(rmcPrimaryText);
		rightMovesCard.add(new JSeparator(SwingConstants.HORIZONTAL));

		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		rightPanel.setBorder(new EmptyBorder(20, 10, 20, 20));
		rightPanel.add(rightActionCard);
		rightPanel.add(rightInfoCard);
		rightPanel.add(rightMovesCard);

		// pouze test metody markSquare:
		/*
		 * chessboardView.markSquare("a4", new Color(144, 128, 112, 192));
		 * chessboardView.markSquare("b4", new Color(64, 192, 0, 192));
		 * chessboardView.markSquare("c4", new Color(112, 255, 48, 192));
		 * chessboardView.markSquare("d4", new Color(224, 255, 128, 192));
		 * chessboardView.markSquare("e4", new Color(255, 192, 64, 192));
		 * chessboardView.markSquare("f4", new Color(255, 96, 0, 192));
		 * chessboardView.markSquare("g4", new Color(192, 0, 0, 192));
		 * chessboardView.markSquare("h4", new Color(192, 64, 64, 192));
		 * 
		 * chessboardView.markSquare("a5", new Color(144, 128, 112, 192));
		 * chessboardView.markSquare("b5", new Color(64, 192, 0, 192));
		 * chessboardView.markSquare("c5", new Color(112, 255, 48, 192));
		 * chessboardView.markSquare("d5", new Color(224, 255, 128, 192));
		 * chessboardView.markSquare("e5", new Color(255, 192, 64, 192));
		 * chessboardView.markSquare("f5", new Color(255, 96, 0, 192));
		 * chessboardView.markSquare("g5", new Color(192, 0, 0, 192));
		 * chessboardView.markSquare("h5", new Color(192, 64, 64, 192));
		 */

		root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, chessboardView, rightPanel);
		root.setDividerLocation(600);
		root.setResizeWeight(0.5);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(root, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(this);
		this.addComponentListener(this);
		this.saved = true;
		/*
		 * this.setSize(900, 650); this.setMinimumSize(this.getSize());
		 * this.setLocationRelativeTo(null);
		 */
	}

	// Chessboard saving ------------------------------------------------------

	private File file;
	private boolean saved;

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	private void save(File file) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(new SerializableGamePlayChessboard((GamePlayChessboard) chessboardView.getChessboard()));
		oos.close();
	}

	private void save() throws IOException {
		if (file == null) {
			file = saveAs();
		} else {
			save(file);
		}
	}

	private File saveAs() throws IOException {
		int opt = SavedGameFileChooser.getInstance().showSaveDialog(this);
		File f = SavedGameFileChooser.getInstance().getSelectedFile();
		if (opt == JFileChooser.APPROVE_OPTION) {
			save(f);
			return f;
		}
		return null;
	}

	// Manipulation with components -------------------------------------------

	private void setToolbarEnabled(boolean e) {
		for (int i = 0; i < racToolbar.getComponentCount(); i++) {
			Component comp = racToolbar.getComponent(i);
			comp.setEnabled(e);
		}
	}

	public void refresh() {
		racPrimaryText.setText(file == null ? "New Game" : file.getName());
		racPrimaryText.setMinimumSize(new Dimension(0, 0));

		racSecondaryText.setText(file == null ? null : file.getAbsolutePath());
		racSecondaryText.setMinimumSize(new Dimension(0, 0));

		rightActionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, rightActionCard.getPreferredSize().height));
	}

	// Chessboard manipulation ------------------------------------------------

	public GamePlayChessboard getChessboard() {
		return (GamePlayChessboard) chessboardView.getChessboard();
	}

	public void setChessboard(GamePlayChessboard newChessboard, boolean disposePrevous) {
		Chessboard previousChessboard = chessboardView.getChessboard();
		if (previousChessboard != null && disposePrevous)
			previousChessboard.dispose();
		chessboardView.setChessboard(newChessboard);
	}

	// ChessboardListener -----------------------------------------------------

	@Override
	public void moveDone(ChessboardEvent evt) {
		saved = false;
		refresh();
	}

	@Override
	public void moveUndone(ChessboardEvent evt) {
		saved = false;
		refresh();
	}

	@Override
	public void moveRedone(ChessboardEvent evt) {
		saved = false;
		refresh();
	}

	// WindowListener ---------------------------------------------------------

	@Override
	public void windowOpened(WindowEvent e) {
		refresh();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		boolean close = saved;
		if (!saved) {
			int opt = JOptionPane.showConfirmDialog(this, "You have not saved all changes. Close anyway?",
					"Unsaved Changes", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			close = opt == JOptionPane.YES_OPTION;
		}
		if (close) {
			this.getChessboard().dispose();
			System.exit(0);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		refresh();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}