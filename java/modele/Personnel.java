package modele;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personnel")
@PrimaryKeyJoinColumn(name = "id")
public class Personnel extends Personne {

    private String role; [cite_start]// Mecanicien, Accueil, etc. [cite: 486]

    // Relation : Un membre du personnel gère plusieurs interventions
    @OneToMany(mappedBy = "personnel", cascade = CascadeType.ALL)
    private List<Intervention> interventionsGerees = new ArrayList<>();

    // --- Constructeurs ---
    public Personnel() {}

    public Personnel(String nom, String prenom, String role) {
        super(nom, prenom);
        this.role = role;
    }

    // --- Getters et Setters ---
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Intervention> getInterventionsGerees() { return interventionsGerees; }
    public void setInterventionsGerees(List<Intervention> interventionsGerees) { this.interventionsGerees = interventionsGerees; }
}