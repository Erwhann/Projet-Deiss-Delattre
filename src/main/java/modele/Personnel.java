package modele;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "personnel")
@PrimaryKeyJoinColumn(name = "id")
public class Personnel extends Personne {

    private String poste;
    private double salaire;

    // ✅ PIN pour login
    private String pin;

    public Personnel() {
        super();
    }

    public Personnel(String nom,
                     String prenom,
                     String email,
                     String telephone,
                     String poste,
                     String pin) {
        super(nom, prenom, email, telephone);
        this.poste = poste;
        this.pin = pin;
    }

    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }

    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}
