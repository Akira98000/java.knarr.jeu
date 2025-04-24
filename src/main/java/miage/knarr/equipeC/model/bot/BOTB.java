package miage.knarr.equipeC.model.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miage.knarr.equipeC.model.CarteDestination;
import miage.knarr.equipeC.model.CarteViking;
import miage.knarr.equipeC.model.CoutExploration;
import miage.knarr.equipeC.model.Gain;
import miage.knarr.equipeC.model.Joueur;
import miage.knarr.equipeC.model.enums.CouleurBateau;
import miage.knarr.equipeC.model.enums.TypeAction;
import miage.knarr.equipeC.model.enums.TypeGain;
import miage.knarr.equipeC.model.enums.VikingCouleur;

public class BOTB extends Joueur {

    public BOTB(String type, String nom, CouleurBateau couleurBateau, int pionsRecrue, int pionsBracelet,
                int pointVictoire, int pointRenomme) {
        super(type, nom, couleurBateau, pionsRecrue, pionsBracelet, pointVictoire, pointRenomme);
    }

    @Override
    public TypeAction choisirAction() {
        List<TypeAction> actionsDisponibles = choisirActionsDuTour();

        if (!actionsDisponibles.isEmpty()) {
            TypeAction actionPrincipale;

            if (actionsDisponibles.contains(TypeAction.EXPLORER) && canAcquireDestination()) {
                actionPrincipale = TypeAction.EXPLORER;
            } else if (actionsDisponibles.contains(TypeAction.RECRUTER)) {
                actionPrincipale = TypeAction.RECRUTER;
            } else {
                actionPrincipale = actionsDisponibles.get(0);
            }

            if (actionPrincipale == TypeAction.RECRUTER || actionPrincipale == TypeAction.EXPLORER) {
                actionPrincipaleEffectuee = true;
            }
            return actionPrincipale;
        }
        return null;
    }

    @Override
    public List<TypeAction> choisirActionsDuTour() {
        List<TypeAction> actionsDuTour = new ArrayList<>();
        aCommerceCeTour = false;
        System.out.println("Points de victoire de " + nom + " : " + pointVictoire);
        System.out.println("Points de renommée de " + nom + " : " + pointRenomme);

        if (shouldTrade()) {
            deciderEtEffectuerCommerce();
        }

        TypeAction actionPrincipale = choisirActionPrincipale();
        actionsDuTour.add(actionPrincipale);

        return actionsDuTour;
    }

    @Override
    public CarteViking choisirCarteEquipage() {
        if (zoneEquipage.isEmpty()) {
            return null;
        }
        List<CarteViking> allCards = new ArrayList<>();
        for (ArrayList<CarteViking> cartes : zoneEquipage.values()) {
            allCards.addAll(cartes);
        }

        if (allCards.isEmpty()) {
            return null;
        }

        CarteViking bestCard = null;
        int maxGainValue = -1;
        for (CarteViking carte : allCards) {
            Gain gain = carte.getGain();
            int gainValue = evaluateGain(gain);
            if (gainValue > maxGainValue) {
                maxGainValue = gainValue;
                bestCard = carte;
            }
        }
        if (bestCard == null) {
            return allCards.get(0);
        }
        return bestCard;
    }

    @Override
    public void deciderEtEffectuerCommerce() {
        System.out.println(nom + " vérifie s'il peut commercer ce tour.");
        if (!aCommerceCeTour && shouldTrade()) {
            System.out.println(nom + " peut commercer et n'a pas encore commercé ce tour.");
            commercer();
            aCommerceCeTour = true;
            System.out.println(nom + " a décidé de commercer.");
        } else {
            System.out.println(nom + " ne peut pas commercer ou a déjà commercé ce tour.");
        }
    }

    @Override
    public TypeAction choisirActionPrincipale() {
        if (canAcquireDestination()) {
            return TypeAction.EXPLORER;
        } else {
            return TypeAction.RECRUTER;
        }
    }

    @Override
    public void recruter() {
        if (cartesVikingMain.isEmpty()) {
            System.out.println(nom + " n'a pas de cartes Viking en main pour recruter.");
            return;
        }

        CarteViking carteViking = choisirMeilleureCarteVikingMain();

        System.out.println(nom + " veut échanger sa carte : " + carteViking
                + " avec une carte Viking sur le plateau.");
        CarteViking nouvelleCarte = jeu.getPlateau().getEmplacementViking()
                .get(carteViking.getCouleur());

        boolean aUtilisePionRecrue = false;
        VikingCouleur couleurEmplacement = null;

        if (nouvelleCarte != null) {
            if (pionsRecrue > 0) {
                List<CarteViking> cartesDisponibles = new ArrayList<>(
                        jeu.getPlateau().getEmplacementViking().values());
                cartesDisponibles.remove(nouvelleCarte);
                if (!cartesDisponibles.isEmpty()) {
                    nouvelleCarte = choisirMeilleureCarteVikingPlateau(cartesDisponibles);
                    retirerPionsRecrue(1);
                    aUtilisePionRecrue = true;
                    System.out.println(nom + " a utilisé un pion recrue pour choisir une autre carte : "
                            + nouvelleCarte);
                }
            }
            for (Map.Entry<VikingCouleur, CarteViking> entry : jeu.getPlateau()
                    .getEmplacementViking().entrySet()) {
                if (entry.getValue().equals(nouvelleCarte)) {
                    couleurEmplacement = entry.getKey();
                    break;
                }
            }
            if (couleurEmplacement == null) {
                System.out.println("Erreur : Impossible de déterminer la couleur de l'emplacement sur le plateau.");
                return;
            }
            System.out.println(nom + " a trouvé la carte sur le plateau : " + nouvelleCarte);
            retirerCarteVikingMain(carteViking);
            ajouterCarteVikingEquipage(carteViking);
            System.out.println(nom + " retire la carte : " + carteViking);
            ArrayList<CarteViking> colonne = zoneEquipage.get(carteViking.getCouleur());
            for (CarteViking carte : colonne) {
                Gain gain = carte.getGain();
                appliquerGain(gain);
                System.out.println(nom + " a appliqué le gain : " + gain + " grâce à " + carte);
            }

            ajouterCarteVikingMain(nouvelleCarte);
            System.out.println(nom + " a ajouté la nouvelle carte à sa main : " + nouvelleCarte);
            jeu.getPlateau().rafraichirVikingEmplacement(couleurEmplacement);
        } else {
            System.out.println("Aucune carte Viking disponible sur le plateau pour la couleur "
                    + carteViking.getCouleur());
        }

        if (aUtilisePionRecrue) {
            System.out.println(nom + " a utilisé un pion recrue pour cette action.");
        }
    }

    @Override
    public void explorer() {
        if (jeu.getPlateau().getEmplacementCarteDest().isEmpty()) {
            System.out.println("Aucune destination disponible pour explorer.");
            return;
        }
        List<CarteDestination> disponibles = jeu.getPlateau().getEmplacementCarteDest();
        List<CarteDestination> cartesPossibles = new ArrayList<>();
        for (CarteDestination destination : disponibles) {
            if (peutAcquerirCarteDestination(destination)) {
                cartesPossibles.add(destination);
            }
        }
        if (cartesPossibles.isEmpty()) {
            System.out.println(nom + " ne peut acquérir aucune carte destination disponible.");
            return;
        }
        CarteDestination destinationChoisie = choisirMeilleureDestination(cartesPossibles);
        System.out.println(nom + " souhaite explorer la destination: " + destinationChoisie.getId());
        if (acquerirCarteDestination(destinationChoisie)) {
            for (Map.Entry<TypeGain, Integer> entry : destinationChoisie.getGains().entrySet()) {
                Gain gain = new Gain(entry.getKey(), entry.getValue());
                appliquerGain(gain, entry.getValue());
            }
            jeu.getPlateau().rafraichirCarteDestination(destinationChoisie);
        } else {
            System.out.println(
                    nom + " n'a pas pu acquérir la carte Destination: " + destinationChoisie.getId());
        }
    }

    @Override
    public void commercer() {
        System.out.println(nom + " essaie de commercer.");
        if (!peutCommercer()) {
            System.out.println(nom + " ne peut pas commercer ce tour.");
            return;
        }

        int maxBraceletsToUse = Math.min(pionsBracelet, 3);
        int nombreBraceletsUtilises = decideNombreBraceletsPourCommerce(maxBraceletsToUse);

        retirerPionsBracelet(nombreBraceletsUtilises);
        System.out.println(nom + " utilise " + nombreBraceletsUtilises + " bracelets pour commercer.");

        for (CarteDestination destination : cartesDestination) {
            System.out.println("Détails de la carte destination: " + destination);
            HashMap<Integer, HashMap<TypeGain, Integer>> gainCommerce = destination.getGainCommerce();
            System.out.println(nom + " obtient des gains de la carte destination: " + destination.getId());

            for (int i = 1; i <= nombreBraceletsUtilises; i++) {
                if (gainCommerce.containsKey(i)) {
                    for (Map.Entry<TypeGain, Integer> entry : gainCommerce.get(i).entrySet()) {
                        System.out.println("Gain obtenu: " + entry.getKey() + " - Quantité: " + entry.getValue());
                        appliquerGain(new Gain(entry.getKey(), entry.getValue()), entry.getValue());
                    }
                }
            }
        }
        this.aCommerceCeTour = true;
    }

    @Override
    public boolean peutAcquerirCarteDestination(CarteDestination carteDestination) {
        CoutExploration cout = carteDestination.getCoutExploration().get(0);
        List<VikingCouleur> requisList = cout.getCouleur();
        List<Integer> quantitesList = cout.getNombres();

        if (requisList.size() != quantitesList.size()) {
            return false;
        }

        Map<VikingCouleur, Integer> requis = new HashMap<>();
        for (int i = 0; i < requisList.size(); i++) {
            VikingCouleur couleur = requisList.get(i);
            int quantite = quantitesList.get(i);
            requis.put(couleur, requis.getOrDefault(couleur, 0) + quantite);
        }

        int pionsRecrueRestants = pionsRecrue;

        for (Map.Entry<VikingCouleur, Integer> entry : requis.entrySet()) {
            VikingCouleur couleur = entry.getKey();
            int quantiteRequise = entry.getValue();

            if (couleur == VikingCouleur.COULEURANY) {
                int totalVikings = 0;
                for (VikingCouleur c : VikingCouleur.values()) {
                    if (c != VikingCouleur.COULEURANY && c != VikingCouleur.COULEURDIFF) {
                        List<CarteViking> equipageCouleur = zoneEquipage.getOrDefault(c, new ArrayList<>());
                        totalVikings += equipageCouleur.size();
                    }
                }
                if (totalVikings < quantiteRequise) {
                    int manque = quantiteRequise - totalVikings;
                    if (pionsRecrueRestants >= manque) {
                        pionsRecrueRestants -= manque;
                    } else {
                        return false;
                    }
                }
            } else if (couleur == VikingCouleur.COULEURDIFF) {
                int differentColors = 0;
                for (VikingCouleur c : VikingCouleur.values()) {
                    if (c != VikingCouleur.COULEURANY && c != VikingCouleur.COULEURDIFF) {
                        List<CarteViking> equipageCouleur = zoneEquipage.getOrDefault(c, new ArrayList<>());
                        if (!equipageCouleur.isEmpty()) {
                            differentColors++;
                        }
                    }
                }
                if (differentColors < quantiteRequise) {
                    int manque = quantiteRequise - differentColors;
                    if (pionsRecrueRestants >= manque) {
                        pionsRecrueRestants -= manque;
                    } else {
                        return false;
                    }
                }
            } else {
                List<CarteViking> equipageCouleur = zoneEquipage.getOrDefault(couleur, new ArrayList<>());
                int count = equipageCouleur.size();

                if (count < quantiteRequise) {
                    int manque = quantiteRequise - count;
                    if (pionsRecrueRestants >= manque) {
                        pionsRecrueRestants -= manque;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean acquerirCarteDestination(CarteDestination carteDestination) {
        System.out.println("\n=== Début acquisition carte destination " + carteDestination.getId() + " ===");

        if (!peutAcquerirCarteDestination(carteDestination)) {
            System.out.println("Échec: Ressources insuffisantes pour acquérir la destination.");
            return false;
        }

        CoutExploration cout = carteDestination.getCoutExploration().get(0);
        List<VikingCouleur> requisList = cout.getCouleur();
        List<Integer> quantitesList = cout.getNombres();

        Map<VikingCouleur, Integer> requis = new HashMap<>();
        for (int i = 0; i < requisList.size(); i++) {
            VikingCouleur couleur = requisList.get(i);
            int quantite = quantitesList.get(i);
            requis.put(couleur, requis.getOrDefault(couleur, 0) + quantite);
        }

        int pionsRecrueUtilises = 0;
        System.out.println("\nDéfausse des cartes utilisées:");

        for (Map.Entry<VikingCouleur, Integer> entry : requis.entrySet()) {
            VikingCouleur couleur = entry.getKey();
            int quantiteRequise = entry.getValue();

            if (couleur == VikingCouleur.COULEURANY) {
                int quantiteRestante = quantiteRequise;

                List<VikingCouleur> couleursPossedees = new ArrayList<>();
                for (VikingCouleur c : VikingCouleur.values()) {
                    if (c != VikingCouleur.COULEURANY && c != VikingCouleur.COULEURDIFF) {
                        ArrayList<CarteViking> equipageCouleur = zoneEquipage.getOrDefault(c,
                                new ArrayList<>());
                        if (!equipageCouleur.isEmpty()) {
                            couleursPossedees.add(c);
                        }
                    }
                }

                for (VikingCouleur c : couleursPossedees) {
                    if (quantiteRestante <= 0)
                        break;

                    ArrayList<CarteViking> equipageCouleur = zoneEquipage.getOrDefault(c,
                            new ArrayList<>());
                    int quantiteDisponible = equipageCouleur.size();
                    int cartesADefausser = Math.min(quantiteDisponible, quantiteRestante);

                    if (cartesADefausser > 0) {
                        List<CarteViking> cartesASupprimer = new ArrayList<>(
                                equipageCouleur.subList(0, cartesADefausser));
                        System.out.println("- Défausse de " + cartesASupprimer.size() + " carte(s) " + c);
                        equipageCouleur.removeAll(cartesASupprimer);
                        for (CarteViking carte : cartesASupprimer) {
                            jeu.ajouterCarteVikingDefausse(carte);
                        }
                        quantiteRestante -= cartesADefausser;
                    }
                }

                if (quantiteRestante > 0) {
                    System.out.println("  → Utilisation de " + quantiteRestante
                            + " pion(s) de recrutement pour compenser le manque");
                    pionsRecrueUtilises += quantiteRestante;
                }

            } else if (couleur == VikingCouleur.COULEURDIFF) {
                int quantiteRestante = quantiteRequise;

                for (VikingCouleur c : VikingCouleur.values()) {
                    if (c != VikingCouleur.COULEURANY && c != VikingCouleur.COULEURDIFF
                            && quantiteRestante > 0) {
                        ArrayList<CarteViking> equipageCouleur = zoneEquipage.getOrDefault(c,
                                new ArrayList<>());
                        if (!equipageCouleur.isEmpty()) {
                            CarteViking carteASupprimer = equipageCouleur.remove(0);
                            System.out.println("- Défausse de 1 carte " + c);
                            jeu.ajouterCarteVikingDefausse(carteASupprimer);
                            quantiteRestante--;
                        }
                    }
                }

                if (quantiteRestante > 0) {
                    System.out.println("  → Utilisation de " + quantiteRestante
                            + " pion(s) de recrutement pour compenser le manque en couleurs différentes");
                    pionsRecrueUtilises += quantiteRestante;
                }

            } else {
                ArrayList<CarteViking> equipageCouleur = zoneEquipage.getOrDefault(couleur,
                        new ArrayList<>());
                int quantiteDisponible = equipageCouleur.size();

                int cartesADefausser = Math.min(quantiteDisponible, quantiteRequise);
                int manque = quantiteRequise - cartesADefausser;

                if (cartesADefausser > 0) {
                    List<CarteViking> cartesASupprimer = new ArrayList<>(
                            equipageCouleur.subList(0, cartesADefausser));
                    System.out.println("- Défausse de " + cartesASupprimer.size() + " carte(s) " + couleur);
                    equipageCouleur.removeAll(cartesASupprimer);
                    for (CarteViking carte : cartesASupprimer) {
                        jeu.ajouterCarteVikingDefausse(carte);
                    }
                }
                if (manque > 0) {
                    System.out.println("  → Utilisation de " + manque + " pion(s) de recrutement pour compenser le manque en " + couleur);
                    pionsRecrueUtilises += manque;
                }
            }
        }

        utiliserPionsRecrut(pionsRecrueUtilises);
        cartesDestination.add(carteDestination);
        System.out.println("\nSuccès: " + nom + " a acquis la carte Destination " + carteDestination.getId());
        System.out.println("=== Fin acquisition (succès) ===\n");
        return true;
    }

    @Override
    public void utiliserPionsRecrut(int montant) {
        if (pionsRecrue >= montant) {
            pionsRecrue -= montant;
            System.out.println(nom + " a utilisé " + montant + " pion(s) de recrutement.");
        } else {
            System.out.println(nom + " n'a pas assez de pions de recrutement.");
        }
    }


    // NEW METHODS

    private boolean canAcquireDestination() {
        for (CarteDestination destination : jeu.getPlateau().getEmplacementCarteDest()) {
            if (peutAcquerirCarteDestination(destination)) {
                return true;
            }
        }
        return false;
    }

    private CarteViking choisirMeilleureCarteVikingMain() {
        return getCarteViking(cartesVikingMain);
    }

    private boolean shouldTrade() {
        return peutCommercer() && !aCommerceCeTour;
    }

    private int evaluateGain(Gain gain) {
        return switch (gain.getType()) {
            case POINT -> gain.getValeur();
            case RENOMME -> gain.getValeur() * 2;
            case RECRUE -> gain.getValeur() * 3;
            case BRACELET -> gain.getValeur() * 4;
            case CARTE_VIKING -> gain.getValeur() * 5;
        };
    }

    private CarteViking choisirMeilleureCarteVikingPlateau(List<CarteViking> cartesDisponibles) {
        return getCarteViking(cartesDisponibles);
    }

    private CarteViking getCarteViking(List<CarteViking> cartesDisponibles) {
        CarteViking meilleureCarte = null;
        int valeurMax = -1;
        for (CarteViking carte : cartesDisponibles) {
            int valeur = evaluateGain(carte.getGain());
            if (valeur > valeurMax) {
                valeurMax = valeur;
                meilleureCarte = carte;
            }
        }
        return meilleureCarte;
    }

    private CarteDestination choisirMeilleureDestination(List<CarteDestination> cartesPossibles) {
        CarteDestination meilleureDestination = null;
        int valeurMax = -1;
        for (CarteDestination destination : cartesPossibles) {
            int valeur = 0;
            for (Map.Entry<TypeGain, Integer> entry : destination.getGains().entrySet()) {
                valeur += evaluateGain(new Gain(entry.getKey(), entry.getValue()));
            }
            if (valeur > valeurMax) {
                valeurMax = valeur;
                meilleureDestination = destination;
            }
        }
        return meilleureDestination;
    }

    private int decideNombreBraceletsPourCommerce(int maxBraceletsToUse) {
        // Logique simple pour décider du nombre de bracelets à utiliser
        // Ici, on utilise toujours le maximum de bracelets disponibles
        return maxBraceletsToUse;
    }
}