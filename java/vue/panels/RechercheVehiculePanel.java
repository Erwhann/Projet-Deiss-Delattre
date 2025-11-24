package vue;

import vue.panels.AjoutVehiculePanel;
import vue.panels.RechercheVehiculePanel;
import vue.panels.DashboardPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Auto2I - Gestion du garage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ------------------------------------------------------
        // BARRE DE MENU
        // ------------------------------------------------------
        JMenuBar menuBar = new JMenuBar();

        // MENU VEHICULES
        JMenu menuVehicule = new JMenu("Véhicules");
        JMenuItem itemAjout = new JMenuItem("Ajouter un véhicule");
        JMenuItem itemRecherche = new JMenuItem("Rechercher un véhicule");

        menuVehicule.add(itemAjout);
        menuVehicule.add(itemRecherche);
        menuBar.add(menuVehicule);

        // MENU ACCUEIL / TABLEAU DE BORD
        JMenu menuAccueil = new JMenu("Accueil");
        JMenuItem itemDashboard = new JMenuItem("Dashboard");
        menuAccueil.add(itemDashboard);
        menuBar.add(menuAccueil);

        setJMenuBar(menuBar);

        // ------------------------------------------------------
        // CONTENU PAR DÉFAUT
        // ------------------------------------------------------
        add(new DashboardPanel(), BorderLayout.CENTER);

        // ------------------------------------------------------
        // ACTION : Ajouter un véhicule
        // ------------------------------------------------------
        itemAjout.addActionListener(e -> {
            getContentPane().removeAll();
            add(new AjoutVehiculePanel(), BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        // ------------------------------------------------------
        // ACTION : Recherche + Historique
        // ------------------------------------------------------
        itemRecherche.addActionListener(e -> {
            getContentPane().removeAll();
            add(new RechercheVehiculePanel(), BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        // ------------------------------------------------------
        // ACTION : Dashboard
        // ------------------------------------------------------
        itemDashboard.addActionListener(e -> {
            getContentPane().removeAll();
            add(new DashboardPanel(), BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        setVisible(true);
    }
}
