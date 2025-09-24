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


package fr.univartois.butinfo.ihm.controller;

import fr.univartois.butinfo.ihm.model.AbstractBomb;
import fr.univartois.butinfo.ihm.model.BombermanGame;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BombController {

    @FXML
    private Label descriptionBomb;

    @FXML
    private ImageView imageBomb;
    
    @FXML
    private ImageView myImageView;
    
    Image overlayImage = new Image(getClass().getResourceAsStream("../image/Overlay.PNG"));

    @FXML
    private ListView<AbstractBomb> listView;

    @FXML
    private Label nomBomb;
    
    private Stage stage;
    
    private Scene sceneMap;
    
    private BombermanGame bombermanGame;
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public void setScene(Scene scene){
    	this.sceneMap = scene;
    }
    
    public void setModel(BombermanGame bombermanGame){
    	this.bombermanGame = bombermanGame;	
    }
    
    @FXML
    public void initialize() {
        myImageView.setImage(overlayImage);
    }

    
    public void associeListView(ObservableList<AbstractBomb> listeBombs) {
    	listView.setItems(listeBombs);
    	listView.getSelectionModel().selectedItemProperty().addListener(
    			(obj, prev, now) -> {
    				if(now != null) {
    					//image
    					Image bombImage = new Image(getClass().getResource("../image/"+now.getName()+".png").toString());
    					imageBomb.setImage(bombImage);
        				nomBomb.setText(now.getName());
        				descriptionBomb.setText(now.getDescription());
    				}
    	});


    }

    @FXML
    void onAnnuler(ActionEvent event) {
    	stage.setScene(this.sceneMap);
    }

    @FXML
    void onValider(ActionEvent event) {
    	int indexBomb = listView.getSelectionModel().getSelectedIndex();
    	bombermanGame.placeBombs(indexBomb);
    	stage.setScene(this.sceneMap);
    }

}
