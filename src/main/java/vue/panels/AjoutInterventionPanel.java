package vue.panels;

import config.DbUtils;
import jakarta.persistence.EntityManager;
import gestion.GestionIntervention;
import modele.*;
import vue.composants.CustomButton;
import vue.composants.HeaderPanel;
import vue.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AjoutInterventionPanel extends JPanel {

    private final GestionIntervention gestion = new GestionIntervention();
    private JComboBox<Vehicule> cbVehicule;
    private JComboBox<TypeIntervention> cbType;
    private JComboBox<String> cbNature;
    private JTextField tfPrixHT, tfPrixTTC, tfDate;
    private JSpinner spKilometrage, spTva;
    private JTextArea taCommentaire;
    private DefaultListModel<String> pieceModel = new DefaultListModel<>();

    public AjoutInterventionPanel() {
        setLayout(new BorderLayout());
        add(new HeaderPanel("Saisie d'Intervention"), BorderLayout.NORTH);

        JScrollPane scrollForm = new JScrollPane(buildForm());
        scrollForm.setBorder(null);
        add(scrollForm, BorderLayout.CENTER);

        refreshCombos();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        cbVehicule = new JComboBox<>();
        cbType = new JComboBox<>();
        cbNature = new JComboBox<>(new String[]{"Entretien", "Réparation"});

        cbType.addActionListener(e -> calculerPrixTotal());
        cbVehicule.addActionListener(e -> calculerPrixTotal());

        tfDate = new JTextField(LocalDate.now().toString());
        spKilometrage = new JSpinner(new SpinnerNumberModel(0, 0, 2000000, 100));
        tfPrixHT = new JTextField("0.00"); tfPrixHT.setEditable(false);
        spTva = new JSpinner(new SpinnerNumberModel(20, 0, 100, 1));
        spTva.addChangeListener(e -> recalculerTTC());
        tfPrixTTC = new JTextField("0.00"); tfPrixTTC.setEditable(false);

        taCommentaire = new JTextArea(4, 30);
        taCommentaire.setLineWrap(true);
        taCommentaire.setWrapStyleWord(true);

        int y = 0;
        addRow(p, gbc, y++, "Véhicule :", cbVehicule, false);
        addRow(p, gbc, y++, "Nature des travaux :", cbNature, false);
        addRow(p, gbc, y++, "Forfait Intervention :", cbType, false);

        JScrollPane scrollPieces = new JScrollPane(new JList<>(pieceModel));
        scrollPieces.setPreferredSize(new Dimension(0, 100));
        addRow(p, gbc, y++, "Détail des pièces :", scrollPieces, true);

        addRow(p, gbc, y++, "Date :", tfDate, false);
        addRow(p, gbc, y++, "Kilométrage relevé :", spKilometrage, false);
        addRow(p, gbc, y++, "Montant HT :", tfPrixHT, false);
        addRow(p, gbc, y++, "TVA (%) :", spTva, false);
        addRow(p, gbc, y++, "Total TTC :", tfPrixTTC, false);

        addRow(p, gbc, y++, "Commentaires techniques :", new JScrollPane(taCommentaire), true);

        CustomButton btnSave = new CustomButton("Enregistrer en Base de Données");
        btnSave.addActionListener(e -> enregistrer());

        gbc.gridy = y; gbc.gridx = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        p.add(btnSave, gbc);

        return p;
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int y, String label, JComponent c, boolean expand) {
        gbc.gridy = y; gbc.gridx = 0; gbc.weightx = 0; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        p.add(new JLabel(label), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        if (expand) {
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 0.2;
        } else {
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weighty = 0;
        }
        p.add(c, gbc);
    }

    private void calculerPrixTotal() {
        TypeIntervention type = (TypeIntervention) cbType.getSelectedItem();
        Vehicule v = (Vehicule) cbVehicule.getSelectedItem();
        if (type == null || v == null) return;

        pieceModel.clear();
        double sommePieces = 0;
        for (Piece piece : type.getPiecesNecessaires()) {
            pieceModel.addElement(piece.getNom() + " - " + piece.getPrix() + " €");
            sommePieces += piece.getPrix();
        }

        int cv = v.getType().getCvFiscaux();
        double totalHT = (sommePieces + type.getPrixBase()) * (1 + (cv / 20.0));

        tfPrixHT.setText(String.format("%.2f", totalHT).replace(',', '.'));
        recalculerTTC();
    }

    private void enregistrer() {
        try {
            TypeIntervention type = (TypeIntervention) cbType.getSelectedItem();
            Intervention inter;

            if ("Entretien".equals(cbNature.getSelectedItem())) {
                Entretien e = new Entretien();
                e.setPiecesUtilisees(new ArrayList<>(type.getPiecesNecessaires()));
                inter = e;
            } else {
                Reparation r = new Reparation();
                r.setPiecesUtilisees(new ArrayList<>(type.getPiecesNecessaires()));
                inter = r;
            }

            inter.setVehicule((Vehicule) cbVehicule.getSelectedItem());
            inter.setTypeIntervention(type);
            inter.setDate(LocalDate.parse(tfDate.getText()));
            inter.setKilometrage((Integer) spKilometrage.getValue());
            inter.setPrixTotal(Double.parseDouble(tfPrixTTC.getText().replace(',', '.')));
            inter.setCommentaire(taCommentaire.getText());
            inter.setPersonnel(MainFrame.getUtilisateurSession());

            // ✅ FIX DUREE REEL : Synchronisée sur le forfait standard
            inter.setDureeReel(type.getDureeStandard());

            if (gestion.enregistrerIntervention(inter)) {
                JOptionPane.showMessageDialog(this, "Intervention validée et pièces décomptées ! ✅");
                taCommentaire.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur fatale : " + ex.getMessage());
        }
    }

    private void recalculerTTC() {
        try {
            double ht = Double.parseDouble(tfPrixHT.getText());
            double tva = (int) spTva.getValue();
            tfPrixTTC.setText(String.format("%.2f", ht * (1 + tva / 100.0)).replace(',', '.'));
        } catch (Exception e) { tfPrixTTC.setText("0.00"); }
    }

    private void refreshCombos() {
        EntityManager em = DbUtils.getEntityManager();
        try {
            List<TypeIntervention> types = em.createQuery("SELECT DISTINCT t FROM TypeIntervention t LEFT JOIN FETCH t.piecesNecessaires", TypeIntervention.class).getResultList();
            List<Vehicule> vehs = em.createQuery("SELECT v FROM Vehicule v", Vehicule.class).getResultList();
            cbType.setModel(new DefaultComboBoxModel<>(types.toArray(new TypeIntervention[0])));
            cbVehicule.setModel(new DefaultComboBoxModel<>(vehs.toArray(new Vehicule[0])));
            calculerPrixTotal();
        } finally { em.close(); }
    }

    public void preselectVehicule(Vehicule v) {
        if (v != null) cbVehicule.setSelectedItem(v);
        calculerPrixTotal();
    }

    public void onShow() { refreshCombos(); }
}