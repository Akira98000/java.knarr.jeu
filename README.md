# KNARR - Version Électronique

## Description du projet

Ce projet a pour but de développer une version électronique du jeu "KNARR", comprenant :

1. **Modélisation du jeu** : Représentation du plateau, des cartes, des pions, etc.
2. **Moteur de jeu** : Gestion des différentes phases du jeu, des tours, des points et des conditions de fin de partie.
3. **Robots joueurs** : Création de robots qui simulent les joueurs du jeu. Les robots auront plusieurs niveaux de sophistication, allant de choix aléatoires à des stratégies plus avancées.
4. **Simulation de parties** : Une simulation de 500 parties entre différents types de robots, avec comptage des points, des victoires et classement final.
5. **Visualisation textuelle** : Affichage de l'état du jeu à la fin de la partie et pendant son déroulement.

Ce projet ne nécessite pas d'interaction avec des joueurs humains, et aucune version graphique ne sera développée.

## Commande pour Installer
### Utilisation de maven

**Construction du projet maven**
```bash
mvn clean install 
```
**Lancer le projet**
- 1er argument : nombre de joueurs
- 2ème argument : nombre de parties
- 3ème argument : liste des types de joueurs (BOTA, BOTB, BOTC, BOTD)
```bash
mvn exec:java "-Dexec.mainClass=miage.knarr.equipeC.App" "-Dexec.args=3 1 BOTA BOTB BOTC"
```
**Analyser les résultats**
- Se positionner dans le folder /json/resultat_partie
- Executer la commande suivante :
  ```bash
  python3 json_analyzer.py [nom du fichier json]
  ```
- A noter qu'il est possible d'analyser directement la dernière partie enregistrée en utilisant "--last" ou "-l" à la place du nom du fichier json.
## Documentation du projet Knarr C

### Découpage du projet 
[Découpage du projet](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%200/D%C3%A9coupage_du_projet_Groupe_C.pdf)

### Diagramme de classe
[Diagramme de classe](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%201/Knarr_DC.svg)

### Diagramme d'activité
[Diagramme d'activité](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%201/diagram_activite_knarr.svg)

### Analyse du jeu
[Analyse des règles du jeu (PDF)](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%201/Knarr%20C%20-%20Analyse%20Règle%20du%20jeu.pdf)


## Accès aux documents des différentes étapes du projet
### Sprint 1
[Résumé du Sprint1](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%201/Recapitulatif%20-%20Livraison%2001%20-%20KnarrC.pdf)

### Sprint 2 
[README du Sprint2](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%202/README_Sprint2.md)

### Sprint 3 
[README du Sprint3](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%203/README_Sprint3.md)

### Sprint 4
[README du Sprint4](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%204/README_Sprint4.md)

### Sprint 5
[README du Sprint5](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%205/README_Sprint5.md)

### Sprint 6
[README du Sprint6](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%206/README_Sprint6.md)

### Sprint 7
[README du Sprint7](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%207/README_Sprint7.md)

### Sprint 8
[README du Sprint8](https://github.com/UCA-DS4H-MIAGE-L3/knarr-knarr-c/blob/main/Sprints/Sprint%208/README_Sprint8.md)

## Équipe du projet
### Team Knarr C

- **Akira Santhakumaran** - [GitHub](https://github.com/Akira98000)
- **Logan Laporte** - [GitHub](https://github.com/pzygwg) & [GitHub](https://github.com/loganlap)
- **Germain Doglioli-Ruppert** - [GitHub](https://github.com/GermainDR)
