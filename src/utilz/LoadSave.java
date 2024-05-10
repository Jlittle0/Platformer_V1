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

    // All the images used throughout the game as strings to easily call later
    public static final String PLAYER_ATLAS = "player_atlas_new.png";
    // Change the .png file above to character_atlas in the future.
    public static final String LEVEL_ATLAS = "LevelSprites.png";
    public static final String MENU_BUTTONS= "menu_buttons.png";
    public static final String PAUSE_BACKGROUND= "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String PLAYING_BACKGROUND_IMG = "lvl1bg.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String STUMP_ATLAS = "tree_atlas.PNG";
    public static final String LEVEL_COMPLETE_IMG = "level_completed.png";
    public static final String CHARACTER_TEST = "Character_1.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "cannonball.png";
    public static final String WORM_ATLAS = "worm_atlas.png";
    public static final String OPTIONS_BACKGROUND = "options_background.png";
    public static final String OPTIONS_BUTTONS = "options_buttons.png";
    public static final String HIGHLIGHTS = "highlights.png";
    public static final String TUTORIAL = "tutorial_background.png";
    public static final String GAME_OVER = "gameover.png";
    public static final String GAME_WON = "congratulations.png";
    public static final String TUTORIAL_PLATFORM_2 = "tutorial_platform_2.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        // Method idea taken from stackOverflow (forgot the username but I'll find it and put
        // it here) and it basically just makes sure the image with the specified fileName actually
        // exists and whether or not it should be returned
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
        // 3 isn't just a magic number but not currently sure how to do that.
        int numLevels = 3;
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
