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


import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    public static final AudioClip PLACE_BOMB = loadClip("/fr/univartois/butinfo/ihm/sounds/place-bomb.wav");
    public static final AudioClip EXPLOSION = loadClip("/fr/univartois/butinfo/ihm/sounds/bomb-explodes.wav");
    public static final AudioClip VICTORY = loadClip("/fr/univartois/butinfo/ihm/sounds/SonsVictoire.wav");
    public static final AudioClip DEFEAT = loadClip("/fr/univartois/butinfo/ihm/sounds/SonsDefaite.wav");
    public static final AudioClip DOMMAGE = loadClip("/fr/univartois/butinfo/ihm/sounds/SonsDegats.wav");

    public static final MediaPlayer BACKGROUND = loadMusic("/fr/univartois/butinfo/ihm/sounds/SonsJeu.wav");

    private static AudioClip loadClip(String path) {
        var url = SoundManager.class.getResource(path);
        if (url == null) {
            System.err.println("Son introuvable : " + path);
            return null;
        }
        return new AudioClip(url.toString());
    }

    private static MediaPlayer loadMusic(String path) {
        var url = SoundManager.class.getResource(path);
        if (url == null) {
            System.err.println("Musique introuvable : " + path);
            return null;
        }
        Media media = new Media(url.toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE); // boucle
        return player;
    }
}



