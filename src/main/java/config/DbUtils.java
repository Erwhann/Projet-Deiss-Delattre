package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modele.*;

import java.time.LocalDate;
import java.util.*;

public class DbUtils {

    private static EntityManagerFactory entityManagerFactory = null;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("Auto2i");
        } catch (Throwable ex) {
            System.err.println("Échec de l'initialisation de l'EntityManagerFactory : " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public static void seedDatabase(EntityManager em) {
        Long countTypes = em.createQuery("SELECT COUNT(t) FROM TypeVehicule t", Long.class).getSingleResult();
        if (countTypes == null) countTypes = 0L;

        if (countTypes == 0) {
            System.out.println("Amorçage intensif : 4 technos, 6 véhicules, historique massif...");
            try {
                em.getTransaction().begin();

                List<TypeVehicule> allTypes = new ArrayList<>();
                List<Piece> allPieces = new ArrayList<>();
                List<TypeIntervention> allInterventions = new ArrayList<>();
                List<Personnel> techniciens = new ArrayList<>();

                // 1) LES 4 TECHNICIENS (Profils variés)
                String[][] staffs = {
                        {"ALAIN", "Pro", "alain@auto2i.fr", "1111", "Technicien", "2100"},
                        {"BERNARD", "Expert", "bernard@auto2i.fr", "2222", "Technicien", "2300"},
                        {"CLAUDE", "Meca", "claude@auto2i.fr", "3333", "Technicien", "2200"},
                        {"ADMIN", "Boss", "admin@auto2i.fr", "0000", "Admin", "3500"}
                };
                for (String[] s : staffs) {
                    Personnel p = new Personnel(s[0], s[1], s[2], "0600000000", s[4], s[3]);
                    p.setSalaire(Double.parseDouble(s[5]));
                    em.persist(p);
                    techniciens.add(p);
                }

                // 2) TYPES DE VÉHICULES (30)
                String[] marques = {"Renault", "Peugeot", "VW", "Audi", "BMW", "Toyota"};
                for (int i = 0; i < 30; i++) {
                    TypeVehicule tv = new TypeVehicule(marques[i % marques.length], "Serie-" + (i + 1),
                            (i % 2 == 0 ? "Essence" : "Diesel"), (i % 3 == 0 ? "Auto" : "Manu"), 5, 5, 90 + i, 5 + (i % 3));
                    em.persist(tv);
                    allTypes.add(tv);
                }

                // 3) CATALOGUE PIÈCES (réaliste)
                String[][] piecesData = {
                        {"FH-101", "Filtre à huile", "12.50"}, {"HM-5W30", "Huile 5W30", "39.90"},
                        {"PF-AV", "Plaquettes AV", "79.90"}, {"DF-AV", "Disques AV", "149.90"},
                        {"BAT-70", "Batterie 70Ah", "129.90"}, {"PNEU-16", "Pneu R16", "89.90"}
                };
                Map<String, Piece> pieceByRef = new HashMap<>();
                for (String[] p : piecesData) {
                    Piece piece = new Piece(p[0], p[1], Double.parseDouble(p[2]));
                    em.persist(piece);
                    allPieces.add(piece);
                    pieceByRef.put(p[0], piece);
                }

                // 4) TYPES D'INTERVENTIONS (avec durée standard)
                TypeIntervention vidange = new TypeIntervention("Vidange moteur", "Entretien classique");
                vidange.setDureeStandard(45); vidange.setPrixBase(50);
                vidange.getPiecesNecessaires().add(pieceByRef.get("FH-101"));
                vidange.getPiecesNecessaires().add(pieceByRef.get("HM-5W30"));
                em.persist(vidange);
                allInterventions.add(vidange);

                TypeIntervention freins = new TypeIntervention("Freins", "Remplacement disques/plaquettes");
                freins.setDureeStandard(90); freins.setPrixBase(100);
                freins.getPiecesNecessaires().add(pieceByRef.get("PF-AV"));
                freins.getPiecesNecessaires().add(pieceByRef.get("DF-AV"));
                em.persist(freins);
                allInterventions.add(freins);

                // 5) CLIENT UNIQUE POUR LES TESTS
                Client client = new Client("DUPONT", "Jean", "jean.dupont@email.com", "0601020304");
                client.setNumeroClient("CLI-TEST-001");
                em.persist(client);

                // 6) GÉNÉRATION DE 6 VÉHICULES AVEC 5 À 8 INTERVENTIONS CHACUN
                Random rnd = new Random();
                String[] immats = {"AA-111-AA", "BB-222-BB", "CC-333-CC", "DD-444-AA", "EE-555-EE", "AA-666-FF"};

                for (String immat : immats) {
                    Vehicule v = new Vehicule();
                    v.setVin("VIN-" + immat);
                    v.setImmatriculation(immat);
                    v.setKilometrageActuel(40000 + rnd.nextInt(50000));
                    v.setDateMiseEnCirculation(LocalDate.now().minusYears(3));
                    v.setType(allTypes.get(rnd.nextInt(allTypes.size())));
                    v.setProprietaire(client);
                    em.persist(v);

                    // Génération de l'historique massif
                    int nbInter = 5 + rnd.nextInt(4); // Entre 5 et 8
                    for (int j = 0; j < nbInter; j++) {
                        Intervention inter = (rnd.nextBoolean()) ? new Entretien() : new Reparation();
                        TypeIntervention ti = allInterventions.get(rnd.nextInt(allInterventions.size()));

                        inter.setVehicule(v);
                        inter.setPersonnel(techniciens.get(rnd.nextInt(techniciens.size()))); // Répartition aléatoire technos
                        inter.setTypeIntervention(ti);
                        inter.setDate(LocalDate.now().minusMonths(j + 1));
                        inter.setKilometrage(v.getKilometrageActuel() - (j * 2000));
                        inter.setPrixTotal(200.0 + rnd.nextInt(400));
                        inter.setDureeReel(ti.getDureeStandard()); //
                        inter.setCommentaire("Test auto - Intervention n°" + (j+1));

                        // Transfert des pièces pour éviter les listes vides
                        if (inter instanceof Entretien e) e.setPiecesUtilisees(new ArrayList<>(ti.getPiecesNecessaires()));
                        else if (inter instanceof Reparation r) r.setPiecesUtilisees(new ArrayList<>(ti.getPiecesNecessaires()));

                        em.persist(inter);
                    }
                }

                em.getTransaction().commit();
                System.out.println("✅ Base initialisée : 4 technos, 6 véhicules, ~40 interventions.");

            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                e.printStackTrace();
            }
        } else {
            System.out.println("Base déjà initialisée. Seed ignoré.");
        }
    }

    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}