package levels;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelHandler {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelHandler(Game game) {
        this.game = game;
        importOutsideSprites();
        // Stores the data for each level
        levelOne = new Level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites() {
        // Imports the tile assets for each level, currently only one image
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
    }

    public void draw(Graphics g, int lvloffset) {
        // offSet to create the illusion of movement and scrolling based on player location
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < levelOne.getLevelData()[0].length; i++) {
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - lvloffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levelOne;
    }
}

// Idea to create a tutorial area or a place that isn't actually part of a level is to create a new level but never actually
// Draw the images that are meant to represent the floor so that their hitboxes are still there and so that the character
// Can't fall through the ground or anything but so that the background remains solid