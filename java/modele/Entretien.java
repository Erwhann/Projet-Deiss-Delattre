package modele;

import jakarta.persistence.*;

@Entity
@Table(name = "entretien")
@PrimaryKeyJoinColumn(name = "id")
public class Entretien extends Intervention {

    private int kmMax; [cite_start]// [cite: 503]
    private int dureeMaxMois; [cite_start]// [cite: 504]
    private int prix; [cite_start]// Prix forfaitaire de l'entretien [cite: 505]
    @ManyToMany
    @JoinTable(
            name = "entretien_piece",
            joinColumns = @JoinColumn(name = "entretien_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id")
    )
    private java.util.List<Piece> piecesUtilisees = new java.util.ArrayList<>();

    public java.util.List<Piece> getPiecesUtilisees() { return piecesUtilisees; }
    public void setPiecesUtilisees(java.util.List<Piece> piecesUtilisees) { this.piecesUtilisees = piecesUtilisees; }

    // --- Constructeurs ---
    public Entretien() {}

    public Entretien(java.time.LocalDate date, int kilometrage, double prixTotal, int dureeReel, Vehicule vehicule, int kmMax, int dureeMaxMois, int prix) {
        super(date, kilometrage, prixTotal, dureeReel, vehicule);
        this.kmMax = kmMax;
        this.dureeMaxMois = dureeMaxMois;
        this.prix = prix;
    }

    // --- Getters et Setters ---
    public int getKmMax() { return kmMax; }
    public void setKmMax(int kmMax) { this.kmMax = kmMax; }

    public int getDureeMaxMois() { return dureeMaxMois; }
    public void setDureeMaxMois(int dureeMaxMois) { this.dureeMaxMois = dureeMaxMois; }

    public int getPrix() { return prix; }
    public void setPrix(int prix) { this.prix = prix; }
}