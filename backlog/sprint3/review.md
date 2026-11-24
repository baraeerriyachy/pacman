# Sprint 3 — Revue du backlog

**Objectif :** Enrichir les mécaniques de jeu en intégrant les trésors, les monstres, les pièges et le système de points de vie, ainsi que des variations dans le gameplay.

### Priorité haute

| Tâche | Contributeur(s) | Statut |
|---|---|---|
| Implémentation des trésors : placement sur la carte et collecte par le héros | Moutawakil (première implémentation) + Alson (refonte de la logique de collecte dans Plateau) | Terminé |
| Affichage du nombre de trésors collectés dans le WinScreen | Moutawakil (proposition d'affichage) + Alson (refonte pour intégration au système d'étoiles) | Terminé |
| Implémentation d'un monstre simple suivant une trajectoire prédéfinie | Alson | Terminé |
| Implémentation d'un piège simple à position fixe | Alson | Terminé |
| Gestion des points de vie (PV) du héros : affichage en bas de l'écran et diminution au contact d'un monstre ou d'un piège | Alson | Terminé |
| Implémentation du Game Over Screen lorsque les PV du héros atteignent zéro | Alson | Terminé |

### Priorité moyenne

| Tâche | Contributeur(s) | Statut |
|---|---|---|
| Implémentation des portes de téléportation permettant au héros de se déplacer vers une autre zone de la carte | Alson | Terminé |
| Implémentation des monstres intelligents : poursuite du héros, déplacement à travers les obstacles, etc. | Alson | En cours |
| Implémentation de pièges avec activation conditionnée par le temps | Alson | Terminé |
| Écrire les tests unitaires pour monstre, piège, trésor et porte de téléportation | Alson | Terminé |

### Priorité basse

| Tâche | Contributeur(s) | Statut |
|---|---|---|
| Implémentation de plusieurs niveaux avec des cartes et des difficultés différentes | Barae (classe GameLevel de base String ) + Alson (refonte difficulte et interface de sélection, étoiles et verrouillage des niveaux, obscurité) | Terminé |
| Mise en place d'un système de sauvegarde et de reprise de la progression du joueur | Aucun | Pas commencé |
| Évolution du fichier de configuration en format CSV pour intégrer davantage d'éléments du jeu | Alson | Terminé (toujours en txt mais plus flexible) |
| Interface d'aide présentant les règles du jeu, les commandes et les différentes mécaniques | Alson | Terminé |

---

## Commentaires et rétrospective

### 1. Fonctionnalités supplémentaires développées

Au-delà des objectifs initialement définis, plusieurs améliorations ont été apportées au jeu afin d'enrichir le gameplay et d'améliorer l'expérience utilisateur.

**Mécaniques de jeu supplémentaires :**
- Ajout de mécanismes comme des leviers et des murs secrets.
- Ajout d'un piège de type mâchoire (bear trap), qui ne s'active qu'une seule fois.
- Ajout d'un monstre de type fantôme capable de traverser les murs.
- Gestion de différents délais d'activation pour les pièges temporisés.

**Améliorations de l'interface et de l'expérience utilisateur :**
- Animation du héros.
- Déplacement dynamique de la caméra pendant le jeu.
- Ajout d'un système d'obscurité pour certaines zones du niveau.
- Ajout d'un bouton permettant de mettre le jeu en pause.
- Affichage de messages d'aide et d'astuces sous la zone de jeu.
- Amélioration d'interface de fin de jeu avec un système d'étoiles basé sur les performances du joueur.
- Buttons pour : recommencer le niveau, passer au niveau suivant lorsqu'il est disponible, retourner à la sélection des niveaux ou quitter le jeu.
- Ajout d'effets visuels lors de la téléportation, avec suspension temporaire du gameplay pendant l'animation.
- Création d'une interface d'aide accessible directement depuis le jeu, avec un contenu HTML défilant.

### 2. Choix techniques et évolutions

- **Configuration des cartes :** Le format CSV initialement envisagé a été remplacé par un fichier TXT plus flexible, adapté aux besoins actuels du projet.
- **Évolution des cartes :** Pour une prochaine version, l'utilisation du fichier de format XML/JSON issue de l'outil Tiled est envisagée afin de faciliter la création et l'édition visuelle des niveaux.
- **Système de niveaux :** La structure de données initiale a été complétée par une véritable intégration dans le jeu, avec sélection des niveaux, progression, verrouillage et gestion de leur difficulté visuelle.

### 3. Difficultés rencontrées et retour d'expérience

- **Intégration des fonctionnalités :** Certaines fonctionnalités disposaient d'une première implémentation, mais nécessitaient une reprise importante de la logique métier et de leur intégration avec le reste du jeu.
- **Répartition du travail :** Plusieurs tâches initialement réparties entre les membres du groupe ont finalement été reprises. La contribution effective de chacun n'a donc pas toujours correspondu à la répartition initiale du backlog.

### 4. Axes d'amélioration pour les prochains sprints

- Proposer des diagrammes UML en début de sprint afin de clarifier l'architecture, les interactions entre les composants et le travail attendu de chacun.
- Mieux répartir les tâches et s'assurer que chaque membre contribue effectivement aux objectifs qui lui sont attribués.
- Continuer à améliorer la modularité du code et la couverture des tests unitaires.