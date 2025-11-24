package modele;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Crée une table 'personne' et une table 'client' liée
public abstract class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id; // [cite: 475]

    @Column(nullable = false)
    protected String nom; // [cite: 477]

    @Column(nullable = false)
    protected String prenom; // [cite: 476]

    // --- Constructeurs ---
    public Personne() {}

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    // --- Getters et Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}