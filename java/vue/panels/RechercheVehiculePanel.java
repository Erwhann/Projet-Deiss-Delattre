package vue.panels;

import gestion.GestionVehicule;
import gestion.GestionIntervention;
import modele.Intervention;
import modele.Vehicule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RechercheVehiculePanel extends JPanel {

    private JTextField immatField;
    private JLabel infoVehicule;

    private JTable table;
    private DefaultTableModel tableModel;

    private GestionVehicule gestionVehicule = new GestionVehicule();
    private GestionIntervention gestionIntervention = new GestionIntervention();

    public RechercheVehiculePanel() {

        setLayout(new BorderLayout(15, 15));

        // ---------------------------
        // BARRE DE RECHERCHE
        // ---------------------------
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        immatField = new JTextField(12);
        JButton rechercher = new JButton("Rechercher");
        infoVehicule = new JLabel(" ");

        top.add(new JLabel("Immatriculation : "));
        top.add(immatField);
        top.add(rechercher);

        add(top, BorderLayout.NORTH);
        add(infoVehicule, BorderLayout.SOUTH);

        // ---------------------------
        // TABLEAU (historique)
        // ---------------------------
        tableModel = new DefaultTableModel(
                new Object[]{"Date", "Km", "Type", "Prix", "Durée", "Description"},
                0
        );
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Action du bouton
        rechercher.addActionListener(e -> rechercherVehicule());
    }

    private void rechercherVehicule() {
        String immat = immatField.getText().trim().toUpperCase();

        Vehicule v = gestionVehicule.rechercherVehiculeParImmat(immat);

        if (v == null) {
            infoVehicule.setText("❌ Aucun véhicule trouvé.");
            tableModel.setRowCount(0);
            return;
        }

        infoVehicule.setText(
                "<html><b>"
                        + v.getTypeVehicule().getMarque() + " "
                        + v.getTypeVehicule().getModele()
                        + "</b> — " + v.getKilometrageActuel() + " km</html>"
        );

        // HISTORIQUE DES INTERVENTIONS
        List<Intervention> interventions =
                gestionIntervention.getInterventionsByVehicule(v);

        tableModel.setRowCount(0); // reset table

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Intervention i : interventions) {
            tableModel.addRow(new Object[]{
                    df.format(i.getDate()),
                    i.getKilometrage(),
                    i.getTypeIntervention().getNom(),
                    i.getPrixTotal(),
                    i.getDureeReel(),
                    i.getDescription()
            });
        }
    }
}
