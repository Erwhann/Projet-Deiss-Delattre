package modele;

import jakarta.persistence.*;

@Entity
@Table(name = "reparation")
@PrimaryKeyJoinColumn(name = "id") // Se lie à l'ID de la table intervention
@ManyToMany
@JoinTable(
        name = "reparation_piece",
        joinColumns = @JoinColumn(name = "reparation_id"),
        inverseJoinColumns = @JoinColumn(name = "piece_id")
)
private java.util.List<Piece> piecesUtilisees = new java.util.ArrayList<>();

public java.util.List<Piece> getPiecesUtilisees() { return piecesUtilisees; }
public void setPiecesUtilisees(java.util.List<Piece> piecesUtilisees) { this.piecesUtilisees = piecesUtilisees; }
public class Reparation extends Intervention {

    private int prix; [cite_start]// Spécifique à la réparation [cite: 498]

    // --- Constructeurs ---
    public Reparation() {}

    public Reparation(java.time.LocalDate date, int kilometrage, double prixTotal, int dureeReel, Vehicule vehicule, int prix) {
        super(date, kilometrage, prixTotal, dureeReel, vehicule);
        this.prix = prix;
    }

    // --- Getters et Setters ---
    public int getPrix() { return prix; }
    public void setPrix(int prix) { this.prix = prix; }
}