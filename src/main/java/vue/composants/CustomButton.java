package vue.composants;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public CustomButton(String texte) {
        super(texte);

        // ✅ C'est CA qui empêche le blanc forcé par Windows/Mac
        setContentAreaFilled(false);
        setOpaque(true);

        setBackground(new Color(41, 128, 185)); // Bleu
        setForeground(Color.WHITE);             // Texte Blanc
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // ✅ On force le dessin du rectangle bleu avant tout
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}