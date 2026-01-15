package modele;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_vehicule")
public class TypeVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    private String energie;
    private String boite;
    private int nbPortes;
    private int nbPlaces;
    private int puissance;
    private int cvFiscaux;

    @OneToMany(mappedBy = "typeVehicule", cascade = CascadeType.ALL)
    private List<Vehicule> vehicules = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "type_vehicule_piece",
            joinColumns = @JoinColumn(name = "type_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id")
    )
    private List<Piece> piecesCompatibles = new ArrayList<>();

    // Getters et Setters
    public List<Piece> getPiecesCompatibles() { return piecesCompatibles; }
    public void setPiecesCompatibles(List<Piece> piecesCompatibles) { this.piecesCompatibles = piecesCompatibles; }

    // --- Constructeurs ---
    public TypeVehicule() {}

    public TypeVehicule(String marque, String modele, String energie, String boite, int nbPortes, int nbPlaces, int puissance, int cvFiscaux) {
        this.marque = marque;
        this.modele = modele;
        this.energie = energie;
        this.boite = boite;
        this.nbPortes = nbPortes;
        this.nbPlaces = nbPlaces;
        this.puissance = puissance;
        this.cvFiscaux = cvFiscaux;
    }

    // --- Getters et Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public String getEnergie() { return energie; }
    public void setEnergie(String energie) { this.energie = energie; }

    public String getBoite() { return boite; }
    public void setBoite(String boite) { this.boite = boite; }

    public int getNbPortes() { return nbPortes; }
    public void setNbPortes(int nbPortes) { this.nbPortes = nbPortes; }

    public int getNbPlaces() { return nbPlaces; }
    public void setNbPlaces(int nbPlaces) { this.nbPlaces = nbPlaces; }

    public int getPuissance() { return puissance; }
    public void setPuissance(int puissance) { this.puissance = puissance; }

    public int getCvFiscaux() { return cvFiscaux; }
    public void setCvFiscaux(int cvFiscaux) { this.cvFiscaux = cvFiscaux; }

    public List<Vehicule> getVehicules() { return vehicules; }
    public void setVehicules(List<Vehicule> vehicules) { this.vehicules = vehicules; }

    @Override
    public String toString() {
        return marque + " " + modele + " (" + energie + ")";
    }


}