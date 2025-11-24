package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DbUtils {

    private static EntityManagerFactory entityManagerFactory = null;

    static {
        try {
            // Le nom "Auto2iPU" doit correspondre à celui dans persistence.xml
            entityManagerFactory = Persistence.createEntityManagerFactory("Auto2i");
        } catch (Throwable ex) {
            System.err.println("Échec de l'initialisation de la SessionFactory." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Méthode principale pour obtenir une connexion à la BDD
     * À appeler dans tes classes de Gestion
     * @return EntityManager
     */
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Ferme l'usine à la fermeture de l'application
     */
    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}