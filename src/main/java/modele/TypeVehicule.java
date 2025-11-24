package modele;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "type_vehicule")
public class TypeVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    private String energie;
    private String boite;
    private int nbPortes;
    private int nbPlaces;
    private int puissance;

    @Column(name = "cv_fiscaux")
    private int cvFiscaux; // Important pour la formule de prix [cite: 227]

    // Relation inverse (optionnelle mais pratique) : Un type a plusieurs véhicules
    @OneToMany(mappedBy = "typeVehicule")
    private List<Vehicule> vehicules;

    public TypeVehicule() {
    }

    public TypeVehicule(String marque, String modele, int cvFiscaux) {
        this.marque = marque;
        this.modele = modele;
        this.cvFiscaux = cvFiscaux;
    }

    // --- Getters et Setters ---
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;

    }
    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }
    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getEnergie() {
        return energie;
    }

    public void setEnergie(String energie) {
        this.energie = energie;
    }

    public String getBoite() {
        return boite;
    }

    public void setBoite(String boite) {
        this.boite = boite;
    }

    public int getNbPortes() {
        return nbPortes;
    }

    public void setNbPortes(int nbPortes) {
        this.nbPortes = nbPortes;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }
    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public int getPuissance() {
        return puissance;
    }

    public void setPuissance(int puissance) {
        this.puissance = puissance;
    }

    public int getCvFiscaux() {
        return cvFiscaux;
    }

    public void setCvFiscaux(int cvFiscaux) {
        this.cvFiscaux = cvFiscaux;
    }

    public List<Vehicule> getVehicules() {
        return vehicules;
    }

    public void setVehicules(List<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    
}