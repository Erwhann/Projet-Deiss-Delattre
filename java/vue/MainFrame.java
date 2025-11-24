package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Auto2I - Gestion du garage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Menu principal
        JMenuBar menuBar = new JMenuBar();
        JMenu menuVehicule = new JMenu("Véhicules");
        JMenuItem ajouterVehicule = new JMenuItem("Ajouter un véhicule");
        menuVehicule.add(ajouterVehicule);
        menuBar.add(menuVehicule);
        setJMenuBar(menuBar);

        // Zone centrale
        add(new JLabel("Bienvenue dans Auto2I", SwingConstants.CENTER), BorderLayout.CENTER);

        setVisible(true);

        ajouterVehicule.addActionListener(e -> {
            getContentPane().removeAll();
            add(new AjoutVehiculePanel(), BorderLayout.CENTER);
            revalidate();
            repaint();
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }


}
