package levels;

import entities.Crab;
import entities.Worm;
import main.Game;
import objects.Cannon;
import objects.Spike;
import utilz.HelperMethods;
import objects.ContainerObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelperMethods.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crab> crabs;
    private ArrayList<Worm> worms;
    private ArrayList<ContainerObject> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createContainers();
        createSpikes();
        createCannons();
        calculateOffsets();
        calculatePlayerSpawn();
    }

    private void createCannons() {
        cannons = HelperMethods.GetCannons(img);
    }

    private void createSpikes() {
        spikes = HelperMethods.GetSpike(img);
    }

    private void createContainers() {
        containers = HelperMethods.GetContainers(img);
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
        worms = GetWorms(img);
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

    public ArrayList<Worm> getWorms() {
        return worms;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<ContainerObject> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }
}
