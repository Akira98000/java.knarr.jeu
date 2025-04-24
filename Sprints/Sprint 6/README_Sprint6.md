# Sprint 6: Système d'Automatisation pour 500 Parties

## Réalisations principales

### 1. Système d'Automatisation
- Développement d’un **système permettant l’exécution automatique de 500 parties consécutives**.
- Génération automatique des résultats pour chaque partie jouée.
- Optimisation du flux d’exécution pour garantir la stabilité et la rapidité lors des 500 parties.

---

### 2. Système de Persistance
- Mise en place d’un **système d’enregistrement des données de chaque partie** au format JSON :
    - Stockage des résultats détaillés (gagnant, scores, ressources utilisées, etc.).
    - Création d’un fichier centralisé pour regrouper les informations de toutes les parties jouées.
- Fichiers JSON de sortie générés dans le répertoire :
    - `src/main/resources/output/results.json`

---

### 3. Moteur de Jeu
- **Finalisation complète du moteur de jeu** :
    - Gestion optimisée des règles.
    - Intégration fluide des actions des bots et des événements du jeu.
    - Stabilité confirmée lors des exécutions prolongées (testé sur 500 parties).

---

### 4. Développement des Bots
#### **BOTA (Bot Random)**
- **Version stable et complète**, intégrée au système d'automatisation.
- Capacité à jouer efficacement sur 500 parties sans erreurs.

#### **BOTB et BOTC**
- **Progression importante** dans l’implémentation :
    - BOTB : Avancement significatif avec des stratégies exploratoires partiellement opérationnelles.
    - BOTC : Premiers tests de logique commerciale, bien que non finalisée.

---

## Perspectives
- Finalisation des stratégies pour **BOTB** et **BOTC**.
- Tests intensifs pour identifier et corriger d’éventuels déséquilibres dans le jeu.
- Optimisation des rapports JSON pour une analyse approfondie des performances des bots.