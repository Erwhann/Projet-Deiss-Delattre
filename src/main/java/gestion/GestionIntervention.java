package gestion;

import config.DbUtils;
import jakarta.persistence.EntityManager;
import modele.Intervention;
import java.util.*;

public class GestionIntervention {

    public List<Intervention> getInterventionsByPersonnelId(int personnelId) {
        EntityManager em = DbUtils.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.personnel.id = :id ORDER BY i.date DESC",
                    Intervention.class
            ).setParameter("id", personnelId).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean enregistrerIntervention(Intervention inter) {
        EntityManager em = DbUtils.getEntityManager();
        try {
            em.getTransaction().begin();
            // Merge pour rattacher les entités liées (Véhicule, Type, Personnel)
            if (inter.getVehicule() != null) inter.setVehicule(em.merge(inter.getVehicule()));
            if (inter.getTypeIntervention() != null) inter.setTypeIntervention(em.merge(inter.getTypeIntervention()));
            if (inter.getPersonnel() != null) inter.setPersonnel(em.merge(inter.getPersonnel()));

            em.persist(inter);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}