package milan.liebsch.mycomponents;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class TitleLabel extends JLabel {
	
	// what is this ? find out !
	private static final long serialVersionUID = 1L;
	
	public TitleLabel(String title) {
		Font font = new Font(Font.SERIF, Font.BOLD, 32);
		setFont(font);
		setBackground(Color.black);
		setForeground(Color.white);
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setText(title);
	}

}