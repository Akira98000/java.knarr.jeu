# Sprint 2 - Création des Squelettes de Classes

## Lien vers le sprint 2 :
https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/releases/tag/Sprint2

## Vue d'ensemble

Dans le **Sprint 2**, nous nous sommes concentrés sur la création des squelettes de toutes les classes nécessaires pour le développement de notre jeu. Ce sprint a permis de définir l'architecture de base du projet, en établissant les fondations pour les fonctionnalités à implémenter dans les prochains sprints.

## Réalisations

### **1. Contrôleurs (`controller`)**

- **`BotController`** *(vide pour le moment)* : Prévu pour gérer l'intelligence artificielle des joueurs bots dans le jeu.
- **`JeuController`** : Responsable de la gestion des interactions entre le modèle du jeu et la vue, coordonne les actions du joueur et les mises à jour de l'état du jeu.
- **`LoggerController`** : Gère la journalisation des événements du jeu, utile pour le débogage et le suivi des actions effectuées pendant le jeu.

### **2. Modèle (`model`)**

#### **a. Énumérations (`enums`)**

- **`CouleurBateau`** : Définit les différentes couleurs possibles pour les bateaux des joueurs.
- **`Difficulte`** : Enumère les niveaux de difficulté du jeu (facile, moyen, difficile).
- **`TypeAction`** : Liste les types d'actions que les joueurs peuvent effectuer (recruter, explorer, commercer, etc.).
- **`TypeGain`** : Spécifie les différents types de gains que les joueurs peuvent obtenir (points de victoire, renommée, ressources).
- **`VikingCouleur`** : Indique les différentes couleurs attribuables aux Vikings dans le jeu.

#### **b. Interface**

- **`JeuInterface`** : Définit les méthodes essentielles que doit implémenter la classe `Jeu`, assurant une cohérence dans l'utilisation du modèle du jeu.

#### **c. JSON (pour la configuration initiale)**

- **`cartes_knarr.json`** : Fichier JSON contenant les données des cartes utilisées dans le jeu, facilitant le chargement dynamique des cartes.
- **`JsonLoader`** : Classe utilitaire pour charger et parser les fichiers JSON, notamment pour initialiser les cartes du jeu à partir de `cartes_knarr.json`.

#### **d. Classes du Modèle**

- **`Carte`** : Classe de base représentant une carte dans le jeu, à partir de laquelle héritent les autres types de cartes.
- **`CarteDestination`** : Représente les cartes de destination que les joueurs peuvent explorer.
- **`CarteEchange`** : Modélise les cartes permettant aux joueurs d'effectuer des échanges.
- **`CarteInfluence`** : Définit les cartes qui affectent l'influence des joueurs dans le jeu.
- **`CarteViking`** : Spécialise la classe `Carte` pour les cartes représentant les Vikings recrutables par les joueurs.
- **`CoutExploration`** : Classe détaillant les coûts associés à l'exploration de nouvelles destinations.
- **`Deck<T>`** : Classe générique gérant un ensemble de cartes de type `T`, avec des méthodes pour mélanger et piocher des cartes.
- **`Gain`** : Représente les gains qu'un joueur peut obtenir, tels que des points de victoire ou des ressources.
- **`Jeu`** : Classe principale du modèle, contenant l'état global du jeu, y compris les joueurs, les decks, et le plateau.
- **`Joueur`** : Modélise un joueur dans le jeu, avec ses attributs et ses actions possibles.
- **`MoteurJeu`** : Gère la logique centrale du jeu, y compris les règles et le déroulement des parties.
- **`Plateau`** : Représente le plateau de jeu, incluant les emplacements des cartes et l'état des différentes zones de jeu.
- **`Score`** : Classe pour suivre et gérer les scores des joueurs tout au long du jeu.

### **3. Vue (`view`)**

- **`JeuView`** : Gère l'affichage des informations du jeu, notamment les logs et les messages destinés à l'utilisateur (dans le cmd).

## Résumé

Au cours de ce sprint, nous avons posé les bases du projet en créant les squelettes de toutes les classes nécessaires au fonctionnement du jeu. Cela inclut les classes du modèle, les contrôleurs pour la gestion de la logique du jeu, et les classes de vue pour l'affichage des informations. Cette structuration nous permet de disposer d'une architecture claire et modulaire pour le développement futur.
