package vue.composants;

import modele.Vehicule;
import vue.composants.NavigationListener;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.function.Consumer;

public class NavBar extends JPanel {

    private final Consumer<String> onNavigation;
    private final NavigationListener navListener;
    private final JPanel historyPanel;

    public NavBar(Consumer<String> onNavigation, NavigationListener navListener) {
        this.onNavigation = onNavigation;
        this.navListener = navListener;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(240, 0));
        setBackground(new Color(33, 47, 61)); // Fond sombre identique au header
        setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // --- ACTIONS PRINCIPALES ---
        JPanel mainActions = new JPanel(new GridLayout(0, 1, 8, 8));
        mainActions.setOpaque(false);
        mainActions.add(createButton("📊 Dashboard", () -> onNavigation.accept("dashboard")));
        mainActions.add(createButton("🚗 Ajouter véhicule", () -> onNavigation.accept("ajoutVehicule")));
        mainActions.add(createButton("🔍 Rechercher véhicule", () -> onNavigation.accept("rechercheVehicule")));
        mainActions.add(createButton("🛠️ Nouvelle Intervention", () -> onNavigation.accept("ajoutIntervention")));

        // --- HISTORIQUE ---
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setOpaque(false);

        JLabel lblHist = new JLabel("VÉHICULES RÉCENTS");
        lblHist.setForeground(new Color(149, 165, 166));
        lblHist.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblHist.setBorder(BorderFactory.createEmptyBorder(25, 5, 10, 5));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(lblHist, BorderLayout.NORTH);
        centerContainer.add(historyPanel, BorderLayout.CENTER);

        add(mainActions, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);
    }

    public void rafraichirHistorique(Collection<Vehicule> vehicules) {
        historyPanel.removeAll();
        for (Vehicule v : vehicules) {
            // ✅ Pas d'argument supplémentaire, juste le texte
            CustomButton btnVehicule = new CustomButton(v.getImmatriculation());

            // ✅ L'action de navigation
            btnVehicule.addActionListener(e -> navListener.afficherDetailsVehicule(v));

            historyPanel.add(btnVehicule);
            historyPanel.add(Box.createVerticalStrut(5));
        }
        historyPanel.revalidate();
        historyPanel.repaint();
    }
    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void styleHistoryButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // ✅ FIX BLANC SUR BLANC : On force le texte blanc sur fond bleu/gris sombre
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(41, 128, 185)); // Bleu pro pour l'historique

        // ✅ Important pour éviter le rendu système blanc
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet de survol pour la validation visuelle
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(41, 128, 185));
            }
        });
    }
}