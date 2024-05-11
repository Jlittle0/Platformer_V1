package utilz;

import entities.Crab;
import main.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.CRAB;

public class LoadSave {

   // Custom Creation
    public static final String PLAYER_ATLAS = "player_atlas_new.png";
    // Customized version of atlas taken from internet
    public static final String LEVEL_ATLAS = "LevelSprites.png";
    // Customized version of button taken from internet
    public static final String MENU_BUTTONS= "menu_buttons.png";
    // Taken from the internet
    public static final String PAUSE_BACKGROUND= "pause_menu.png";
    // Taken from the internet
    public static final String SOUND_BUTTONS = "sound_button.png";
    // Taken from the internet
    public static final String URM_BUTTONS = "urm_buttons.png";
    // Taken from the internet
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    // Custom Creation
    public static final String PLAYING_BACKGROUND_IMG = "lvl1bg.png";
    // Customized version of atlas taken from the internet
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    // Taken from the internet
    public static final String STATUS_BAR = "health_power_bar.png";
    // Heavily Inspired by Reference from internet
    public static final String MENU_BACKGROUND = "menu_background.png";
    // Custom Creation
    public static final String STUMP_ATLAS = "tree_atlas.PNG";
    // Taken from internet
    public static final String LEVEL_COMPLETE_IMG = "level_completed.png";
    // Custom Creation
    public static final String CHARACTER_TEST = "Character_1.png";
    // Taken from internet
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    // Custom Creation
    public static final String TRAP_ATLAS = "trap_atlas.png";
    // Taken from internet
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    // Taken from internet
    public static final String CANNON_BALL = "cannonball.png";
    // Custom Creation
    public static final String WORM_ATLAS = "worm_atlas.png";
    // Custom Creation inspired by videogame
    public static final String OPTIONS_BACKGROUND = "options_background.png";
    // Custom Creation
    public static final String OPTIONS_BUTTONS = "options_buttons.png";
    // Custom Creation
    public static final String HIGHLIGHTS = "highlights.png";
    // Custom Creation
    public static final String TUTORIAL = "tutorial_background.png";
    // Custom Creation
    public static final String GAME_OVER = "gameover.png";
    // Custom Creation
    public static final String GAME_WON = "congratulations.png";
    // Custom Creation
    public static final String TUTORIAL_PLATFORM_2 = "tutorial_platform_2.png";
    // Custom Creation
    public static final String SHOCKER_ATLAS = "shocker_atlas.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        // Method idea taken from stackOverflow and it basically just makes sure the image
        // with the specified fileName actually exists and whether or not it should be returned
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
             img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetLevelMaps() {
        // Gets the maps for every (manually countered) level and returns them in an array
        // of BufferedImages. Might try to make this work regardless of the size so that the
        // 4 isn't just a magic number but not currently sure how to do that.
        int numLevels = 4;
        BufferedImage[] imgs = new BufferedImage[numLevels];
        for (int i = 0; i < numLevels; i++) {
            Image temp = null;
            try {
                temp = ImageIO.read(new File("resources/lvls/" + (i + 1) + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imgs[i] = (BufferedImage) temp;
        }
        return imgs;
    }
}
