package md.jgames.jchess.swingcomponents;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import md.jgames.jchess.logic.GamePlayChessboard;
import md.jgames.jchess.logic.Utilities;

public class ChessboardSetupSelect extends JPanel {
    private static final long serialVersionUID = 1L;

    private final ChessboardView chessboardView;
    private final JPanel posSelectContainer;

    private final ButtonGroup radioGroup;
    private final JRadioButton radioStandardPosition, radioChess960, radioCustomFEN;

    private final JSeparator separator;

    private final JButton chess960OtherPositionBtn;
    private final JTextField fenTextField;
    private final JLabel invalidFENLabel;

    public ChessboardSetupSelect() {
        chessboardView = new ChessboardView(new GamePlayChessboard());
        chessboardView.setEnabled(false);
        chessboardView.setBorder(new EmptyBorder(10, 10, 10, 10));

        ItemListener updateChangeListener = event -> update();
        ActionListener updateActionListener = event -> update();

        radioGroup = new ButtonGroup();
        posSelectContainer = new JPanel();
        posSelectContainer.setLayout(new BoxLayout(posSelectContainer, BoxLayout.PAGE_AXIS));
        posSelectContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        radioStandardPosition = new JRadioButton("Standard initial position");
        radioStandardPosition.addItemListener(updateChangeListener);
        radioGroup.add(radioStandardPosition);
        posSelectContainer.add(radioStandardPosition);

        radioChess960 = new JRadioButton("Random Chess960 position");
        radioChess960.addItemListener(updateChangeListener);
        radioGroup.add(radioChess960);
        posSelectContainer.add(radioChess960);

        radioCustomFEN = new JRadioButton("Enter custom FEN code");
        radioCustomFEN.addItemListener(updateChangeListener);
        radioGroup.add(radioCustomFEN);
        posSelectContainer.add(radioCustomFEN);

        separator = new JSeparator(JSeparator.HORIZONTAL);
        posSelectContainer.add(separator);

        chess960OtherPositionBtn = new JButton("Generate");
        chess960OtherPositionBtn.addActionListener(updateActionListener);
        posSelectContainer.add(chess960OtherPositionBtn);

        fenTextField = new JTextField(Utilities.FEN_STARTING_POSITION);
        fenTextField.setAlignmentX(0f);
        fenTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fenTextField.getPreferredSize().height));
        fenTextField.addKeyListener(new KeyListener() {
            public void keyPressed(final KeyEvent arg0) {
                update();
            }

            public void keyReleased(final KeyEvent arg0) {
                update();
            }

            public void keyTyped(final KeyEvent arg0) {
                update();
            }
        });
        posSelectContainer.add(fenTextField);

        posSelectContainer.add(Box.createVerticalGlue());

        invalidFENLabel = new JLabel("<html><p>You have entered invalid FEN. The standard position will be used instead.</p></html>");
        invalidFENLabel.setIcon((Icon) UIManager.get("OptionPane.errorIcon"));
        posSelectContainer.add(invalidFENLabel);

        this.setLayout(new GridLayout(1, 2));
        this.add(chessboardView);
        this.add(posSelectContainer);

        radioStandardPosition.setSelected(true);
        update();
    }

    private void update() {
        chess960OtherPositionBtn.setVisible(radioChess960.isSelected());
        fenTextField.setVisible(radioCustomFEN.isSelected());
        invalidFENLabel.setVisible(false);

        if (radioStandardPosition.isSelected())
            chessboardView.getChessboard().reset();
        else if (radioChess960.isSelected())
            chessboardView.getChessboard().reset(Utilities.generateChess960FEN());
        else if (radioCustomFEN.isSelected()) try {
            chessboardView.getChessboard().reset(fenTextField.getText().trim());
        } catch (IllegalArgumentException exc) {
            chessboardView.getChessboard().reset();
            invalidFENLabel.setVisible(true);
        }

        posSelectContainer.revalidate();
        chessboardView.repaint();
    }

    public GamePlayChessboard getChessboard() {
        return (GamePlayChessboard) chessboardView.getChessboard();
    }

    public void reset() {
        radioStandardPosition.setSelected(true);
        update();
    }
}
