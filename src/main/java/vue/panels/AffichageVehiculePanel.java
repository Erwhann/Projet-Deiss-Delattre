package vue.panels;

import config.DbUtils;
import jakarta.persistence.EntityManager;
import modele.Intervention;
import modele.Vehicule;
import vue.composants.CustomButton;
import vue.composants.HeaderPanel;
import vue.composants.NavigationListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AffichageVehiculePanel extends JPanel {

    private Vehicule vehicule;
    private final NavigationListener navListener;

    public AffichageVehiculePanel(Vehicule v, NavigationListener navListener) {
        // ✅ STRATÉGIE : Recharger le véhicule depuis la DB pour avoir les dernières interventions
        this.vehicule = rechargerVehicule(v);
        this.navListener = navListener;

        setLayout(new BorderLayout());

        // --- EN-TÊTE ---
        add(new HeaderPanel("DOSSIER TECHNIQUE : " + vehicule.getImmatriculation()), BorderLayout.NORTH);

        // --- CONTENEUR PRINCIPAL ---
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 0);

        // 1. BLOC INFOS PROPRIÉTAIRE (Top)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        mainContent.add(createClientCard(vehicule), gbc);

        // 2. BLOC CARACTÉRISTIQUES TECHNIQUES (Middle)
        gbc.gridy = 1;
        mainContent.add(createTechnicalGrid(vehicule), gbc);

        // 3. TABLEAU DES INTERVENTIONS (Bottom)
        gbc.gridy = 2; gbc.weighty = 1.0;
        mainContent.add(createInterventionTable(vehicule), gbc);

        // 4. BARRE D'ACTIONS (Bouton Ajout)
        gbc.gridy = 3; gbc.weighty = 0;
        mainContent.add(createActionPanel(), gbc);

        add(new JScrollPane(mainContent), BorderLayout.CENTER);
    }

    private Vehicule rechargerVehicule(Vehicule v) {
        EntityManager em = DbUtils.getEntityManager();
        try {
            // ✅ Utilise JOIN FETCH pour récupérer les interventions et types de véhicules liés
            return em.createQuery(
                            "SELECT v FROM Vehicule v " +
                                    "LEFT JOIN FETCH v.interventions i " +
                                    "LEFT JOIN FETCH i.typeIntervention " +
                                    "WHERE v.vin = :vin", Vehicule.class)
                    .setParameter("vin", v.getVin())
                    .getSingleResult();
        } catch (Exception e) {
            return v; // Repli sur l'objet original en cas d'erreur
        } finally {
            em.close();
        }
    }

    private JPanel createClientCard(Vehicule v) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "COORDONNÉES PROPRIÉTAIRE"));
        panel.setBackground(new Color(245, 245, 248));

        String clientInfo = "<html><body style='padding:10px;'>"
                + "<font size='5'><b>" + v.getProprietaire().getNom().toUpperCase() + " " + v.getProprietaire().getPrenom() + "</b></font><br><br>"
                + "<b>N° Client :</b> " + v.getProprietaire().getNumeroClient() + "<br>"
                + "<b>Email :</b> " + v.getProprietaire().getEmail() + "<br>"
                + "<b>Téléphone :</b> " + v.getProprietaire().getTelephone()
                + "</body></html>";

        panel.add(new JLabel(clientInfo), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTechnicalGrid(Vehicule v) {
        JPanel panel = new JPanel(new GridLayout(0, 3, 20, 10));
        panel.setBorder(BorderFactory.createTitledBorder("SPÉCIFICATIONS VÉHICULE"));

        panel.add(createDataLabel("Marque / Modèle", v.getType().getMarque() + " " + v.getType().getModele()));
        panel.add(createDataLabel("VIN", v.getVin()));
        panel.add(createDataLabel("Kilométrage Actuel", v.getKilometrageActuel() + " km"));
        panel.add(createDataLabel("Puissance (ch)", String.valueOf(v.getType().getPuissance())));
        panel.add(createDataLabel("Boite", v.getType().getBoite()));
        panel.add(createDataLabel("Énergie", v.getType().getEnergie()));

        return panel;
    }

    private JPanel createInterventionTable(Vehicule v) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("HISTORIQUE DES INTERVENTIONS"));

        String[] cols = {"Date", "Type d'intervention", "Kilométrage", "Prix TTC", "Commentaire"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Intervention i : v.getInterventions()) {
            model.addRow(new Object[]{
                    i.getDate(),
                    i.getTypeIntervention() != null ? i.getTypeIntervention().getNom() : "N/C",
                    i.getKilometrage() + " km",
                    String.format("%.2f €", i.getPrixTotal()),
                    i.getCommentaire()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(0, 300));
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        CustomButton btnAdd = new CustomButton("Nouvelle Intervention");
        btnAdd.setPreferredSize(new Dimension(220, 40));

        btnAdd.addActionListener(e -> {
            if (navListener != null) {
                navListener.ouvrirAjoutIntervention(vehicule);
            }
        });

        panel.add(btnAdd);
        return panel;
    }

    private JLabel createDataLabel(String title, String value) {
        return new JLabel("<html><b>" + title + " :</b><br>" + (value != null ? value : "N/C") + "</html>");
    }
}