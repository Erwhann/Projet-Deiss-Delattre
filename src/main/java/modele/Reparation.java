package modele;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reparation")
@PrimaryKeyJoinColumn(name = "id")
public class Reparation extends Intervention {

    @Column(nullable = false)
    private double prixMainOeuvre;

    @ManyToMany
    @JoinTable(
            name = "reparation_piece",
            joinColumns = @JoinColumn(name = "reparation_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id")
    )
    private List<Piece> piecesUtilisees = new ArrayList<>();

    public Reparation() { super(); }

    public Reparation(LocalDate date, int kilometrage, double prixTotal, int dureeReel, Vehicule vehicule, double prixMainOeuvre) {
        super(date, kilometrage, prixTotal, dureeReel, vehicule);
        this.prixMainOeuvre = prixMainOeuvre;
    }

    // STRATÉGIE : Calcul selon Formule (Somme Pièces + Base) * (1 + CV/20)
    public void appliquerFormulePrix() {
        if (this.vehicule == null || this.typeIntervention == null) return;

        double totalPieces = this.piecesUtilisees.stream().mapToDouble(Piece::getPrix).sum();
        double base = this.typeIntervention.getPrixBase();
        int cv = this.vehicule.getType().getCvFiscaux();

        this.prixTotal = (totalPieces + base) * (1 + (cv / 20.0));
    }

    public List<Piece> getPiecesUtilisees() { return piecesUtilisees; }
    public void setPiecesUtilisees(List<Piece> piecesUtilisees) { this.piecesUtilisees = piecesUtilisees; }
    public double getPrixMainOeuvre() { return prixMainOeuvre; }
    public void setPrixMainOeuvre(double prixMainOeuvre) { this.prixMainOeuvre = prixMainOeuvre; }
}