package levels;

import entities.Crab;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelperMethods.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crab> crabs;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calculateOffsets();
        calculatePlayerSpawn();
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calculateOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Crab> getCrabs() {
        return crabs;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
