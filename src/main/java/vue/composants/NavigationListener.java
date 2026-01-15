package vue.composants;

import modele.Vehicule;

/**
 * Interface permettant de naviguer entre les vues
 * tout en passant des données (le véhicule sélectionné).
 */
public interface NavigationListener {
    void afficherDetailsVehicule(Vehicule v);

    /**
     * Ouvre le panel d'ajout d'intervention en pré-sélectionnant le véhicule.
     */
    void ouvrirAjoutIntervention(Vehicule v);
}