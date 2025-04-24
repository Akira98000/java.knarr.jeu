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


public class BOTC extends Joueur {

    private static final int SIMULATION_NUMBER = 1000; 
    private static final double EXPLORATION_CONSTANT = 1;//Math.sqrt(2);
    private boolean isCloned = false;

    public BOTC(String type, String nom, CouleurBateau couleurBateau, int pionsRecrue, int pionsBracelet, int pointVictoire,
            int pointRenomme) {
        super(type, nom, couleurBateau, pionsRecrue, pionsBracelet, pointVictoire, pointRenomme);
    }

    /**
     * Choisir une action en utilisant l'algorithme MCTS.
     *
     * @return TypeAction choisie
     */
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
            double childValue = (child.visitCount == 0) ? 0 : child.winScore / child.visitCount;
            if (childValue > bestValue) {
                bestValue = childValue;
                bestChild = child;
            }
        }

        if (bestChild != null && bestChild.action != null) {
            System.out.println("" + this.nom + " a choisi l'action: " + bestChild.action + "");
            return bestChild.action;
        } else {
            System.out.println("" + this.nom + " n'a pas pu choisir une action via MCTS, action par défaut sélectionnée.");
            return choisirActionPrincipale();
        }
    }

    /**
     * Choisir les actions du tour en intégrant le MCTS.
     *
     * @return Liste des actions du tour
     */
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
            System.out.println("" + nom + " n'a pas de cartes Viking en main pour recruter.");
            return;
        }

        // Choisir la première carte Viking de la main (peut être amélioré)
        CarteViking carteViking = cartesVikingMain.get(0);

        // Vérifier si une carte Viking de la même couleur est disponible sur le plateau
        CarteViking cartePlateau = jeu.getPlateau().getEmplacementViking().get(carteViking.getCouleur());

        if (cartePlateau != null) {
            retirerCarteVikingMain(carteViking);
            ajouterCarteVikingEquipage(carteViking);

            // Appliquer les gains
            Gain gain = carteViking.getGain();
            if (gain != null) {
                appliquerGain(gain);
            }

            // Ajouter la carte du plateau à la main
            ajouterCarteVikingMain(cartePlateau);

            // Rafraîchir l'emplacement sur le plateau
            jeu.getPlateau().rafraichirVikingEmplacement(carteViking.getCouleur());

            System.out.println("nom" + " a recruté et échangé une carte Viking.");
        } else {
            System.out.println("" + nom + " ne peut pas recruter car aucune carte correspondante sur le plateau.");
        }
    }

    @Override
    public void explorer() {
        List<CarteDestination> destinationsDisponibles = jeu.getPlateau().getEmplacementCarteDest();
        for (CarteDestination destination : destinationsDisponibles) {
            if (peutAcquerirCarteDestination(destination)) {
                acquerirCarteDestination(destination);
                System.out.println("" + nom + " a exploré la destination " + destination.getId() + "");
                return;
            }
        }
        System.out.println("" + nom + " ne peut pas explorer de destinations disponibles.");
    }

    @Override
    public void commercer() {
        if (!peutCommercer()) {
            System.out.println("" + nom + " ne peut pas commercer.");
            return;
        }

        // Utiliser tous les pions bracelet pour commercer
        int braceletsUtilises = pionsBracelet;
        retirerPionsBracelet(braceletsUtilises);

        for (CarteDestination destination : cartesDestination) {
            HashMap<Integer, HashMap<TypeGain, Integer>> gainCommerce = destination.getGainCommerce();
            for (int i = 1; i <= braceletsUtilises; i++) {
                HashMap<TypeGain, Integer> gains = gainCommerce.get(i);
                if (gains != null) {
                    for (Map.Entry<TypeGain, Integer> entry : gains.entrySet()) {
                        appliquerGain(new Gain(entry.getKey(), entry.getValue()));
                    }
                }
            }
        }
        System.out.println("" + nom + " a effectué une action de commerce.");
        this.aCommerceCeTour = true;
    }

    @Override
    public CarteViking choisirCarteEquipage() {
        // Choisir la première carte disponible dans l'équipage (peut être amélioré)
        for (ArrayList<CarteViking> cartes : zoneEquipage.values()) {
            if (!cartes.isEmpty()) {
                return cartes.get(0);
            }
        }
        return null;
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
        // Ajouter la carte à la collection du joueur
        cartesDestination.add(carteDestination);

        // Appliquer les gains de la carte
        HashMap<TypeGain, Integer> gains = carteDestination.getGain();
        if (gains != null) {
            for (Map.Entry<TypeGain, Integer> entry : gains.entrySet()) {
                appliquerGain(new Gain(entry.getKey(), entry.getValue()));
            }
        }

        // Retirer la carte du plateau et rafraîchir
        jeu.getPlateau().rafraichirCarteDestination(carteDestination);

        System.out.println("" + nom + " a acquis la carte destination " + carteDestination.getId() + "");
        return true;
    }

    @Override
    public void utiliserPionsRecrut(int montant) {
        if (pionsRecrue >= montant) {
            pionsRecrue -= montant;
            System.out.println("" + nom + " a utilisé " + montant + " pion(s) de recrutement.");
        } else {
            System.out.println("" + nom + " n'a pas assez de pions de recrutement.");
        }
    }

    /**
     * Classe interne Node représentant un nœud dans l'arbre MCTS.
     */
    private class Node {
        private final Node parent;
        private final List<Node> children;
        private final Jeu gameState;
        private final Joueur currentPlayer;
        private final TypeAction action;
        private int visitCount;
        private double winScore;

        /**
         * Constructeur du nœud.
         *
         * @param parent        Parent du nœud
         * @param gameState     État du jeu pour ce nœud
         * @param currentPlayer Joueur actuel
         * @param action        Action qui a mené à ce nœud
         */
        public Node(Node parent, Jeu gameState, Joueur currentPlayer, TypeAction action) {
            this.parent = parent;
            this.gameState = gameState;
            this.currentPlayer = currentPlayer;
            this.action = action;
            this.children = new ArrayList<>();
            this.visitCount = 0;
            this.winScore = 0;
        }

        /**
         * Sélectionner un nœud à étendre.
         *
         * @return Nœud sélectionné
         */
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

        /**
         * Étendre le nœud en ajoutant tous les enfants possibles.
         */
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

        /**
         * Simuler le jeu à partir de ce nœud et obtenir le résultat.
         *
         * @return Résultat de la simulation
         */
        public int simulate() {
            Jeu simulationGameState = cloneGameState(gameState);
            Joueur simulationPlayer = clonePlayer(currentPlayer);

            int simulationDepth = 0;
            int maxSimulationDepth = 10; // Limite de profondeur pour éviter les boucles infinies

            while (simulationDepth < maxSimulationDepth && !isTerminal(simulationGameState, simulationPlayer)) {
                List<TypeAction> possibleActions = getPossibleActions(simulationPlayer, simulationGameState);
                if (possibleActions.isEmpty()) {
                    break;
                }
                TypeAction randomAction = getRandomAction(possibleActions);
                simulateAction(simulationPlayer, simulationGameState, randomAction);
                simulationDepth++;
            }

            return getGameResult(simulationPlayer);
        }

        /**
         * Rétropropager le résultat de la simulation jusqu'à la racine.
         *
         * @param result Résultat de la simulation
         */
        public void backpropagate(int result) {
            visitCount++;
            winScore += result;
            if (parent != null) {
                parent.backpropagate(result);
            }
        }

        /**
         * Obtenir le meilleur enfant selon la valeur UCT.
         *
         * @return Meilleur enfant
         */
        public Node getBestChild() {
            Node bestChild = null;
            double bestValue = Double.NEGATIVE_INFINITY;
            for (Node child : children) {
                double uctValue;
                if (child.visitCount == 0) {
                    uctValue = Double.MAX_VALUE; // Favoriser l'exploration
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

        /**
         * Vérifier si ce nœud est terminal.
         *
         * @return true si terminal, false sinon
         */
        public boolean isTerminal() {
            return gameState.isFinalRound() || currentPlayer.getPointVictoire() >= 40;
        }

        /**
         * Vérifier si cet état de jeu est terminal.
         *
         * @param gameState     État du jeu simulé
         * @param currentPlayer Joueur simulé
         * @return true si terminal, false sinon
         */
        private boolean isTerminal(Jeu gameState, Joueur currentPlayer) {
            return gameState.isFinalRound() || currentPlayer.getPointVictoire() >= 40;
        }

        /**
         * Obtenir les actions possibles pour le joueur dans cet état de jeu.
         *
         * @param joueur    Joueur actuel
         * @param gameState État du jeu
         * @return Liste des actions possibles
         */
        private List<TypeAction> getPossibleActions(Joueur joueur, Jeu gameState) {
            List<TypeAction> actions = new ArrayList<>();
            if (!joueur.getCartesVikingMain().isEmpty()) {
                actions.add(TypeAction.RECRUTER);
            }
            if (!gameState.getPlateau().getEmplacementCarteDest().isEmpty()) {
                actions.add(TypeAction.EXPLORER);
            }
            // Ajoutez d'autres actions si nécessaire
            return actions;
        }

        /**
         * Sélectionner une action aléatoire parmi les actions possibles.
         *
         * @param possibleActions Liste des actions possibles
         * @return Action aléatoire
         */
        private TypeAction getRandomAction(List<TypeAction> possibleActions) {
            if (possibleActions.isEmpty()) {
                return null;
            }
            int index = (int) (Math.random() * possibleActions.size());
            return possibleActions.get(index);
        }

        /**
         * Simuler une action sur le joueur et l'état du jeu.
         *
         * @param joueur    Joueur simulé
         * @param gameState État du jeu simulé
         * @param action    Action à simuler
         */
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

        /**
         * Simuler l'action de recruter.
         *
         * @param joueur    Joueur simulé
         * @param gameState État du jeu simulé
         */
        private void simulateRecruter(Joueur joueur, Jeu gameState) {
            if (joueur.getCartesVikingMain().isEmpty()) {
                return;
            }

            // Choisir la première carte Viking de la main (peut être amélioré)
            CarteViking carteViking = joueur.getCartesVikingMain().get(0);

            // Vérifier si une carte Viking de la même couleur est disponible sur le plateau
            CarteViking cartePlateau = gameState.getPlateau().getEmplacementViking().get(carteViking.getCouleur());

            if (cartePlateau != null) {
                joueur.retirerCarteVikingMain(carteViking);
                joueur.ajouterCarteVikingEquipage(carteViking);

                // Appliquer les gains
                Gain gain = carteViking.getGain();
                if (gain != null) {
                    joueur.appliquerGain(gain, gain.getValeur());
                }

                // Ajouter la carte du plateau à la main
                joueur.ajouterCarteVikingMain(cartePlateau);

                // Rafraîchir l'emplacement sur le plateau
                gameState.getPlateau().rafraichirVikingEmplacement(carteViking.getCouleur());
            }
        }

        /**
         * Simuler l'action d'explorer.
         *
         * @param joueur    Joueur simulé
         * @param gameState État du jeu simulé
         */
        private void simulateExplorer(Joueur joueur, Jeu gameState) {
            List<CarteDestination> destinationsDisponibles = gameState.getPlateau().getEmplacementCarteDest();
            for (CarteDestination destination : destinationsDisponibles) {
                if (joueur.peutAcquerirCarteDestination(destination)) {
                    joueur.acquerirCarteDestination(destination);
                    // Appliquer les gains de la carte destination
                    for (Map.Entry<TypeGain, Integer> entry : destination.getGain().entrySet()) {
                        Gain gain = new Gain(entry.getKey(), entry.getValue());
                        joueur.appliquerGain(gain, entry.getValue());
                    }
                    break;
                }
            }
        }

        /**
         * Obtenir le résultat de la simulation.
         *
         * @param joueur    Joueur simulé
         * @return Résultat de la simulation (par exemple, points de victoire)
         */
        private int getGameResult(Joueur joueur) {
            // Retourner les points de victoire du joueur simulé
            return joueur.getPointVictoire();
        }
    }

    /**
     * Cloner l'état du jeu.
     *
     * @param gameState État du jeu à cloner
     * @return Clonage de l'état du jeu
     */
    private Jeu cloneGameState(Jeu gameState) {
        return gameState.clone();
    }

    /**
     * Cloner un joueur.
     *
     * @param joueur Joueur à cloner
     * @return Clonage du joueur
     */
    private Joueur clonePlayer(Joueur joueur) {
        Joueur clonedPlayer = joueur.clone();
        clonedPlayer.setJeu(this.jeu.clone()); 
        return clonedPlayer;
    }
}