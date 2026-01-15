package gestion;

import config.DbUtils;
import jakarta.persistence.EntityManager;
import modele.Personnel;

import java.util.List;

public class GestionPersonnel {

    public Personnel login(String email, String pin) {
        EntityManager em = DbUtils.getEntityManager();
        try {
            List<Personnel> res = em.createQuery(
                    "SELECT p FROM Personnel p WHERE p.email = :email",
                    Personnel.class
            ).setParameter("email", email).getResultList();

            if (res.isEmpty()) return null;

            Personnel p = res.get(0);
            if (p.getPin() != null && p.getPin().equals(pin)) return p;

            return null;
        } finally {
            em.close();
        }
    }

    public Personnel creerTechnicien(String nom,
                                     String prenom,
                                     String email,
                                     String telephone,
                                     String poste,
                                     String pin,
                                     double salaire) {

        EntityManager em = DbUtils.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(p) FROM Personnel p WHERE p.email = :email",
                    Long.class
            ).setParameter("email", email).getSingleResult();

            if (count != null && count > 0) return null;

            em.getTransaction().begin();

            Personnel tech = new Personnel(nom, prenom, email, telephone, poste, pin);
            tech.setSalaire(salaire);
            em.persist(tech);

            em.getTransaction().commit();
            return tech;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
