package vue;

import config.DbUtils;
import jakarta.persistence.EntityManager;
import javax.swing.*;

/**
 * Point d'entrée principal de l'application Auto2I.
 * Gère l'amorçage de la base de données et le lancement de l'IHM.
 */
public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("=== DÉMARRAGE AUTO2I SYSTÈME ===");

            // 1. Initialisation de l'EntityManager via DbUtils
            // On récupère un EntityManager pour les opérations de pré-lancement
            EntityManager em = DbUtils.getEntityManager();

            System.out.println("Connexion à la base de données établie. ✅");

            // 2. Amorçage stratégique des données (Seed)
            // Cette méthode vérifie d'elle-même si la base est vide avant d'injecter
            // les 4 techniciens, 6 véhicules et les ~40 interventions.
            System.out.println("Vérification de l'intégrité des données...");
            DbUtils.seedDatabase(em);


            // 4. Fermeture de l'EntityManager de service
            if (em.isOpen()) {
                em.close();
            }

            // 5. Lancement de l'interface graphique (Swing Thread)
            SwingUtilities.invokeLater(() -> {
                try {
                    // Look and Feel système pour une intégration propre
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {}

                System.out.println("Lancement de la fenêtre principale (MainFrame)...");

                /* * NOTE STRATÉGIQUE : La MainFrame s'auto-enregistre comme instance
                 * statique dans son constructeur. Cela permet au Dashboard d'appeler
                 * le reset de l'historique lors d'un changement de profil.
                 */
                MainFrame frame = new MainFrame();
                frame.setVisible(true);

                System.out.println("Application prête. En attente de connexion utilisateur.");
            });

        } catch (Exception e) {
            System.err.println("ERREUR CRITIQUE AU DÉMARRAGE :");
            System.err.println("Vérifiez que l'unité de persistance 'Auto2i' est bien configurée dans persistence.xml");
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,
                    "Erreur critique lors de l'accès à la base de données :\n" + e.getMessage(),
                    "Erreur de lancement",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }
    }
}