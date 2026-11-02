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




# Guide de connection au projet 

#1. Prerequis 


Avant de commencer, assurez-vous d'avoir installé les outils suivants sur votre ordinateur :

 -----> Java JDK 17 ou supérieur : pour compiler et exécuter le programme.
----->  Git : pour récupérer le projet depuis GitHub.
----->  Apache Maven 3.9 ou supérieur : pour gérer les dépendances et compiler le projet.



Pour vérifier que ces outils sont correctement installés, ouvrez un terminal et exécutez :

-----> java -version
-----> javac -version
-----> git --version
-----> mvn -version

2. Exécution du jeu :

Pour lancer le projet suivre ces instructions :
recupérer  le  projet:  
git clone https://github.com/baraeerriyachy/pacman.git

puis faire :

-----> mvn clean compile
-----> mvn exec:java 


