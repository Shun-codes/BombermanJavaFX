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

import java.util.List;
import java.util.Random;

/**
 * La classe GameMapFactory permet de créer des cartes de jeu avec différents thèmes.
 */
public class GameMapFactory {
	
	private static final Random RANDOM = new Random();

    /**
     * Empêche la création d'instances de cette classe.
     */
    private GameMapFactory() {
        throw new AssertionError("No GameMapFactory instances for you!");
    }

    /**
     * Crée une carte vide, sur laquelle les murs se trouvent uniquement sur les côtés.
     *
     * @param height Le nombre de lignes dans la carte.
     * @param width Le nombre de colonnes dans la carte.
     *
     * @return La carte qui a été créée.
     */
    public static GameMap createEmptyMap(int height, int width) {
        GameMap map = new GameMap(height, width);

        // On place les murs en haut et en bas de la carte.
        for (int i = 0; i < width; i++) {
            map.get(0, i).setContent(TileContent.SOLID_WALL);
            map.get(height - 1, i).setContent(TileContent.SOLID_WALL);
        }

        // On place les murs à gauche et à droite de la carte.
        for (int i = 0; i < height; i++) {
            map.get(i, 0).setContent(TileContent.SOLID_WALL);
            map.get(i, width - 1).setContent(TileContent.SOLID_WALL);
        }

        // Entre les murs, il y a de l'herbe partout.
        for (int i = 1; i < (height - 1); i++) {
            for (int j = 1; j < (width - 1); j++) {
                map.get(i, j).setContent(TileContent.LAWN);
            }
        }

        return map;
    }

    /**
     * Crée une carte avec des murs positionnés à intervalle régulier à l'intérieur
     * de la carte.
     *
     * @param height Le nombre de lignes dans la carte.
     * @param width Le nombre de colonnes dans la carte.
     *
     * @return La carte qui a été créée.
     */
    public static GameMap createMapWithRegularIntermediateWall(int height, int width) {
        GameMap map = createEmptyMap(height, width);

        // On rajoute les murs à intervalle régulier.
        for (int i = 2; i < (height - 1); i += 2) {
            for (int j = 2; j < (width - 1); j += 2) {
                map.get(i, j).setContent(TileContent.SOLID_WALL2);
            }
        }

        return map;
    }

    /**
     * Crée une carte avec des murs solides positionnés à intervalle régulier à l'intérieur
     * de la carte, et un certain nombre de murs de briques répartis aléatoirement sur la
     * carte.
     *
     * @param height Le nombre de lignes dans la carte.
     * @param width Le nombre de colonnes dans la carte.
     * @param nWalls Le nombre de murs de briques à placer dans la carte.
     *
     * @return La carte qui a été créée.
     */
    /*
    public static GameMap createMapWithRandomBrickWalls(int height, int width, int nWalls) {
        GameMap map = createMapWithRegularIntermediateWall(height, width);

        // On choisit aléatoirement des tuiles vides pour y placer des murs de briques.
        List<Tile> emptyTiles = map.getEmptyTiles();
        for (int i = 0; i < nWalls; i++) {
            int index = RANDOM.nextInt(emptyTiles.size());
            Tile tile = emptyTiles.remove(index);
            tile.setContent(TileContent.BRICK_WALL);
        }

        return map;
    }
    
    */
    /**
     * Crée une carte avec des murs de briques aléatoires et un thème spécifique.
     * 
     * @param height La hauteur de la carte.
     * @param width La largeur de la carte.
     * @param brickPercentage Le pourcentage de briques à placer.
     * @param theme Le thème à utiliser pour les briques.
     * @return La carte créée.
     */
    public static GameMap createMapWithRandomBrickWalls(int height, int width, int brickPercentage, String theme) {
        GameMap map = new GameMap(height, width);
        Random random = new Random();
        
        // Obtenir le contenu de brique selon le thème
        TileContent brickContent = getBrickContentByTheme(theme);
        
        // Placer les murs solides sur les bords et aux positions fixes
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    // Murs solides sur les bords
                    map.get(i, j).setContent(TileContent.SOLID_WALL);
                } else if (i % 2 == 0 && j % 2 == 0) {
                    // Murs solides aux positions paires
                    map.get(i, j).setContent(TileContent.SOLID_WALL);
                } else {
                    // Zones intérieures : pelouse ou briques selon le pourcentage
                    if (random.nextInt(100) < brickPercentage) {
                        map.get(i, j).setContent(brickContent);
                    } else {
                        map.get(i, j).setContent(TileContent.LAWN);
                    }
                }
            }
        }
        
        
        
        return map;
    }
    
    /**
     * Version avec thème par défaut (bricks).
     */
    public static GameMap createMapWithRandomBrickWalls(int height, int width, int brickPercentage) {
        return createMapWithRandomBrickWalls(height, width, brickPercentage, "bricks");
    }
    
    /**
     * Retourne le contenu de tuile approprié selon le thème.
     * 
     * @param theme Le nom du thème.
     * @return Le contenu de tuile correspondant.
     */
    private static TileContent getBrickContentByTheme(String theme) {
        switch (theme.toLowerCase()) {
            case "lvl2":
                return TileContent.LVL2_WALL;
            case "lvl3":
                return TileContent.LVL3_WALL;
            case "lvl4":
                return TileContent.LVL4_WALL;
            case "lvl5":
                return TileContent.LVL5_WALL;
            case "bricks":
            default:
                return TileContent.BRICK_WALL;
        }
    }
    
    
}
