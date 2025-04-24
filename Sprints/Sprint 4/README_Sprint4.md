# README - Sprint 4 : Bot Random (Bot 1)

## Lien vers le sprint 4 :
[Voir la release Sprint 4](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/releases/tag/Sprint4)

## Vue d'ensemble

Dans le **Sprint 4**, l'objectif principal a été la conception, le développement et l'intégration d'un premier bot joueur capable d'interagir avec le moteur de jeu. Ce bot adopte une stratégie aléatoire pour prendre ses décisions, permettant ainsi de simuler une partie avec des actions automatisées.

## Réalisations

### **1. Conception du Bot 1**

- Création d’un **bot aléatoire**, doté des capacités suivantes :
    - Exécution des actions principales disponibles dans le moteur de jeu : **Recrutement** et **Exploration**.
    - Choix aléatoire d’actions parmi les options disponibles, garantissant une diversité de décisions pendant les tours.

- Intégration du bot dans le cycle de jeu :
    - Le bot fonctionne comme un joueur standard, utilisant les mêmes ressources et contraintes.
    - Implémentation d’un mécanisme de prise de décisions automatisé, respectant les règles définies dans le moteur de jeu.

Cette première version fournit une base simple mais fonctionnelle pour le comportement des bots.

### **2. Interaction avec le Moteur de Jeu**

- Le bot interagit directement avec le moteur de jeu, tirant parti des actions et des fonctionnalités implémentées lors des sprints précédents :
    - Recrutement : Le bot pioche une `CarteViking` lorsqu’il choisit cette action.
    - Exploration : Le bot sélectionne une `CarteDestination` au hasard parmi celles disponibles, simulant une tentative d'acquisition.

- Les décisions aléatoires du bot respectent les contraintes imposées par les règles du jeu, garantissant des interactions conformes avec le moteur de jeu.


### **3. Intégration des Premiers Tests Unitaires JUnit**

- Mise en œuvre des tests unitaires pour valider les fonctionnalités principales du bot et du moteur de jeu :
    - **Tests des actions du bot** : Validation du comportement du bot lors des phases de recrutement et d'exploration.
    - **Tests du moteur de jeu** : Vérification de la bonne gestion des ressources et des transitions entre les phases.

- Utilisation de **JUnit** pour automatiser les tests :
    - Implémentation de cas de test spécifiques pour détecter rapidement les erreurs dans les interactions du bot avec le moteur de jeu.
    - Suivi des résultats des tests pour assurer la stabilité des nouvelles fonctionnalités ajoutées.

- Résultats :
    - Les tests JUnit se sont avérés efficaces pour identifier et corriger des bugs mineurs.
    - La couverture des tests a été augmentée, garantissant une meilleure qualité du code et une fiabilité accrue du système.

## Résumé

Le **Sprint 4** marque une étape importante avec l’introduction d’un premier bot aléatoire jouant le rôle d’un joueur dans le moteur de jeu, ainsi que l'intégration des premiers tests unitaires pour valider son fonctionnement. Ces avancées garantissent un gameplay stable tout en posant les bases pour une automatisation des tests et une amélioration continue de la qualité du projet.  