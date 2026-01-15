package gestion;

import config.DbUtils;
import jakarta.persistence.EntityManager;
import modele.*;
import java.time.LocalDate;
import java.util.List;

public class GestionVehicule {

    public Vehicule ajouterVehicule(String immat, String vin, int km, LocalDate date,
                                    String nom, String prenom, String tel, String email,
                                    TypeVehicule type) {

        EntityManager em = DbUtils.getEntityManager();
        try {
            em.getTransaction().begin();

            // Recherche Client existant
            Client client;
            List<Client> existing = em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class)
                    .setParameter("email", email).getResultList();

            if (!existing.isEmpty()) {
                client = existing.get(0);
                client.setNom(nom); client.setPrenom(prenom);
            } else {
                client = new Client(nom, prenom, email, tel);
                em.persist(client);
            }

            Vehicule v = new Vehicule(vin, immat, date, km, em.merge(type), client);
            em.persist(v);

            em.getTransaction().commit();
            return v;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    public List<Vehicule> chercherParImmatriculation(String immat) {
        EntityManager em = DbUtils.getEntityManager();
        try {
            return em.createQuery("SELECT v FROM Vehicule v WHERE v.immatriculation LIKE :immat", Vehicule.class)
                    .setParameter("immat", immat + "%").getResultList();
        } finally { em.close(); }
    }
}