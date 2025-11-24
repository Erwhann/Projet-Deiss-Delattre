package view;

import javax.swing.*;
import java.awt.*;

public class AjoutVehiculePanel extends JPanel {

    private JTextField vinField, immatField, kmField, puissanceField, dateField, proprietaireField, mailField, telField;
    private JComboBox<String> marqueBox, modeleBox, energieBox;
    private JSpinner nbPlacesSpinner, nbPortesSpinner;

    public AjoutVehiculePanel() {
        setLayout(new GridLayout(1, 3, 10, 10));

        // --- Colonne 1 : Infos générales ---
        JPanel infosGenerales = new JPanel(new GridLayout(8, 1, 5, 5));
        infosGenerales.setBorder(BorderFactory.createTitledBorder("Ajout d’un véhicule"));

        marqueBox = new JComboBox<>(new String[]{"Renault", "Peugeot", "BMW"});
        modeleBox = new JComboBox<>(new String[]{"Clio", "208", "X5"});
        energieBox = new JComboBox<>(new String[]{"Essence", "Diesel", "Électrique", "Hybride"});
        nbPlacesSpinner = new JSpinner(new SpinnerNumberModel(5, 2, 9, 1));
        nbPortesSpinner = new JSpinner(new SpinnerNumberModel(5, 2, 5, 1));
        puissanceField = new JTextField();

        infosGenerales.add(new JLabel("Marque :"));
        infosGenerales.add(marqueBox);
        infosGenerales.add(new JLabel("Modèle :"));
        infosGenerales.add(modeleBox);
        infosGenerales.add(new JLabel("Énergie :"));
        infosGenerales.add(energieBox);
        infosGenerales.add(new JLabel("Puissance fiscale :"));
        infosGenerales.add(puissanceField);

        // --- Colonne 2 : Infos véhicule ---
        JPanel infosVehicule = new JPanel(new GridLayout(6, 1, 5, 5));
        infosVehicule.setBorder(BorderFactory.createTitledBorder("Informations du véhicule"));

        vinField = new JTextField();
        immatField = new JTextField();
        dateField = new JTextField();
        kmField = new JTextField();

        infosVehicule.add(new JLabel("Numéro VIN :"));
        infosVehicule.add(vinField);
        infosVehicule.add(new JLabel("Immatriculation :"));
        infosVehicule.add(immatField);
        infosVehicule.add(new JLabel("Date mise en circulation (AAAA-MM-JJ) :"));
        infosVehicule.add(dateField);
        infosVehicule.add(new JLabel("Kilométrage :"));
        infosVehicule.add(kmField);

        // --- Colonne 3 : Propriétaire ---
        JPanel infosClient = new JPanel(new GridLayout(5, 1, 5, 5));
        infosClient.setBorder(BorderFactory.createTitledBorder("Informations propriétaire"));

        proprietaireField = new JTextField();
        mailField = new JTextField();
        telField = new JTextField();

        infosClient.add(new JLabel("Propriétaire :"));
        infosClient.add(proprietaireField);
        infosClient.add(new JLabel("Téléphone :"));
        infosClient.add(telField);
        infosClient.add(new JLabel("Email :"));
        infosClient.add(mailField);

        JButton ajouterBtn = new JButton("Ajouter");
        infosClient.add(ajouterBtn);

        // --- Ajout des colonnes au panel principal ---
        add(infosGenerales);
        add(infosVehicule);
        add(infosClient);
    }
}
