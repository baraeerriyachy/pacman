# Pacman - M1 ACL

Projet réalisé dans le cadre du module **Analyse et Conception de Logiciels (Master 1)** à l'Université de Lorraine. 

Ce projet consiste à développer une application Pacman en Java en appliquant les principes de conception orientée objet, le développement piloté par les tests (TDD) et la gestion de versions avec Git.

---

## 📁 Structure du Projet

Le projet suit la structure standard Maven[cite: 1] :

```text
pacman/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       └── fr/
    │           └── ul/
    │               └── acl/
    │                   ├── model/      # Modèle de données (Pacman, Fantômes, Grille, etc.)
    │                   └── start/      # Point d'entrée de l'application (Main.java)
    └── test/
        └── java/
            └── fr/
                └── ul/
                    └── acl/
                        └── tests/      # Tests unitaires (JUnit)

## Niveaux de difficulte

Au lancement, le jeu propose trois cartes selectionnables :

- **Niveau 1 - Facile** (`map.txt`) : carte initiale.
- **Niveau 2 - Moyen** (`level2.txt`) : labyrinthe plus grand et plus dense.
- **Niveau 3 - Difficile** (`level3.txt`) : carte la plus grande avec davantage de murs.

Les cartes sont des fichiers texte : `H` indique le depart du heros, `S` la sortie,
`1` un mur et `0` une case libre. Le bouton de recommencement recharge la carte
du niveau choisi. Pour ajouter un niveau, ajouter son fichier dans
`src/fr/ul/acl/resource/` puis l'enregistrer dans `GameLevel`.
