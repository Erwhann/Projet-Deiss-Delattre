package modele;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entretien")
@PrimaryKeyJoinColumn(name = "id")
public class Entretien extends Intervention {

    private int kmMax;
    private int dureeMaxMois;
    private int prix; // Prix spécifique forfaitaire

    @ManyToMany
    @JoinTable(
            name = "entretien_piece",
            joinColumns = @JoinColumn(name = "entretien_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id")
    )
    private List<Piece> piecesUtilisees = new ArrayList<>();

    public Entretien() {}

    public Entretien(LocalDate date, int kilometrage, double prixTotal, int dureeReel, Vehicule vehicule, int kmMax, int dureeMaxMois, int prix) {
        super(date, kilometrage, prixTotal, dureeReel, vehicule);
        this.kmMax = kmMax;
        this.dureeMaxMois = dureeMaxMois;
        this.prix = prix;
    }

    public List<Piece> getPiecesUtilisees() { return piecesUtilisees; }
    public void setPiecesUtilisees(List<Piece> piecesUtilisees) { this.piecesUtilisees = piecesUtilisees; }
    public int getPrix() { return prix; }
    public void setPrix(int prix) { this.prix = prix; }
}