# README - Sprint 3 : Réalisation du Moteur de Jeu

## Lien vers le sprint 3 :
[Voir la release Sprint 3](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/releases/tag/Sprint3)

## Vue d'ensemble

Dans le **Sprint 3**, nous nous sommes concentrés sur l'implémentation du moteur de jeu et la gestion des phases principales, permettant ainsi de dérouler une partie complète de bout en bout. Ce sprint marque une étape importante en intégrant les fonctionnalités essentielles du jeu, incluant l'initialisation des ressources et la réalisation des actions principales des joueurs.

## Réalisations

### **1. Initialisation des Ressources**

- Mise en place de l'initialisation des cartes et des ressources pour les joueurs, comprenant :
  - Distribution de trois `cartesViking` à chaque joueur.
  - Attribution d’une `CarteBateau` par joueur, avec un pion Recrut et un pion Bracelet.
  - Création du `VikingDeck`, représentant le paquet de cartes sur le plateau.

L'étape d'initialisation est désormais fonctionnelle et assure une répartition équilibrée des ressources de départ pour chaque joueur.

### **2. Gestion des Actions de Jeu**

- Implémentation des actions de jeu pour les joueurs (bots non intelligents), incluant :
  - **Recrutement (`Recrut`)** : Les joueurs peuvent piocher une `CarteViking` du `VikingDeck`.
  - **Exploration** : Permet aux joueurs de tenter d'acquérir une `CarteDestination`. Cette fonctionnalité est partiellement corrigée et nécessite des ajustements supplémentaires pour une gestion optimale.

Ces actions ont été testées lors du premier tour de jeu, permettant de valider leur bon fonctionnement dans un contexte de jeu réel.

### **3. Intégration des Données JSON**

- Utilisation de la bibliothèque Jackson pour la récupération et la manipulation des données au format JSON :
  - Chargement dynamique des données des cartes (`couleur`, `gains`, etc.) depuis des fichiers JSON.
  - Enrichissement des cartes avec des caractéristiques authentiques, reflétant les données réelles intégrées lors de l'action de recrutement.

Cette intégration garantit que les cartes utilisées dans le jeu correspondent aux spécifications, ajoutant une dimension de réalisme à l'expérience.

## Résumé

Le **Sprint 3** marque l'achèvement d'une étape clé avec l'implémentation d'un moteur de jeu fonctionnel, permettant le déroulement des phases principales et des actions des joueurs. Nous avons assuré une initialisation complète des ressources et implémenté des fonctionnalités interactives de base pour les joueurs. Bien que certaines fonctionnalités nécessitent encore des ajustements, cette release pose les bases pour une partie de jeu fluide et immersive, prête à être étendue et optimisée dans les prochains sprints.