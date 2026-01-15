package vue.composants;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private final JButton backButton;
    private final JLabel titleLabel;

    public HeaderPanel(String titre) {
        this(titre, null);
    }

    public HeaderPanel(String titre, Runnable onBack) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 60));
        setBackground(new Color(33, 47, 61));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        titleLabel = new JLabel(titre);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        backButton = new JButton("← Retour");
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(true);
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(52, 73, 94));
        backButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (onBack != null) {
            backButton.addActionListener(e -> onBack.run());
            backButton.setVisible(true);
        } else {
            backButton.setVisible(false);
        }

        add(titleLabel, BorderLayout.WEST);
        add(backButton, BorderLayout.EAST);
    }

    public void setBackVisible(boolean visible) {
        backButton.setVisible(visible);
    }

    public void setBackEnabled(boolean enabled) {
        backButton.setEnabled(enabled);
    }

    public void setTitle(String titre) {
        titleLabel.setText(titre);
    }
}
