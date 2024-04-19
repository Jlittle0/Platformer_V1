package utilz;

import entities.Crab;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.CRAB;

public class LoadSave {

    // All the images used throughout the game as strings to easily call later
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
    public static final String MENU_BUTTONS= "button_atlas.png";
    public static final String PAUSE_BACKGROUND= "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String PLAYING_BACKGROUND_IMG = "playing_bg_img.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";

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

    public static ArrayList<Crab> GetCrabs() {
        // Uses the green value of the level map data to determine where Crab enemies
        // should be located. Currently only works for the first level.
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Crab> list = new ArrayList<Crab>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CRAB)
                    list.add(new Crab(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    public static int[][] GetLevelData() {
        // Currently only works for the firstLevel but will eventually have input that specifies
        // which level data should actually be read and returned. Does this by taking the
        // level data image map and checking the red value to determine which of the
        // 48 environment tiles should be located there. Assets from online pack as of now.
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48)
                    value = 0;
                lvlData[j][i] = value;
            }
        return lvlData;
    }
}
