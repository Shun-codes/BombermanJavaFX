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

import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
/**
 * La classe Enemy représente un adversaire du joueur dans le jeu du Bomberman.
 *
 * @author Lembrez Shun
 *
 * @version 0.1.0
 */
public class Enemy extends AbstractCharacter {

    /**
     * Le nom de ce personnage.
     */
    private final String name;
    
    /**
     * ajout de un attribut timeline pour animate 
     */
    private Timeline timeline;
    
    private boolean alive = true;


    /**
     * Construit un nouvel Enemy.
     *
     * @param name Le nom du personnage.
     */
    public Enemy(BombermanGame bombermanGame, String name) {
        super(bombermanGame, 1);
        this.name = name;
    }
    
    
    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.ihm.bomberman.model.AbstractCharacter#getName()
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Choisit une direction aléatoire et déplace l'ennemi. Q 32
     */
    public void moveRandomly() {
        Random rand = new Random();
        int direction = rand.nextInt(4); // 0 = up, 1 = down, 2 = left, 3 = right

        switch (direction) {
            case 0 -> getBombermanGame().moveUp(this);
            case 1 -> getBombermanGame().moveDown(this);
            case 2 -> getBombermanGame().moveLeft(this);
            case 3 -> getBombermanGame().moveRight(this);
        }
    }
    
    /**
     * Lance l’animation du déplacement automatique. Q 33
     */
    public void animate() {
        this.timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {moveRandomly();
            checkCollisionWithPlayer();}
            
            )
        );
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
        
        
    }
    
    private void checkCollisionWithPlayer() {
        Player player = getBombermanGame().getPlayer();

        if (this.getRow() == player.getRow() && this.getColumn() == player.getColumn()) {
            player.decHealth();
        }
    }

    
    public void decHealth(){
    	super.decHealth();
    	if(this.getHealth() == 0) {
    		timeline.stop();
    		alive = false;
    	}
    }

    
    
    public boolean isAlive() {
        return alive;
    }
    
    public void stopAnimation() {
        if (timeline != null) {
            timeline.stop();
        }
    }

}
