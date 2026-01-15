package vue;

import modele.Personnel;
import modele.Vehicule;
import vue.composants.HeaderPanel;
import vue.composants.NavigationListener;
import vue.composants.CustomButton;
import vue.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame implements NavigationListener {

    private static final String DASHBOARD = "dashboard";
    private static final String AJOUT_VEHICULE = "ajoutVehicule";
    private static final String RECHERCHE_VEHICULE = "rechercheVehicule";
    private static final String AJOUT_INTERVENTION = "ajoutIntervention";
    private static final String DETAILS_VEHICULE = "detailsVehicule";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final Deque<String> history = new ArrayDeque<>();
    private final Map<String, Vehicule> cacheJournalier = new LinkedHashMap<>();

    private static Personnel utilisateurSession;
    private String currentView = DASHBOARD;
    private HeaderPanel header;
    private JPanel recentsContainer;
    private AjoutInterventionPanel ajoutInterventionPanel;
    private DashboardPanel dashboardPanel;
    private static MainFrame instance;
    public MainFrame() {
         instance = this;
        setTitle("Auto2I - Logiciel de Gestion Garage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        header = new HeaderPanel("Auto2I - Gestion", this::retour);
        add(buildTop(), BorderLayout.NORTH);

        // ✅ On garde une référence précise au Dashboard
        dashboardPanel = new DashboardPanel();
        contentPanel.add(dashboardPanel, DASHBOARD);
        contentPanel.add(new AjoutVehiculePanel(), AJOUT_VEHICULE);
        contentPanel.add(new RechercheVehiculePanel(this), RECHERCHE_VEHICULE);

        ajoutInterventionPanel = new AjoutInterventionPanel();
        contentPanel.add(ajoutInterventionPanel, AJOUT_INTERVENTION);

        add(contentPanel, BorderLayout.CENTER);
        changerVue(DASHBOARD, false);
        setVisible(true);
    }

    public static void setUtilisateurSession(Personnel p) {
        utilisateurSession = p;
        // ✅ STRATÉGIE : Si on change de session, on vide l'historique immédiatement
        if (instance != null) {
            instance.resetHistorique();
        }
    }

    // ✅ Méthode pour vider proprement le cache et l'interface
    public void resetHistorique() {
        this.cacheJournalier.clear();
        this.rafraichirHistorique();
    }    public static Personnel getUtilisateurSession() { return utilisateurSession; }

    @Override
    public void afficherDetailsVehicule(Vehicule v) {
        if (v == null) return;
        cacheJournalier.remove(v.getImmatriculation());
        cacheJournalier.put(v.getImmatriculation(), v);
        AffichageVehiculePanel detailPanel = new AffichageVehiculePanel(v, this);
        contentPanel.add(detailPanel, DETAILS_VEHICULE);
        changerVue(DETAILS_VEHICULE, true);
    }

    @Override
    public void ouvrirAjoutIntervention(Vehicule v) {
        if (ajoutInterventionPanel != null && v != null) {
            ajoutInterventionPanel.preselectVehicule(v);
        }
        changerVue(AJOUT_INTERVENTION, true);
    }

    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        JPanel navContainer = new JPanel(new BorderLayout());
        navContainer.setBackground(new Color(44, 62, 80));

        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        leftNav.setOpaque(false);
        leftNav.add(navButton("Dashboard", DASHBOARD));
        leftNav.add(navButton("Véhicule (+)", AJOUT_VEHICULE));
        leftNav.add(navButton("Rechercher", RECHERCHE_VEHICULE));
        leftNav.add(navButton("Intervention (+)", AJOUT_INTERVENTION));

        navContainer.add(leftNav, BorderLayout.WEST);

        recentsContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 8));
        recentsContainer.setOpaque(false);
        navContainer.add(recentsContainer, BorderLayout.CENTER);
        top.add(navContainer, BorderLayout.SOUTH);
        return top;
    }

    private void rafraichirHistorique() {
        recentsContainer.removeAll();
        for (String immat : cacheJournalier.keySet()) {
            CustomButton btn = new CustomButton(immat);
            btn.addActionListener(e -> afficherDetailsVehicule(cacheJournalier.get(immat)));
            recentsContainer.add(btn);
        }
        recentsContainer.revalidate(); recentsContainer.repaint();
    }

    private JButton navButton(String text, String target) {
        CustomButton btn = new CustomButton(text);
        btn.addActionListener(e -> changerVue(target, true));
        return btn;
    }

    private void changerVue(String vue, boolean pushHistory) {
        // ✅ SÉCURITÉ : Bloquer si pas de session (sauf pour le login sur Dashboard)
        if (utilisateurSession == null && !DASHBOARD.equals(vue)) {
            JOptionPane.showMessageDialog(this, "Accès refusé : Identifiez-vous sur le Dashboard.");
            cardLayout.show(contentPanel, DASHBOARD);
            return;
        }

        // ✅ SYNCHRO : Si on va sur le Dashboard, on rafraîchit les KPIs
        if (DASHBOARD.equals(vue)) {
            dashboardPanel.rafraichirDonneesSiConnecte();
        }

        if (pushHistory && !vue.equals(currentView)) history.push(currentView);
        currentView = vue;
        cardLayout.show(contentPanel, vue);
        rafraichirHistorique();
        updateBackButton();
    }

    private void retour() {
        if (!history.isEmpty()) changerVue(history.pop(), false);
    }

    private void updateBackButton() {
        header.setBackEnabled(!history.isEmpty());
    }
}