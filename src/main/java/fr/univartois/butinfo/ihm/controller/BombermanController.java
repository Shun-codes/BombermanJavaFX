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

import java.io.IOException;

import java.net.URL;
import java.util.NoSuchElementException;

import fr.univartois.butinfo.ihm.BombermanApplication;
import fr.univartois.butinfo.ihm.model.AbstractBomb;
import fr.univartois.butinfo.ihm.model.AbstractCharacter;
import fr.univartois.butinfo.ihm.model.BombermanGame;
import fr.univartois.butinfo.ihm.model.GameMap;
import fr.univartois.butinfo.ihm.model.IBombermanController;
import fr.univartois.butinfo.ihm.model.PlayerListener;
import fr.univartois.butinfo.ihm.model.Tile;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class BombermanController  implements IBombermanController, PlayerListener {

    @FXML
    private GridPane cartePerso;

    @FXML
    private GridPane carteTerrain;

    @FXML
    private ImageView myImageView;
    
    Image overlayImage = new Image(getClass().getResourceAsStream("../image/Overlay.PNG"));
    
    
    @FXML
    private Label nbBombes;
    
    @FXML
    private ImageView bomb;
    
    private ObservableList<AbstractBomb> listeBombs;
    
    private final Image bombImage = new Image(getClass().getResource("../image/bomb.png").toString());


    @FXML
    private Label lives;
    
    @FXML
    private ImageView coeur1;

    @FXML
    private ImageView coeur2;

    @FXML
    private ImageView coeur3;
    
    // Images des coeurs
    private final Image coeurRouge = new Image(getClass().getResource("../image/heart.png").toString());
    private final Image coeurGris = new Image(getClass().getResource("../image/heartGris.png").toString());
    
    private BombermanGame bombermanGame;
    
    private Stage stage;
    
    
    private Scene sceneMap;
    
    @FXML
    private Label score;
    
    @FXML
    private Label highScore;
    
    @FXML
    private Label levelLabel;
    
    private int currentScore = 0;
    
    private int highScoreValue = 0;

    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
        
    public void setScene(Scene scene) {
        this.sceneMap = scene;

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case UP -> bombermanGame.moveUp();
                case DOWN -> bombermanGame.moveDown();
                case LEFT -> bombermanGame.moveLeft();
                case RIGHT -> bombermanGame.moveRight();
                case SPACE -> bombermanGame.placeBombs();
            }
        });

    }


    public void setBombermanGame(BombermanGame bombermanGame) {
		this.bombermanGame = bombermanGame;
	}
    
    public void updateScore(int newScore) {
        
        if (newScore > highScoreValue) {
            highScoreValue = newScore;
            highScore.setText("High Score : " + highScoreValue);
        }
    }

    
    @Override
    public void showLevelMessage(String message) {
        Platform.runLater(() -> {
            // Mettre à jour le label de niveau s'il existe
            if (levelLabel != null) {
                levelLabel.setText(message);
            }
            
            // Afficher aussi une popup temporaire
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nouveau Niveau");
            alert.setHeaderText(null);
            alert.setContentText(message);
            
            
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                if (alert.isShowing()) {
                    alert.close();
                }
            }));
            timeline.play();
            
            alert.show();
        });
    }

    
    private Image loadImage(String name) {
        try {
            URL url = getClass().getResource("../image/"+name+".png");
            return new Image(url.toExternalForm(), 48, 48, true, true);

        } catch (NullPointerException | IllegalArgumentException e) {
            throw new NoSuchElementException("Could not load image", e);
        }
    }
    
    @Override
	public void initialize(GameMap gameMap) {
    	
    	
    	    	
    	for (int i = 0; i < gameMap.getHeight(); i++) {
            for (int j = 0; j < gameMap.getWidth(); j++) {
            	
            	int iLocal = i;
            	int jLocal = j;
            	
            	
            	ImageView image = new ImageView();
            	Tile tile = gameMap.get(i, j);
            	image.imageProperty().bind(Bindings.createObjectBinding(() -> loadImage(tile.getContent().getName()),
            	tile.getContentProperty()));
            	image.setFitHeight(48);
            	image.setFitWidth(48);
            	carteTerrain.add(image, j, i);
            	
            	ImageView imageExplosion = new ImageView();
            	imageExplosion.setImage(loadImage("explosion"));
            	tile.getExploseProperty().addListener(
        				(p, o, n) -> {
        					if(n) {
        						carteTerrain.add(imageExplosion, jLocal, iLocal);
        					}
        					else {
        						carteTerrain.getChildren().remove(imageExplosion);
        					}
        				}
            			);
            	
            	
            }
    	}
    	
    	cartePerso.setMinSize(carteTerrain.getWidth(), carteTerrain.getHeight());
    	cartePerso.setMaxSize(carteTerrain.getWidth(), carteTerrain.getHeight());
    	cartePerso.setPrefSize(carteTerrain.getWidth(), carteTerrain.getHeight());
    	for(int i = 0 ; i < gameMap.getHeight(); i++) {
    		RowConstraints row = new RowConstraints(48);
            cartePerso.getRowConstraints().add(row);
    	}
    	for(int i = 0 ; i < gameMap.getWidth(); i++) {
    		ColumnConstraints column = new ColumnConstraints(48);
            cartePerso.getColumnConstraints().add(column);
            column.setHalignment(HPos.CENTER);
    	}
    	
    	bomb.setImage(bombImage);
    	myImageView.setImage(overlayImage);

    	
    	
    }
	
	@Override
	public void linkPersoAffiche(AbstractCharacter perso) {
		
		ImageView imagePerso = new ImageView();
		imagePerso.setImage(loadImage(perso.getName()));
		perso.getRowProperty().addListener(
				(p, o, n) -> {
					GridPane.setConstraints(imagePerso, perso.getColumn(), n.intValue());

				}
				);
		
		perso.getColumnProperty().addListener(
				(p, o, n) -> {
					GridPane.setConstraints(imagePerso, n.intValue(), perso.getRow());

				}
				);
		
		cartePerso.add(imagePerso, perso.getColumn(), perso.getRow());
		
		
		 perso.getHealthProperty().addListener(
		 		(p, o, n) -> {
					if(n.intValue() == 0) {
						cartePerso.getChildren().remove(imagePerso);
					}
				}
    			);
    			
		
	}
	
	@Override
	public void linkBombsAffiche(IntegerBinding bombs) {
		nbBombes.textProperty().bind(bombs.asString());
	}
	
	@Override
	public void afficheBombes(AbstractBomb bombs) {
		ImageView  imageBombs= new ImageView();
		imageBombs.setImage(loadImage(bombs.getName()));
		cartePerso.add(imageBombs, bombs.getColumn(), bombs.getRow());
		bombs.explodedProperty().addListener(
				(p, o, n) -> {
					cartePerso.getChildren().remove(imageBombs);
				}
				);
		
	}
	
	@Override
	public void onGameEnd() {        
	    // Affiche une popup 
	    Platform.runLater(() -> {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Fin de la partie");
	        alert.setHeaderText("Game Over");
	        alert.setContentText("Vous avez perdu !");
	        alert.showAndWait();

	    });
	}
	
	@Override
	public void linkHealthProperty(IntegerProperty healthProperty) {
		
	    //lives.textProperty().bind(healthProperty.asString()); // just le nombre de vie classique

	    // Mise à jour des cœurs à chaque changement de vie
	    healthProperty.addListener((obs, oldVal, newVal) -> {
	        updateHearts(newVal.intValue());
	    });

	    // Initialisation directe
	    updateHearts(healthProperty.get());
	}
	
	private void updateHearts(int health) {
	    coeur1.setImage(health >= 1 ? coeurRouge : coeurGris);
	    coeur2.setImage(health >= 2 ? coeurRouge : coeurGris);
	    coeur3.setImage(health >= 3 ? coeurRouge : coeurGris);
	}
	
	public void stockBombs(ObservableList<AbstractBomb> listeBombsPerso) {
		listeBombs = listeBombsPerso;
	}


	@FXML
    void onInventaire(ActionEvent event) throws IOException {
		// On récupère la description de la nouvelle vue.
	    FXMLLoader fxmlLoader = new FXMLLoader(
	        getClass().getResource("../view/bomb-list.fxml"));
	    Parent viewContent = fxmlLoader.load();

	    // Ensuite, on la place dans une nouvelle Scene.
	    Scene addBombScene = new Scene(viewContent);
	    stage.setScene(addBombScene);

	    // On lie le modèle au nouveau contrôleur.
	    BombController controller = fxmlLoader.getController();
	    controller.setStage(this.stage);
	    controller.setScene(this.sceneMap);
	    controller.setModel(this.bombermanGame);
	    
	    controller.associeListView(listeBombs);
    }
	
    
	@FXML
	void onRestart(ActionEvent event) {
	    bombermanGame.restart();
	    // Réinitialiser le score
	    updateScore(currentScore);
	    
	    currentScore = 0;
	    score.setText("Score: 0");
	    
	    
	}
    
    @Override
    public void onGameWin() {	
    	
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Victoire !");
            alert.setHeaderText(null);
            alert.setContentText("Félicitations ! Vous avez gagné !");
            alert.showAndWait();

        });
    }
    
    public void addScore(int points) {
        currentScore += points;
        score.setText("Score: " + currentScore);
    }



	@Override
	public void onPlayerInvincible() {
		Timeline invincibilityAnim = new Timeline(
		        new KeyFrame(Duration.seconds(0.0), e -> myImageView.setVisible(false)),
		        new KeyFrame(Duration.seconds(0.1), e -> myImageView.setVisible(true)),
		        new KeyFrame(Duration.seconds(0.2), e -> myImageView.setVisible(false)),
		        new KeyFrame(Duration.seconds(0.3), e -> myImageView.setVisible(true)),
		        new KeyFrame(Duration.seconds(0.4), e -> myImageView.setVisible(false)),
		        new KeyFrame(Duration.seconds(0.5), e -> myImageView.setVisible(true))
		    );
		    invincibilityAnim.setCycleCount(Animation.INDEFINITE);
		    invincibilityAnim.setAutoReverse(true);

		    
		    Timeline stop = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
		        invincibilityAnim.stop();
		        myImageView.setVisible(true);
		    }));
		    invincibilityAnim.play();
		    stop.play();
		
	}


	@Override
	public void clearDisplay() {
	    // Nettoyer les grilles d'affichage
	    cartePerso.getChildren().clear();
	    carteTerrain.getChildren().clear();
	    
	    //j'avais des gros probleme avec ça !
	    
	    // Réinitialiser les contraintes
	    cartePerso.getRowConstraints().clear();
	    cartePerso.getColumnConstraints().clear();
	    
	    
	}
	



	

}
