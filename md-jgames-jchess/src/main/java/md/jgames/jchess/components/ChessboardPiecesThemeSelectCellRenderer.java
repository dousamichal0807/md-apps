package md.jgames.jchess.components;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import md.jgames.jchess.settings.theming.ChessboardPiecesTheme;

public class ChessboardPiecesThemeSelectCellRenderer implements ListCellRenderer<ChessboardPiecesTheme> {

	@Override
	public Component getListCellRendererComponent(JList<? extends ChessboardPiecesTheme> list,
			ChessboardPiecesTheme value, int index, boolean isSelected, boolean cellHasFocus) {
		
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		JLabel namelabel = new JLabel(value.getName());
		JLabel idlabel = new JLabel(value.getID());
		JLabel desclabel = new JLabel(value.getDescription());
		
		content.add(namelabel);
		content.add(idlabel);
		content.add(desclabel);
		
		namelabel.setFont(namelabel.getFont().deriveFont(Font.BOLD));
		idlabel.setFont(new Font("Monospace", Font.PLAIN, idlabel.getFont().getSize()));
		desclabel.setFont(desclabel.getFont().deriveFont(Font.PLAIN));
		
		if(isSelected) {
			content.setBackground(list.getSelectionBackground());
			content.setForeground(list.getSelectionForeground());
		} else {
			content.setBackground(list.getBackground());
			content.setBackground(list.getForeground());
		}
		
		return content;
	}

    
}