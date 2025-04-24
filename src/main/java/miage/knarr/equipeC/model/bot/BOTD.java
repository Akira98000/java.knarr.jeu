package miage.knarr.equipeC.model.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miage.knarr.equipeC.model.CarteDestination;
import miage.knarr.equipeC.model.CarteViking;
import miage.knarr.equipeC.model.CoutExploration;
import miage.knarr.equipeC.model.Gain;
import miage.knarr.equipeC.model.Jeu;
import miage.knarr.equipeC.model.Joueur;
import miage.knarr.equipeC.model.enums.CouleurBateau;
import miage.knarr.equipeC.model.enums.TypeAction;
import miage.knarr.equipeC.model.enums.TypeGain;
import miage.knarr.equipeC.model.enums.VikingCouleur;



public class BOTD extends Joueur {

    private static final int SIMULATION_NUMBER = 1000; 
    private static final double EXPLORATION_CONSTANT = 1;

    public BOTD(String type, String nom, CouleurBateau couleurBateau, int pionsRecrue, int pionsBracelet,
            int pointVictoire, int pointRenomme) {
        super(type, nom, couleurBateau, pionsRecrue, pionsBracelet, pointVictoire, pointRenomme);
    }

    @Override
    public TypeAction choisirAction() {
        Node rootNode = new Node(null, this.jeu.clone(), this.clone(), null);

        for (int i = 0; i < SIMULATION_NUMBER; i++) {
            Node selectedNode = rootNode.select();
            int simulationResult = selectedNode.simulate();
            selectedNode.backpropagate(simulationResult);
        }

        Node bestChild = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        for (Node child : rootNode.children) {
            double childValue = (child.visitCount == 0) ? 0 : (child.winScore / child.visitCount);
            if (childValue > bestValue) {
                bestValue = childValue;
                bestChild = child;
            }
        }

        if (bestChild != null && bestChild.action != null) {
            System.out.println("\033[32m" + this.nom + " (BOTD) a choisi l'action via MCTS: " + bestChild.action + "\033[0m");
            return bestChild.action;
        } else {
            System.out.println("\033[32m" + this.nom + " (BOTD) n'a pas pu choisir d'action via MCTS, action par défaut.\033[0m");
            return choisirActionPrincipale();
        }
    }

    @Override
    public List<TypeAction> choisirActionsDuTour() {
        List<TypeAction> actionsDuTour = new ArrayList<>();
        aCommerceCeTour = false;

        if (peutCommercer()) {
            deciderEtEffectuerCommerce();
        }

        TypeAction actionPrincipale = choisirAction();
        actionsDuTour.add(actionPrincipale);

        return actionsDuTour;
    }

    @Override
    public void deciderEtEffectuerCommerce() {
        if (!aCommerceCeTour && peutCommercer()) {
            commercer();
            aCommerceCeTour = true;
        }
    }

    @Override
    public TypeAction choisirActionPrincipale() {
        return TypeAction.EXPLORER;
    }

    @Override
    public void recruter() {
        if (cartesVikingMain.isEmpty()) {
            System.out.println("\033[32m" + nom + " (BOTD) n'a pas de cartes Viking en main pour recruter.\033[0m");
            return;
        }

        CarteViking carteViking = choisirMeilleureCarteVikingMain();
        if (carteViking == null) {
            System.out.println("\033[32m" + nom + " (BOTD) ne trouve pas de carte viking à recruter.\033[0m");
            return;
        }

        System.out.println("\033[32m" + nom + " (BOTD) veut échanger sa carte : " + carteViking + "\033[0m");

        CarteViking cartePlateau = jeu.getPlateau().getEmplacementViking().get(carteViking.getCouleur());

        boolean aUtilisePionRecrue = false;
        VikingCouleur couleurEmplacement = null;

        if (cartePlateau != null) {
            if (pionsRecrue > 0) {
                List<CarteViking> cartesDisponibles = new ArrayList<>(jeu.getPlateau().getEmplacementViking().values());
                if (!cartesDisponibles.isEmpty()) {
                    CarteViking meilleureCartePlateau = choisirMeilleureCarteVikingPlateau(cartesDisponibles);
                    if (meilleureCartePlateau != null && !meilleureCartePlateau.equals(cartePlateau)) {
                        retirerPionsRecrue(1);
                        aUtilisePionRecrue = true;
                        cartePlateau = meilleureCartePlateau;
                        System.out.println("\033[32m" + nom + " (BOTD) a utilisé un pion recrue pour choisir une autre carte : " + meilleureCartePlateau + "\033[0m");
                    }
                }
            }

            for (Map.Entry<VikingCouleur, CarteViking> entry : jeu.getPlateau().getEmplacementViking().entrySet()) {
                if (entry.getValue().equals(cartePlateau)) {
                    couleurEmplacement = entry.getKey();
                    break;
                }
            }

            if (couleurEmplacement == null) {
                System.out.println("Erreur : Impossible de déterminer la couleur de l'emplacement sur le plateau.");
                return;
            }

            retirerCarteVikingMain(carteViking);
            ajouterCarteVikingEquipage(carteViking);

            Gain gain = carteViking.getGain();
            if (gain != null) {
                appliquerGain(gain);
            }

            ajouterCarteVikingMain(cartePlateau);
            jeu.getPlateau().rafraichirVikingEmplacement(couleurEmplacement);

            System.out.println("\033[32m" + nom + " (BOTD) a recruté et échangé une carte Viking.\033[0m");

            if (aUtilisePionRecrue) {
                System.out.println(nom + " (BOTD) a utilisé un pion recrue pour cette action.");
            }
        } else {
            System.out.println("\033[32m" + nom + " (BOTD) ne peut pas recruter car aucune carte correspondante sur le plateau.\033[0m");
        }
    }

    @Override
    public void explorer() {
        List<CarteDestination> destinationsDisponibles = jeu.getPlateau().getEmplacementCarteDest();
        if (destinationsDisponibles.isEmpty()) {
            System.out.println("\033[32m" + nom + " (BOTD) Aucune destination disponible pour explorer.\033[0m");
            return;
        }

        List<CarteDestination> cartesPossibles = new ArrayList<>();
        for (CarteDestination destination : destinationsDisponibles) {
            if (peutAcquerirCarteDestination(destination)) {
                cartesPossibles.add(destination);
            }
        }

        if (cartesPossibles.isEmpty()) {
            System.out.println("\033[32m" + nom + " (BOTD) ne peut acquérir aucune carte destination.\033[0m");
            return;
        }

        // Choisir la meilleure destination (renommée fortement valorisée)
        CarteDestination destinationChoisie = choisirMeilleureDestination(cartesPossibles);
        if (destinationChoisie == null) {
            destinationChoisie = cartesPossibles.get(0);
        }

        System.out.println("\033[32m" + nom + " (BOTD) souhaite explorer la destination: " + destinationChoisie.getId() + "\033[0m");
        if (acquerirCarteDestination(destinationChoisie)) {
            for (Map.Entry<TypeGain, Integer> entry : destinationChoisie.getGain().entrySet()) {
                Gain gain = new Gain(entry.getKey(), entry.getValue());
                appliquerGain(gain, entry.getValue());
            }
            jeu.getPlateau().rafraichirCarteDestination(destinationChoisie);
        } else {
            System.out.println("\033[32m" + nom + " (BOTD) n'a pas pu acquérir la carte Destination: " + destinationChoisie.getId() + "\033[0m");
        }
    }

    @Override
    public void commercer() {
        System.out.println("\033[32m" + nom + " (BOTD) essaie de commercer.\033[0m");
        if (!peutCommercer()) {
            System.out.println("\033[32m" + nom + " (BOTD) ne peut pas commercer ce tour.\033[0m");
            return;
        }

        int maxBraceletsToUse = Math.min(pionsBracelet, 3);
        int nombreBraceletsUtilises = maxBraceletsToUse;

        retirerPionsBracelet(nombreBraceletsUtilises);
        System.out.println("\033[32m" + nom + " (BOTD) utilise " + nombreBraceletsUtilises + " bracelets pour commercer.\033[0m");

        for (CarteDestination destination : cartesDestination) {
            HashMap<Integer, HashMap<TypeGain, Integer>> gainCommerce = destination.getGainCommerce();
            for (int i = 1; i <= nombreBraceletsUtilises; i++) {
                if (gainCommerce.containsKey(i)) {
                    for (Map.Entry<TypeGain, Integer> entry : gainCommerce.get(i).entrySet()) {
                        appliquerGain(new Gain(entry.getKey(), entry.getValue()), entry.getValue());
                    }
                }
            }
        }
        this.aCommerceCeTour = true;
    }

    @Override
    public CarteViking choisirCarteEquipage() {
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
        cartesDestination.add(carteDestination);

        HashMap<TypeGain, Integer> gains = carteDestination.getGain();
        if (gains != null) {
            for (Map.Entry<TypeGain, Integer> entry : gains.entrySet()) {
                appliquerGain(new Gain(entry.getKey(), entry.getValue()));
            }
        }

        jeu.getPlateau().rafraichirCarteDestination(carteDestination);
        System.out.println("\033[32m" + nom + " (BOTD) a acquis la carte destination " + carteDestination.getId() + ".\033[0m");
        return true;
    }

    @Override
    public void utiliserPionsRecrut(int montant) {
        if (pionsRecrue >= montant) {
            pionsRecrue -= montant;
            System.out.println("\033[32m" + nom + " (BOTD) a utilisé " + montant + " pion(s) de recrutement.\033[0m");
        } else {
            System.out.println("\033[32m" + nom + " (BOTD) n'a pas assez de pions de recrutement.\033[0m");
        }
    }

    private int evaluateGain(Gain gain) {
        if (gain == null) return 0;
        return switch (gain.getType()) {
            case POINT -> gain.getValeur();
            case RENOMME -> gain.getValeur() * 40;
            case RECRUE -> gain.getValeur() * 1;
            case BRACELET -> gain.getValeur() * 1;
            case CARTE_VIKING -> gain.getValeur() * 1;
        };
    }

    private CarteViking choisirMeilleureCarteVikingMain() {
        return getCarteViking(this.cartesVikingMain);
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
            for (Map.Entry<TypeGain, Integer> entry : destination.getGain().entrySet()) {
                valeur += evaluateGain(new Gain(entry.getKey(), entry.getValue()));
            }
            if (valeur > valeurMax) {
                valeurMax = valeur;
                meilleureDestination = destination;
            }
        }
        return meilleureDestination;
    }


    private class Node {
        private final Node parent;
        private final List<Node> children;
        private final Jeu gameState;
        private final Joueur currentPlayer;
        private final TypeAction action;
        private int visitCount;
        private double winScore;

        public Node(Node parent, Jeu gameState, Joueur currentPlayer, TypeAction action) {
            this.parent = parent;
            this.gameState = gameState;
            this.currentPlayer = currentPlayer;
            this.action = action;
            this.children = new ArrayList<>();
            this.visitCount = 0;
            this.winScore = 0;
        }

        public Node select() {
            Node currentNode = this;
            while (!currentNode.children.isEmpty()) {
                currentNode = currentNode.getBestChild();
            }
            if (currentNode.visitCount > 0 && !currentNode.isTerminal()) {
                currentNode.expand();
                if (!currentNode.children.isEmpty()) {
                    currentNode = currentNode.children.get(0);
                }
            }
            return currentNode;
        }

        public void expand() {
            List<TypeAction> possibleActions = getPossibleActions(currentPlayer, gameState);
            for (TypeAction action : possibleActions) {
                Jeu clonedGameState = cloneGameState(gameState);
                Joueur clonedPlayer = clonePlayer(currentPlayer);
                simulateAction(clonedPlayer, clonedGameState, action);
                Node childNode = new Node(this, clonedGameState, clonedPlayer, action);
                children.add(childNode);
            }
        }

        public int simulate() {
            Jeu simulationGameState = cloneGameState(gameState);
            Joueur simulationPlayer = clonePlayer(currentPlayer);

            int simulationDepth = 0;
            int maxSimulationDepth = 40;

            while (simulationDepth < maxSimulationDepth && !isTerminal(simulationGameState, simulationPlayer)) {
                List<TypeAction> possibleActions = getPossibleActions(simulationPlayer, simulationGameState);
                if (possibleActions.isEmpty()) {
                    break;
                }
                TypeAction randomAction = getRandomAction(possibleActions);
                simulateAction(simulationPlayer, simulationGameState, randomAction);
                simulationDepth++;
            }

            return simulationPlayer.getPointRenomme();
        }

        public void backpropagate(int result) {
            visitCount++;
            winScore += result;
            if (parent != null) {
                parent.backpropagate(result);
            }
        }

        public Node getBestChild() {
            Node bestChild = null;
            double bestValue = Double.NEGATIVE_INFINITY;
            for (Node child : children) {
                double uctValue;
                if (child.visitCount == 0) {
                    uctValue = Double.MAX_VALUE;
                } else {
                    double exploitation = child.winScore / child.visitCount;
                    double exploration = EXPLORATION_CONSTANT * Math.sqrt(Math.log(this.visitCount + 1) / child.visitCount);
                    uctValue = exploitation + exploration;
                }

                if (uctValue > bestValue) {
                    bestValue = uctValue;
                    bestChild = child;
                }
            }
            return bestChild;
        }

        public boolean isTerminal() {
            return gameState.isFinalRound() || currentPlayer.getPointVictoire() >= 40;
        }

        private boolean isTerminal(Jeu gameState, Joueur currentPlayer) {
            return gameState.isFinalRound() || currentPlayer.getPointVictoire() >= 40;
        }

        private List<TypeAction> getPossibleActions(Joueur joueur, Jeu gameState) {
            List<TypeAction> actions = new ArrayList<>();
            if (!joueur.getCartesVikingMain().isEmpty()) {
                actions.add(TypeAction.RECRUTER);
            }
            if (!gameState.getPlateau().getEmplacementCarteDest().isEmpty()) {
                actions.add(TypeAction.EXPLORER);
            }
            return actions;
        }

        private TypeAction getRandomAction(List<TypeAction> possibleActions) {
            if (possibleActions.isEmpty()) {
                return null;
            }
            int index = (int) (Math.random() * possibleActions.size());
            return possibleActions.get(index);
        }

        private void simulateAction(Joueur joueur, Jeu gameState, TypeAction action) {
            if (action == null) return;
            switch (action) {
                case RECRUTER:
                    simulateRecruter(joueur, gameState);
                    break;
                case EXPLORER:
                    simulateExplorer(joueur, gameState);
                    break;
                default:
                    break;
            }
        }

        private void simulateRecruter(Joueur joueur, Jeu gameState) {
            if (joueur.getCartesVikingMain().isEmpty()) {
                return;
            }

            CarteViking carteViking = joueur.getCartesVikingMain().get(0);
            CarteViking cartePlateau = gameState.getPlateau().getEmplacementViking().get(carteViking.getCouleur());

            if (cartePlateau != null) {
                joueur.retirerCarteVikingMain(carteViking);
                joueur.ajouterCarteVikingEquipage(carteViking);
                Gain gain = carteViking.getGain();
                if (gain != null) {
                    joueur.appliquerGain(gain, gain.getValeur());
                }
                joueur.ajouterCarteVikingMain(cartePlateau);
                gameState.getPlateau().rafraichirVikingEmplacement(carteViking.getCouleur());
            }
        }

        private void simulateExplorer(Joueur joueur, Jeu gameState) {
            List<CarteDestination> destinationsDisponibles = gameState.getPlateau().getEmplacementCarteDest();
            for (CarteDestination destination : destinationsDisponibles) {
                if (joueur.peutAcquerirCarteDestination(destination)) {
                    joueur.acquerirCarteDestination(destination);
                    for (Map.Entry<TypeGain, Integer> entry : destination.getGain().entrySet()) {
                        Gain gain = new Gain(entry.getKey(), entry.getValue());
                        joueur.appliquerGain(gain, entry.getValue());
                    }
                    break;
                }
            }
        }

    }

    private Jeu cloneGameState(Jeu gameState) {
        return gameState.clone();
    }

    private Joueur clonePlayer(Joueur joueur) {
        Joueur clonedPlayer = joueur.clone();
        clonedPlayer.setJeu(this.jeu.clone());
        return clonedPlayer;
    }
}