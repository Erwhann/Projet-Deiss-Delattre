package vue.panels;

import config.DbUtils;
import gestion.GestionVehicule;
import jakarta.persistence.EntityManager;
import modele.Client;
import modele.TypeVehicule;
import modele.Vehicule;
import vue.composants.CustomButton;
import vue.composants.HeaderPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AjoutVehiculePanel extends JPanel {

    private final GestionVehicule gestionVehicule = new GestionVehicule();
    private JTextField vinField, immatField, kmField, dateField, proprietaireField, mailField, telField;
    private JComboBox<TypeVehicule> typeVehiculeBox;

    public AjoutVehiculePanel() {
        setLayout(new BorderLayout());
        add(new HeaderPanel("Saisie d’un nouveau véhicule"), BorderLayout.NORTH);

        // Conteneur principal en 3 colonnes
        JPanel mainContent = new JPanel(new GridLayout(1, 3, 20, 0));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainContent.add(buildTypeVehiculePanel());
        mainContent.add(buildInfosVehiculePanel());
        mainContent.add(buildProprietairePanel());

        add(new JScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel buildTypeVehiculePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("1. Référence Modèle"));
        GridBagConstraints gbc = createGbc();

        typeVehiculeBox = new JComboBox<>();
        chargerTypesDepuisDB();

        addRow(p, gbc, 0, "Modèle :", typeVehiculeBox);
        return p;
    }

    private JPanel buildInfosVehiculePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("2. Détails Véhicule"));
        GridBagConstraints gbc = createGbc();

        vinField = new JTextField(17);
        immatField = new JTextField();
        dateField = new JTextField(LocalDate.now().toString());
        kmField = new JTextField("0");

        addRow(p, gbc, 0, "VIN :", vinField);
        addRow(p, gbc, 1, "Immat :", immatField);
        addRow(p, gbc, 2, "Mise en circu :", dateField);
        addRow(p, gbc, 3, "Kilométrage :", kmField);

        return p;
    }

    private JPanel buildProprietairePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("3. Propriétaire"));
        GridBagConstraints gbc = createGbc();

        mailField = new JTextField();
        proprietaireField = new JTextField();
        telField = new JTextField();

        // Champ Email avec bouton recherche
        JPanel emailSearchPanel = new JPanel(new BorderLayout(5, 0));
        emailSearchPanel.add(mailField, BorderLayout.CENTER);
        JButton btnSearch = new JButton("🔍");
        btnSearch.addActionListener(e -> rechercherClientExistant());
        emailSearchPanel.add(btnSearch, BorderLayout.EAST);

        addRow(p, gbc, 0, "Email :", emailSearchPanel);
        addRow(p, gbc, 1, "Nom Prénom :", proprietaireField);
        addRow(p, gbc, 2, "Téléphone :", telField);

        CustomButton btnSave = new CustomButton("Enregistrer le véhicule");
        btnSave.addActionListener(e -> enregistrer());
        gbc.gridy = 4; gbc.gridx = 1; gbc.insets = new Insets(20, 5, 5, 5);
        p.add(btnSave, gbc);

        return p;
    }

    private void rechercherClientExistant() {
        String email = mailField.getText().trim();
        if (email.isEmpty()) return;

        EntityManager em = DbUtils.getEntityManager();
        try {
            List<Client> results = em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class)
                    .setParameter("email", email).getResultList();

            if (!results.isEmpty()) {
                Client c = results.get(0);
                proprietaireField.setText(c.getNom() + " " + c.getPrenom());
                telField.setText(c.getTelephone());
            } else {
                JOptionPane.showMessageDialog(this, "Nouveau client (non trouvé).");
            }
        } finally { em.close(); }
    }

    private void enregistrer() {
        try {
            String fullName = proprietaireField.getText().trim();
            String[] parts = fullName.split("\\s+");
            String nom = parts[0];
            String prenom = (parts.length > 1) ? parts[1] : "";

            gestionVehicule.ajouterVehicule(
                    immatField.getText().trim(),
                    vinField.getText().trim(),
                    Integer.parseInt(kmField.getText().trim()),
                    LocalDate.parse(dateField.getText().trim()),
                    nom, prenom, telField.getText().trim(),
                    mailField.getText().trim(),
                    (TypeVehicule) typeVehiculeBox.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Véhicule enregistré ! ✅");
            resetForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void chargerTypesDepuisDB() {
        EntityManager em = DbUtils.getEntityManager();
        try {
            List<TypeVehicule> types = em.createQuery("SELECT t FROM TypeVehicule t", TypeVehicule.class).getResultList();
            typeVehiculeBox.setModel(new DefaultComboBoxModel<>(types.toArray(new TypeVehicule[0])));
        } finally { em.close(); }
    }

    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int y, String label, JComponent c) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0.3;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        p.add(c, gbc);
    }

    private void resetForm() {
        vinField.setText(""); immatField.setText("");
        proprietaireField.setText(""); mailField.setText(""); telField.setText("");
    }
}