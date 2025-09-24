# Bomberman JavaFX

Un projet de **Bomberman** développé en **Java** avec une interface graphique en **JavaFX**.

---

## Table des matières
- [Description](#description)
- [Aperçu du jeu](#aperçu-du-jeu)
- [Architecture](#architecture)
- [Fonctionnalités](#fonctionnalités)
- [Installation](#installation)

---

## Description
Ce projet est une ré-implémentation du célèbre jeu **Bomberman** avec JavaFX. Le jeu supporte le placement de bombes, les ennemis, une carte dynamique.  


## Aperçu du jeu
Voici à quoi ressemble le jeu :



## Architecture

Le projet est organisé en **packages `controller` et `model`** :

### Controller
- **`BombermanController`** : gère les interactions principales du joueur et l’état du jeu.  
- **`BombController`** : gère la logique des bombes (placement, explosion, dégâts).  
- **`BombermanApplication`** : classe principale, lance l’application JavaFX.

### Model
- **Bombes** : `AbstractBomb`, `Bomb`, `LargeBomb`, `RowBomb`, `ColumnBomb`  
- **Personnages et ennemis** : `Player`, `PlayerListener`, `Enemy`  
- **Jeu et carte** : `BombermanGame`, `GameMap`, `GameMapFactory`  
- **Tiles et contenu** : `Tile`, `TileContent`  
- **Sons** : `SoundManager`  
- **Interface commune** : `IBombermanController`

---

## Fonctionnalités

- Déplacement du joueur et des ennemis  
- Placement de bombes avec explosions directionnelles  
- Gestion obstacles / vie / Victoire/Defaite avec 5 lvl
- Sons et effets audio avec `SoundManager`  
- Carte générée dynamiquement via `GameMapFactory`  
- Interface graphique interactive avec JavaFX  

---

## Installation

### Exécution
1. Cloner le projet depuis GitHub.
2. Les instructions pour exécuter le jeu seront ajoutées après la création de l'exécutable.
