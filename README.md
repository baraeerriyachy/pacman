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
