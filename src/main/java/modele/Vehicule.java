package modele;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicule")
public class Vehicule {

    @Id
    @Column(length = 17)
    private String vin;

    @Column(length = 10, unique = true)
    private String immatriculation;

    private LocalDate dateMiseEnCirculation;

    private int kilometrageActuel;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private TypeVehicule typeVehicule;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "client_id")
    private Client proprietaire;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL)
    private List<Intervention> interventions = new ArrayList<>();

    public Vehicule() {}

    public Vehicule(String vin,
                    String immatriculation,
                    LocalDate dateMiseEnCirculation,
                    int kilometrageActuel,
                    TypeVehicule typeVehicule,
                    Client proprietaire) {
        this.vin = vin;
        this.immatriculation = immatriculation;
        this.dateMiseEnCirculation = dateMiseEnCirculation;
        this.kilometrageActuel = kilometrageActuel;
        this.typeVehicule = typeVehicule;
        this.proprietaire = proprietaire;
    }

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

    public Client getProprietaire() { return proprietaire; }
    public void setProprietaire(Client proprietaire) { this.proprietaire = proprietaire; }

    public List<Intervention> getInterventions() { return interventions; }
    public void setInterventions(List<Intervention> interventions) { this.interventions = interventions; }

    public TypeVehicule getType() { return this.typeVehicule; }
    public void setType(TypeVehicule type) { this.typeVehicule = type; }

    @Override
    public String toString() {
        if (immatriculation != null && !immatriculation.isBlank()) {
            return immatriculation + " - " +
                    typeVehicule.getMarque() + " " +
                    typeVehicule.getModele();
        }
        return vin;
    }
}
