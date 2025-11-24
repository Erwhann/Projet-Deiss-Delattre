package modele;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehicule")
public class Vehicule {

    @Id
    @Column(length = 17) // Le VIN fait 17 caractères max
    private String vin;

    @Column(length = 10, unique = true)
    private String immatriculation;

    private LocalDate dateMiseEnCirculation;

    private int kilometrageActuel;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private TypeVehicule typeVehicule;

    @ManyToOne
    @JoinColumn(name = "client_id") // Crée la colonne client_id dans la table vehicule
    private Client proprietaire; // Lien vers la classe Client

    // N'oublie pas de mettre à jour le Getter et Setter correspondant !
    public Client getProprietaire() { return proprietaire; }
    public void setProprietaire(Client proprietaire) { this.proprietaire = proprietaire; }


    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL)
    private java.util.List<Intervention> interventions = new java.util.ArrayList<>();

    // --- Constructeurs ---
    public Vehicule() {}

    public Vehicule(String vin, String immatriculation, LocalDate dateMiseEnCirculation, int kilometrageActuel, TypeVehicule typeVehicule) {
        this.vin = vin;
        this.immatriculation = immatriculation;
        this.dateMiseEnCirculation = dateMiseEnCirculation;
        this.kilometrageActuel = kilometrageActuel;
        this.typeVehicule = typeVehicule;
    }

    // --- Getters et Setters ---
    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    public LocalDate getDateMiseEnCirculation() { return dateMiseEnCirculation; }
    public void setDateMiseEnCirculation(LocalDate dateMiseEnCirculation) { this.dateMiseEnCirculation = dateMiseEnCirculation; }

    public int getKilometrageActuel() { return kilometrageActuel; }
    public void setKilometrageActuel(int kilometrageActuel) { this.kilometrageActuel = kilometrageActuel; }

    public TypeVehicule getTypeVehicule() { return typeVehicule; }
    public void setTypeVehicule(TypeVehicule typeVehicule) { this.typeVehicule = typeVehicule; }

    public String getProprietaire() { return proprietaire; }
    public void setProprietaire(String proprietaire) { this.proprietaire = proprietaire; }

    public java.util.List<Intervention> getInterventions() { return interventions; }
    public void setInterventions(java.util.List<Intervention> interventions) { this.interventions = interventions; }

}