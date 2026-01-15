package modele;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "id")
public class Client extends Personne {

    @Column(name = "numero_client", unique = true, nullable = false, length = 16)
    private String numeroClient;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL)
    private List<Vehicule> vehicules = new ArrayList<>();

    public Client() {
        super();
    }

    public Client(String nom, String prenom, String email, String telephone) {
        super(nom, prenom, email, telephone);
    }

    // ✅ Génération automatique côté entité (pas de service, pas d'enum)
    @PrePersist
    private void ensureNumeroClient() {
        if (numeroClient == null || numeroClient.isBlank()) {
            // Ex: CLI-A3F92C
            String code = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            this.numeroClient = "CLI-" + code.substring(0, 6);
        }
    }

    public String getNumeroClient() { return numeroClient; }
    public void setNumeroClient(String numeroClient) { this.numeroClient = numeroClient; }

    public List<Vehicule> getVehicules() { return vehicules; }
    public void setVehicules(List<Vehicule> vehicules) { this.vehicules = vehicules; }
}
