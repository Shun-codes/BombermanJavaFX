/**
 * Ce logiciel est distribué à des fins éducatives.
 *
 * Il est fourni "tel quel", sans garantie d'aucune sorte, explicite
 * ou implicite, notamment sans garantie de qualité marchande,
 * d'adéquation à un usage particulier et d'absence de contrefaçon.
 *
 * En aucun cas, les auteurs ou titulaires du droit d'auteur ne seront
 * responsables de tout dommage, réclamation ou autre responsabilité,
 * que ce soit dans le cadre d'un contrat, d'un délit ou autre, en
 * provenance de, consécutif à ou en relation avec le logiciel ou son
 * utilisation, ou avec d'autres éléments du logiciel.
 *
 * (c) 2025 Shun Lembrez
 * Tous droits réservés.
 */

package fr.univartois.butinfo.ihm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class BombermanGame {
	
	private GameMap map;
	
	private IBombermanController interfaceBomberman;
	
	private Player perso = new Player(this);
	
	private List<Enemy> listEnemy = new ArrayList<>();
	
	private static final int INITIAL_ENEMIES = 3;
	private static final int MAX_LEVEL = 5;
	
	private int currentLevel = 1;
	private boolean gameWon = false;
	private boolean gameOver = false;

	
	public GameMap getMap(){
		return map;
	}
	
	public Player getPlayer(){
		return perso;
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	public void setInterfaceBomberman(IBombermanController interfaceBomberman) {
		this.interfaceBomberman = interfaceBomberman;
	}
	
	public void initMap(int height, int width) {
		SoundManager.BACKGROUND.play();
		gameWon = false;
		gameOver = false;
		currentLevel = 1;
		
		initLevel(height, width);
	}
	
	private void initLevel(int height, int width) {
		// Créer la carte avec le thème approprié selon le niveau
		map = GameMapFactory.createMapWithRandomBrickWalls(height, width, 25, getLevelTheme());
		interfaceBomberman.initialize(map);
		
		// Placer le joueur s'il n'existe pas encore
		if (perso == null) {
			perso = new Player(this);
		}
		placePerso(perso);
		interfaceBomberman.linkPersoAffiche(perso);
		
		perso.setPlayerListener(interfaceBomberman);
		interfaceBomberman.linkHealthProperty(perso.getHealthProperty());
		
		// Ajouter les bombes seulement au premier niveau
		if (currentLevel == 1) {
			addBombsToPlayer();
		}
		
		interfaceBomberman.stockBombs(perso.getBombs());
		interfaceBomberman.linkBombsAffiche(perso.getPropertyBombs());
		
		// Créer les ennemis selon le niveau (INITIAL_ENEMIES + niveau - 1)
		int nbEnemies = INITIAL_ENEMIES + currentLevel - 1;
		listEnemy.clear();
		
		for(int i = 0; i < nbEnemies; i++) {
			Enemy persoEnemy = new Enemy(this, "minotaur");
			listEnemy.add(persoEnemy);
			placePerso(persoEnemy);
			interfaceBomberman.linkPersoAffiche(persoEnemy);
			persoEnemy.animate();
		}
		
		// Afficher le niveau actuel
		interfaceBomberman.showLevelMessage("Niveau " + currentLevel);
	}
	
	private void addBombsToPlayer() {
		
		// Ajout de bombes 45 pour que vous finissiez le jeu facilement Monsieur
		//Une récompense vous seras pas accordée ! :)
		
		for (int i = 0; i < 30; i++) {
			perso.getBombs().add(new Bomb(this));
		}
		for (int i = 0; i < 5; i++) {
			perso.getBombs().add(new LargeBomb(this));
		}
		for (int i = 0; i < 4; i++) {
			perso.getBombs().add(new RowBomb(this));
		}
		for (int i = 0; i < 3; i++) {
			perso.getBombs().add(new ColumnBomb(this));
		}
		for (int i = 0; i < 3; i++) {
			perso.getBombs().add(new Bomb(this));
		}
	}
	
	private String getLevelTheme() {
		// Retourne le thème selon le niveau
		switch (currentLevel) {
			case 1: return "bricks"; // Thème par défaut
			case 2: return "lvl2";  // theme lvl 2
			case 3: return "lvl3";   // Thème 3
			case 4: return "lvl4";  // Thème 4
			case 5: return "lvl5"; // Thème 5
			default: return "bricks";
		}
	}
	
	public void placePerso(AbstractCharacter perso) {
		Random randomNumber = new Random();
		List<Tile> emptyTiles = map.getEmptyTiles();
		int indexTile = randomNumber.nextInt(emptyTiles.size());
		Tile emptyTile = emptyTiles.get(indexTile);				
		perso.setPosition(emptyTile.getRow(), emptyTile.getColumn());
	}
	
	public void moveUp(AbstractCharacter perso) {
	    int newRow = perso.getRow() - 1;
	    int col = perso.getColumn();

	    if (map.isOnMap(newRow, col)
	        && map.get(newRow, col).getContent() == TileContent.LAWN
	        && !isEnemyAt(newRow, col)) {
	        
	        perso.setPosition(newRow, col);
	    }
	}

	public void moveDown(AbstractCharacter perso) {
		int newRow = perso.getRow() + 1;
		int col = perso.getColumn();
		
		if (map.isOnMap(newRow, col) && map.get(newRow, col).getContent() == TileContent.LAWN && !isEnemyAt(newRow, col)) {
	        perso.setPosition(newRow, col);
	    }
	}
	
	public void moveLeft(AbstractCharacter perso) {
	    int row = perso.getRow();
	    int newCol = perso.getColumn() - 1;

	    if (map.isOnMap(row, newCol) && map.get(row, newCol).getContent() == TileContent.LAWN && !isEnemyAt(row, newCol) ) {
	        perso.setPosition(row, newCol);
	    }
	}

	public void moveRight(AbstractCharacter perso) {
	    int row = perso.getRow();
	    int newCol = perso.getColumn() + 1;

	    if (map.isOnMap(row, newCol) && map.get(row, newCol).getContent() == TileContent.LAWN && !isEnemyAt(row, newCol) ) {
	        perso.setPosition(row, newCol);
	    }
	}
	
	public void moveUp() {
	    moveUp(perso);
	}

	public void moveDown() {
	    moveDown(perso);
	}

	public void moveLeft() {
	    moveLeft(perso);
	}

	public void moveRight() {
	    moveRight(perso);
	}
	
	public void placeBombs() {
		if (perso.getBombs().size() == 0) {
	        return; // pu de bombes
	    }
	    placeBombs(0);
	}
	
	public void placeBombs(int index) {
		// Vérifie que l'index est valide et qu'il reste des bombes
	    if (index < 0 || index >= perso.getBombs().size()) {
	        return;
	    }

	    SoundManager.PLACE_BOMB.play();

	    AbstractBomb bombAPlace = perso.removeBombs(index);
	    bombAPlace.setPosition(perso.getRow(), perso.getColumn());
	    interfaceBomberman.afficheBombes(bombAPlace);

	    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(bombAPlace.getDelay()), e -> bombAPlace.explode()));
	    timeline.setCycleCount(1);
	    timeline.play();
	}
	
	public void explode(int row, int column) {
	    SoundManager.EXPLOSION.play();		
	    Tile tile = map.get(row, column);
	    
	    //Score sur les caisses
	    if (tile.getContent() == TileContent.BRICK_WALL) {
	        interfaceBomberman.addScore(5);
	    }

	    tile.explode();

	    if (perso.getColumn() == column && perso.getRow() == row) {
	        perso.decHealth();
	        checkPlayerDeath();
	    }

	    List<Enemy> morts = new ArrayList<>();

	    for (Enemy enemy : listEnemy) {
	        if (enemy.getColumn() == column && enemy.getRow() == row) {
	            enemy.decHealth();
	            if (!enemy.isAlive()) {
	                morts.add(enemy);
	                //Score sur les ennemis détruits
	                interfaceBomberman.addScore(50);
	            }
	        }
	    }

	    listEnemy.removeAll(morts);

	    // Vérifier si tous les ennemis sont morts
	    if (!gameWon && listEnemy.isEmpty() && perso.isAlive()) {
	        if (currentLevel < MAX_LEVEL) {
	            // Passer au niveau suivant
	            nextLevel();
	        } else {
	            // Gagner le jeu au niveau 5
	            gameWon = true;
	            if (interfaceBomberman != null) {
	                interfaceBomberman.onGameWin();
	                SoundManager.BACKGROUND.stop();
	                SoundManager.VICTORY.play();
	            }
	        }
	    }
	}
	
	private void nextLevel() {
		currentLevel++;
		
		
		for (Enemy enemy : listEnemy) {
			if (enemy.isAlive()) {
				enemy.stopAnimation();
			}
		}
		
		// Nettoyer l'affichage
		interfaceBomberman.clearDisplay();
		
		// Initialiser le nouveau niveau
		initLevel(map.getHeight(), map.getWidth());
		
		// Message de transition
		interfaceBomberman.showLevelMessage("Niveau " + currentLevel + " - " + 
			(INITIAL_ENEMIES + currentLevel - 1) + " ennemis !");
	}
	
	public void endGame() {
	    System.out.println("Game Over!");
	    if (this != null) {
	        this.checkPlayerDeath();
	    }
	}
	
	public void checkPlayerDeath() {
	    if (!gameOver && !gameWon && !perso.isAlive()) {
	        gameOver = true;
	        if (interfaceBomberman != null) {
	            interfaceBomberman.onGameEnd();
	            SoundManager.BACKGROUND.stop();
	            SoundManager.DEFEAT.play();
	        }
	    }
	}
	
	private boolean isEnemyAt(int row, int col) {
	    for (Enemy enemy : listEnemy) {
	        if (enemy.getRow() == row && enemy.getColumn() == col) {
	            return true;
	        }
	    }
	    return false;
	}

	public void restart() {
		SoundManager.BACKGROUND.stop();
		SoundManager.BACKGROUND.play();
	    
	    for (Enemy enemy : listEnemy) {
	        if (enemy.isAlive()) {
	            enemy.stopAnimation();
	        }
	    }
	    
	    listEnemy.clear();
	    
	    // Réinitialiser les états du jeu
	    gameWon = false;
	    gameOver = false;
	    currentLevel = 1;
	    
	    // Réinitialiser le joueur
	    perso = new Player(this);
	    
	    // Nettoyer l'affichage et réinitialiser
	    interfaceBomberman.clearDisplay();
	    
	    // Initialiser le niveau 1
	    initLevel(map.getHeight(), map.getWidth());
	}
}
