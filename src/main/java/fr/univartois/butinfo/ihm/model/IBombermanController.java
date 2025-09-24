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

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;

public interface IBombermanController extends PlayerListener {
	
	public void setBombermanGame(BombermanGame bombermanGame);
	
	public void initialize(GameMap gameMap);
	
	public void linkPersoAffiche(AbstractCharacter perso);
	
	public void linkBombsAffiche(IntegerBinding bombs);
	
	public void afficheBombes(AbstractBomb bombs);
	
	public void onGameEnd();
	
	public void linkHealthProperty(IntegerProperty healthProperty);
	
	public void stockBombs(ObservableList<AbstractBomb> listeBombsPerso);
	
	public void onGameWin();
	
	public void addScore(int points);
	
	public void clearDisplay();
	
	void showLevelMessage(String message);


}
