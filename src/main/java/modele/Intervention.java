package modele;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "intervention")
public abstract class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(nullable = false)
    protected LocalDate date;

    protected int kilometrage;
    protected double prixTotal;
    protected int dureeReel;
    protected String commentaire;

    @ManyToOne
    @JoinColumn(name = "type_intervention_id")
    protected TypeIntervention typeIntervention;

    @ManyToOne
    @JoinColumn(name = "vehicule_vin", nullable = false)
    protected Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "personnel_id")
    protected Personnel personnel;

    @ManyToMany
    @JoinTable(
            name = "intervention_piece",
            joinColumns = @JoinColumn(name = "intervention_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id")
    )
    protected List<Piece> pieces = new ArrayList<>();

    public Intervention() {}

    public Intervention(LocalDate date, int kilometrage, double prixTotal, int dureeReel, Vehicule vehicule) {
        this.date = date;
        this.kilometrage = kilometrage;
        this.prixTotal = prixTotal;
        this.dureeReel = dureeReel;
        this.vehicule = vehicule;
    }

    // --- Getters / Setters ---
    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getKilometrage() { return kilometrage; }
    public void setKilometrage(int kilometrage) { this.kilometrage = kilometrage; }
    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }
    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }
    public TypeIntervention getTypeIntervention() { return typeIntervention; }
    public void setTypeIntervention(TypeIntervention typeIntervention) { this.typeIntervention = typeIntervention; }
    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public int getDureeReel() {
        return dureeReel;
    }

    public void setDureeReel(int dureeReel) {
        this.dureeReel = dureeReel;
    }
}