package modele;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "piece")
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPiece; //

    @Column(nullable = false, unique = true)
    private String reference; //

    @Column(nullable = false)
    private String nom; //

    private double prix; //

    @ManyToMany(mappedBy = "piecesCompatibles")
    private List<TypeVehicule> typesCompatibles = new ArrayList<>();

    @ManyToMany(mappedBy = "piecesUtilisees")
    private List<Reparation> reparations = new ArrayList<>();

    @ManyToMany(mappedBy = "piecesUtilisees")
    private List<Entretien> entretiens = new ArrayList<>();

    @ManyToMany(mappedBy = "piecesNecessaires")
    private List<TypeIntervention> typesInterventionCompatibles = new ArrayList<>();

    // --- Constructeurs ---
    public Piece() {}

    public Piece(String reference, String nom, double prix) {
        this.reference = reference;
        this.nom = nom;
        this.prix = prix;
    }

    // --- Getters et Setters ---
    public int getIdPiece() { return idPiece; }
    public void setIdPiece(int idPiece) { this.idPiece = idPiece; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public List<TypeVehicule> getTypesCompatibles() { return typesCompatibles; }
    public void setTypesCompatibles(List<TypeVehicule> typesCompatibles) { this.typesCompatibles = typesCompatibles; }

    public List<Reparation> getReparations() { return reparations; }
    public void setReparations(List<Reparation> reparations) { this.reparations = reparations; }

    public List<Entretien> getEntretiens() { return entretiens; }
    public void setEntretiens(List<Entretien> entretiens) { this.entretiens = entretiens; }

    public List<TypeIntervention> getTypesInterventionCompatibles() { return typesInterventionCompatibles; }
    public void setTypesInterventionCompatibles(List<TypeIntervention> list) { this.typesInterventionCompatibles = list; }

    @Override
    public String toString() {
        return nom + " (Ref: " + reference + ") - " + prix + "€";
    }
}