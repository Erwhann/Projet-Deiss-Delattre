package vue.panels;

import gestion.GestionVehicule;
import modele.Vehicule;
import vue.composants.HeaderPanel;
import vue.composants.NavigationListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RechercheVehiculePanel extends JPanel {

    private final GestionVehicule gestionVehicule;
    private final NavigationListener navListener;
    private JTextField immatField;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Vehicule> vehiculesActuels;
    private Timer searchTimer;

    public RechercheVehiculePanel(NavigationListener navListener) {
        // ✅ FIX : On n'utilise plus 'emf', GestionVehicule utilise DbUtils en interne
        this.gestionVehicule = new GestionVehicule();
        this.navListener = navListener;
        setLayout(new BorderLayout());

        // Un seul HeaderPanel suffit
        add(new HeaderPanel("Recherche de Véhicule"), BorderLayout.NORTH);

        // --- Barre de recherche ---
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        immatField = new JTextField(20);
        immatField.setPreferredSize(new Dimension(300, 35)); // Correction dimension
        searchBar.add(new JLabel("Immatriculation / VIN :"));
        searchBar.add(immatField);

        // Conteneur pour empiler Header + Barre de recherche
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(new HeaderPanel("Recherche de Véhicule"), BorderLayout.NORTH);
        northPanel.add(searchBar, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] colonnes = {"Immat", "Marque", "Modèle", "Propriétaire"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- ÉCOUTEUR : Double-clic pour naviguer ---
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1 && vehiculesActuels != null) {
                        // ACTION : Navigation vers le détail du véhicule
                        navListener.afficherDetailsVehicule(vehiculesActuels.get(row));
                    }
                }
            }
        });

        // --- Recherche dynamique ---
        immatField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { planifier(); }
            public void removeUpdate(DocumentEvent e) { planifier(); }
            public void changedUpdate(DocumentEvent e) { planifier(); }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void planifier() {
        if (searchTimer != null && searchTimer.isRunning()) searchTimer.stop();
        searchTimer = new Timer(400, e -> executer());
        searchTimer.setRepeats(false);
        searchTimer.start();
    }

    private void executer() {
        String txt = immatField.getText().trim();
        if (txt.length() < 2) {
            tableModel.setRowCount(0);
            return;
        }

        // ✅ Utilise la méthode de recherche corrigée dans GestionVehicule
        vehiculesActuels = gestionVehicule.chercherParImmatriculation(txt);
        tableModel.setRowCount(0);

        if (vehiculesActuels != null) {
            for (Vehicule v : vehiculesActuels) {
                tableModel.addRow(new Object[]{
                        v.getImmatriculation(),
                        v.getType().getMarque(),
                        v.getType().getModele(),
                        v.getProprietaire() != null ? v.getProprietaire().getNom().toUpperCase() : "N/C"
                });
            }
        }
    }
}