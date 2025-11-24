package modele;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Stratégie pour gérer l'héritage proprement en BDD
@Table(name = "intervention")
public abstract class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id; [cite_start]// [cite: 458]

    @Column(nullable = false)
    protected LocalDate date; [cite_start]// [cite: 459]

    protected int kilometrage; [cite_start]// [cite: 460]

    protected double prixTotal; [cite_start]// [cite: 461]

    protected int dureeReel; [cite_start]// [cite: 462]

    @ManyToOne
    @JoinColumn(name = "vehicule_vin", nullable = false)
    protected Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "personnel_id") // Clé étrangère
    protected Personnel personnel;

    // --- Constructeurs ---
    public Intervention() {}

    public Intervention(LocalDate date, int kilometrage, double prixTotal, int dureeReel, Vehicule vehicule) {
        this.date = date;
        this.kilometrage = kilometrage;
        this.prixTotal = prixTotal;
        this.dureeReel = dureeReel;
        this.vehicule = vehicule;
    }

    // --- Getters et Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getKilometrage() { return kilometrage; }
    public void setKilometrage(int kilometrage) { this.kilometrage = kilometrage; }

    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }

    public int getDureeReel() { return dureeReel; }
    public void setDureeReel(int dureeReel) { this.dureeReel = dureeReel; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }

    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }

}