# Pacman - 2A ISN ACL

Projet réalisé dans le cadre du module **Analyse et Conception de Logiciels** à l'INP Lorraine - ENSEM. 

Ce projet consiste à développer un jeu de type Pac-Man en Java, en mettant en pratique les principes de la programmation orientée objet, la conception logicielle avec UML, les tests logiciels, les méthodes Agiles, la gestion de versions avec Git et la configuration du processus de construction avec Maven.

---

## 📁 Structure du Projet

Le projet suit une architecture inspirée du Modèle-vue-contrôleur avec la structure Maven suivante:

```text
PACMAN
└───src
    └───fr
        └───ul
            └───acl
                ├───model
                ├───view
                ├───controller
                ├───engine
                ├───resource
                ├───start
                └───tests


## Niveaux de difficulté

Au lancement, le joueur accède à un écran de sélection proposant trois difficultés : **Facile**, **Moyen** et **Difficile**.

- **Facile** : labyrinthes adaptés à une première prise en main.
- **Moyen** : labyrinthes plus grands et plus complexes, demandant davantage de réflexion.
- **Difficile** : labyrinthes plus vastes et exigeants, avec davantage d'obstacles et de défis.

La progression se fait niveau par niveau et difficulté par difficulté : le joueur doit terminer une carte pour débloquer la suivante. Une fois tous les niveaux d'une difficulté terminés, la difficulté supérieure est débloquée.

## Installation et exécution

### Prérequis

- **Java JDK 17** ou supérieur
- **Apache Maven 3.9** ou supérieur
- **Git**

### Lancement du projet

Cloner le dépôt :

```bash
git clone https://github.com/baraeerriyachy/pacman.git
cd pacman
```

Compiler le projet et lancer le jeu :

```bash
mvn clean compile
mvn exec:java
```


