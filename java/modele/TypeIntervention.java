package modele;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_intervention")
public class TypeIntervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; [cite_start]// [cite: 464]

    @Column(nullable = false)
    private String nom; [cite_start]// [cite: 466]

    private String description; [cite_start]// [cite: 465]

    @ManyToMany
    @JoinTable(
            name = "type_intervention_piece",
            joinColumns = @JoinColumn(name = "type_intervention_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id")
    )
    private List<Piece> piecesNecessaires = new ArrayList<>();

    // --- Constructeurs ---
    public TypeIntervention() {}

    public TypeIntervention(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // --- Getters et Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Piece> getPiecesNecessaires() { return piecesNecessaires; }
    public void setPiecesNecessaires(List<Piece> piecesNecessaires) { this.piecesNecessaires = piecesNecessaires; }

    @Override
    public String toString() { return nom; }
}