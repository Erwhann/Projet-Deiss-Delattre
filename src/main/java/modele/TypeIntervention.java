package modele;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_intervention")
public class TypeIntervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private double prixBase; // AJOUT : Prix forfaitaire de base

    @ManyToMany
    @JoinTable(
            name = "type_intervention_piece",
            joinColumns = @JoinColumn(name = "type_intervention_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id")
    )
    private List<Piece> piecesNecessaires = new ArrayList<>();

    public TypeIntervention() {}
    public TypeIntervention(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
    public double getPrixBase() { return prixBase; }
    public void setPrixBase(double prixBase) { this.prixBase = prixBase; }

    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public List<Piece> getPiecesNecessaires() { return piecesNecessaires; }
    // Dans ton entité TypeIntervention
    private int dureeStandard; // Durée en minutes (ex: 60)

    public int getDureeStandard() { return dureeStandard; }
    public void setDureeStandard(int dureeStandard) { this.dureeStandard = dureeStandard; }
    @Override
    public String toString() { return nom; }
}