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

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * la classe Player représente le personnage du joueur qui utilise l'application.
 *
 * @author Lembrez Shun
 *
 * @version 0.1.0
 */
public class Player extends AbstractCharacter {
	
	/*
	 * Ajout d'un attribut bombs qui est une liste de bombes divers
	 */
	private ObservableList<AbstractBomb> bombs;
	
	private boolean alive = true;
	
	private boolean invincible = false;
	private Timeline invincibilityTimer;

	 private PlayerListener listener;

    /**
     * Construit un nouveau Player.
     */
    public Player(BombermanGame bombermanGame) {
        super(bombermanGame,3);
        this.bombs = FXCollections.observableArrayList();
    }
    
    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.ihm.bomberman.model.AbstractCharacter#getName()
     */
    @Override
    public String getName() {
        return "guy";
    }
    
   
    public void setPlayerListener(PlayerListener listener) {
        this.listener = listener;
    }

    
    /*
     * Getter pour les bombes
     */
    public ObservableList<AbstractBomb> getBombs() {
        return bombs;
    }  
    
    /*
    public AbstractBomb removeBombs(int index) {
    	
    	return bombs.remove(index);
    }*/
    
    public AbstractBomb removeBombs(int index) {
        if (index < 0 || index >= bombs.size()) {
            throw new IllegalArgumentException("Index de bombe invalide : " + index);
        }
        return bombs.remove(index);
    }

    
    public IntegerBinding getPropertyBombs() {
    	return Bindings.size(bombs);
    }
    
    public void decHealth(){
    	if (invincible) return;
    	SoundManager.DOMMAGE.play();
    	super.decHealth();
    	becomeInvincible();
    	if(this.getHealth() == 0) {
    		//La partie est fini par mort 
    		alive = false;
    		getBombermanGame().endGame();
    		
    		
    	}
    }
    
    private void becomeInvincible() {
        if (invincible) return;

        invincible = true;
        if (listener != null) {
            listener.onPlayerInvincible();
        }

        invincibilityTimer = new Timeline(
            new KeyFrame(Duration.seconds(2), e -> invincible = false)
        );
        invincibilityTimer.setCycleCount(1);
        invincibilityTimer.play();
    }

    
        
    public boolean isAlive() {
        return alive;
    }

}
