package presentation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class ModernButton extends JButton {
    
    private static final long serialVersionUID = 1L;

	public ModernButton(String text) {
        super(text);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setBackground(new Color(52, 152, 219)); 
        setFont(new Font("SansSerif", Font.BOLD, 22));
        setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
