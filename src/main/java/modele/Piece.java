package modele;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "piece")
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPiece;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private String nom;

    private double prix;

    public Piece() {}

    public Piece(String reference, String nom, double prix) {
        this.reference = reference;
        this.nom = nom;
        this.prix = prix;
    }

    // --- Getters ---
    public int getIdPiece() { return idPiece; }
    public String getReference() { return reference; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; } // INDISPENSABLE POUR LA FORMULE

    @Override
    public String toString() { return nom + " (" + reference + ") - " + prix + "€"; }
}