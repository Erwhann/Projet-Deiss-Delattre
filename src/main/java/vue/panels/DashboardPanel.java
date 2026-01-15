package vue.panels;

import gestion.GestionPersonnel;
import gestion.GestionIntervention;
import modele.*;
import vue.composants.CustomButton;
import vue.composants.HeaderPanel;
import vue.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final CardLayout layout = new CardLayout();
    private final JPanel container = new JPanel(layout);
    private final GestionPersonnel gestionPersonnel = new GestionPersonnel();
    private final GestionIntervention gestionInter = new GestionIntervention();

    private Personnel personnelConnecte;
    private JTextField emailField;
    private JPasswordField pinField;
    private JLabel errorLabel, lblProfil, lblNbInter, lblNbEntretiens, lblNbReparations;
    private CustomButton btnAddTechnicien;
    private DefaultTableModel tableModel;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        add(new HeaderPanel("Dashboard - Gestion Garage"), BorderLayout.NORTH);

        container.add(buildLoginPanel(), "login");
        container.add(buildAfterLoginPanel(), "after");

        add(container, BorderLayout.CENTER);
        layout.show(container, "login");
    }

    private JPanel buildLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(245, 246, 248));

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        box.setPreferredSize(new Dimension(500, 350));

        emailField = new JTextField();
        pinField = new JPasswordField();
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(192, 57, 43));

        CustomButton loginBtn = new CustomButton("Se connecter");
        loginBtn.addActionListener(e -> tenterLogin());

        box.add(new JLabel("Connexion Technicien"));
        box.add(Box.createVerticalStrut(20));
        box.add(new JLabel("Email :")); box.add(emailField);
        box.add(Box.createVerticalStrut(10));
        box.add(new JLabel("PIN :")); box.add(pinField);
        box.add(errorLabel); box.add(loginBtn);

        p.add(box);
        return p;
    }

    private JPanel buildAfterLoginPanel() {
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- GAUCHE : PROFIL ---
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setPreferredSize(new Dimension(220, 0));
        left.setBackground(Color.WHITE);
        left.setBorder(BorderFactory.createTitledBorder("Mon Profil"));

        lblProfil = new JLabel("<html><center>Chargement...</center></html>", SwingConstants.CENTER);
        lblProfil.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(Box.createVerticalStrut(20));
        left.add(lblProfil);

        // --- CENTRE : STATS + TABLEAU ---
        JPanel center = new JPanel(new BorderLayout(0, 15));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        lblNbInter = createStatCard(statsPanel, "Total Interventions", new Color(52, 152, 219));
        lblNbEntretiens = createStatCard(statsPanel, "Entretiens", new Color(46, 204, 113));
        lblNbReparations = createStatCard(statsPanel, "Réparations", new Color(230, 126, 34));

        tableModel = new DefaultTableModel(new String[]{"Date", "Véhicule", "Type", "Prix TTC"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);

        center.add(statsPanel, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- BAS : ACTIONS ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAddTechnicien = new CustomButton("Ajouter technicien");
        btnAddTechnicien.setVisible(false);
        btnAddTechnicien.addActionListener(e -> ouvrirPopupAjoutTechnicien());

        CustomButton logout = new CustomButton("Déconnexion");
        logout.addActionListener(e -> logout());

        bottom.add(btnAddTechnicien); bottom.add(logout);

        main.add(left, BorderLayout.WEST);
        main.add(center, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        return main;
    }

    private JLabel createStatCard(JPanel parent, String title, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        JLabel lblVal = new JLabel("0", SwingConstants.CENTER);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblVal.setForeground(color);
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);
        parent.add(card);
        return lblVal;
    }

    private void tenterLogin() {
        Personnel p = gestionPersonnel.login(emailField.getText().trim(), new String(pinField.getPassword()));
        if (p == null) {
            errorLabel.setText("Identifiants incorrects.");
            return;
        }

        personnelConnecte = p;
        MainFrame.setUtilisateurSession(p);

        lblProfil.setText("<html><center><font size='5'><b>" + p.getNom().toUpperCase() + "</b></font><br>" + p.getPrenom() + "<br><br><i>" + p.getPoste() + "</i></center></html>");

        boolean isAdmin = p.getPoste() != null && p.getPoste().equalsIgnoreCase("Admin");
        btnAddTechnicien.setVisible(isAdmin);

        chargerDonnees();
        layout.show(container, "after");
    }

    public void rafraichirDonneesSiConnecte() {
        if (personnelConnecte != null) {
            chargerDonnees();
        }
    }

    private void chargerDonnees() {
        List<Intervention> list = gestionInter.getInterventionsByPersonnelId(personnelConnecte.getId());

        long nbEntretien = list.stream().filter(i -> i instanceof Entretien).count();
        long nbReparation = list.stream().filter(i -> i instanceof Reparation).count();

        lblNbInter.setText(String.valueOf(list.size()));
        lblNbEntretiens.setText(String.valueOf(nbEntretien));
        lblNbReparations.setText(String.valueOf(nbReparation));

        tableModel.setRowCount(0);
        for (Intervention i : list) {
            tableModel.addRow(new Object[]{
                    i.getDate(),
                    i.getVehicule().getImmatriculation(),
                    i.getTypeIntervention().getNom(),
                    String.format("%.2f €", i.getPrixTotal())
            });
        }
    }

    private void ouvrirPopupAjoutTechnicien() {
        JTextField nom = new JTextField();
        JTextField prenom = new JTextField();
        JTextField email = new JTextField();
        JTextField poste = new JTextField("Technicien");
        JPasswordField pin = new JPasswordField();
        JTextField salaire = new JTextField("2000");

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Nom :")); panel.add(nom);
        panel.add(new JLabel("Prénom :")); panel.add(prenom);
        panel.add(new JLabel("Email :")); panel.add(email);
        panel.add(new JLabel("Poste :")); panel.add(poste);
        panel.add(new JLabel("PIN (4 chiffres) :")); panel.add(pin);
        panel.add(new JLabel("Salaire :")); panel.add(salaire);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nouveau Collaborateur", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double sal = Double.parseDouble(salaire.getText().trim());
                gestionPersonnel.creerTechnicien(nom.getText().trim(), prenom.getText().trim(), email.getText().trim(), "", poste.getText().trim(), new String(pin.getPassword()), sal);
                JOptionPane.showMessageDialog(this, "Technicien ajouté !");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur de saisie : " + ex.getMessage());
            }
        }
    }

    private void logout() {
        personnelConnecte = null;
        MainFrame.setUtilisateurSession(null);
        emailField.setText(""); pinField.setText("");
        layout.show(container, "login");
    }
}