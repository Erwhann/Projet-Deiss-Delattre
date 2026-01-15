package modele;

import jakarta.persistence.*;

@Entity
@Table(name = "personne")
@Inheritance(strategy = InheritanceType.JOINED) // Stratégie : une table par classe (Personne, Client, Employe)
public abstract class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(nullable = false, length = 100)
    protected String nom;

    @Column(nullable = false, length = 100)
    protected String prenom;

    @Column(unique = true) // L'email doit être unique pour identifier les clients existants
    protected String email;

    @Column(length = 20)
    protected String telephone;

    // --- Constructeurs ---
    public Personne() {}

    public Personne(String nom, String prenom, String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    // --- Getters / Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + email + ")";
    }
}