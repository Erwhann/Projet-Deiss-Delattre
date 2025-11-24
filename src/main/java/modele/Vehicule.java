package modele;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehicule")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String immatriculation; // [cite: 231]

    @Column(unique = true)
    private String vin; // Numéro de série [cite: 230]

    @Column(name = "date_mise_circulation")
    private LocalDate dateMiseEnCirculation; // [cite: 232]

    @Column(name = "kilometrage_actuel")
    private int kilometrageActuel; // [cite: 291]

    // --- RELATIONS ---

    // Relation : Un véhicule appartient à un Type de Véhicule
    @ManyToOne
    @JoinColumn(name = "type_vehicule_id", nullable = false)
    private TypeVehicule typeVehicule;

    // Relation : Un véhicule appartient à un Client [cite: 256]
    // TODO: Décommenter quand la classe Client sera créée
    // @ManyToOne
    // @JoinColumn(name = "client_id", nullable = false)
    // private Client client;

    // Constructeur vide requis par JPA
    public Vehicule() {
    }

    public Vehicule(String immatriculation, TypeVehicule typeVehicule, int kilometrageActuel) {
        this.immatriculation = immatriculation;
        this.typeVehicule = typeVehicule;
        this.kilometrageActuel = kilometrageActuel;
    }

    // --- Getters et Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public LocalDate getDateMiseEnCirculation() { return dateMiseEnCirculation; }
    public void setDateMiseEnCirculation(LocalDate dateMiseEnCirculation) { this.dateMiseEnCirculation = dateMiseEnCirculation; }

    public int getKilometrageActuel() { return kilometrageActuel; }
    public void setKilometrageActuel(int kilometrageActuel) { this.kilometrageActuel = kilometrageActuel; }

    public TypeVehicule getTypeVehicule() { return typeVehicule; }
    public void setTypeVehicule(TypeVehicule typeVehicule) { this.typeVehicule = typeVehicule; }

    // public Client getClient() { return client; }
    // public void setClient(Client client) { this.client = client; }
}