# Sprint 5: Bot Random - Perfectionnement

## Réalisations principales

### 1. Implémentation JSON avec Jackson
- Mise en place de la lecture des données via Jackson pour :
  - **Les cartes Viking**
  - **Les cartes Destination**
- Configuration des fichiers JSON :
  - `src/main/resources/carte_viking.json`
  - `model/json/cartes_knarr.json`
- Utilisation de **JsonLoader** pour le chargement dynamique des cartes.

---

### 2. Perfectionnement de la logique du jeu
- **Amélioration du système de gestion des tours**
- **Implémentation complète du système de commerce**
- **Raffinement des mécaniques d'exploration**
- **Optimisation de la gestion des ressources**

---

### 3. Développement des bots
#### **BOTA (Bot Random)** - Implémentation complète
- Stratégie de jeu aléatoire.
- Prise de décision pour :
  - **Le recrutement de Vikings**
  - **L'exploration de destinations**
  - **Le commerce**
- Gestion autonome des ressources et des cartes.

#### **BOTB et BOTC** - Initialisation
- Structure de base pour des stratégies plus avancées.
- Préparation pour des comportements plus complexes.

---

## Perspectives
- Finalisation des bots **BOTB** et **BOTC** avec des stratégies plus sophistiquées.
- Optimisation des performances du jeu.
- Tests et équilibrage des stratégies des bots.


