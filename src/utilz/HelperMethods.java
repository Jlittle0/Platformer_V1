package utilz;

import entities.Crab;
import entities.Worm;
import gameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.CRAB;
import static utilz.Constants.EnemyConstants.WORM;
import static utilz.Constants.ObjectConstants.*;

import objects.Cannon;
import objects.ContainerObject;
import objects.Projectile;
import objects.Spike;

public class HelperMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        // Checks whether or not an object can move to a certain location by checking the four
        // corners of the hitbox against the lvlData which stores the location of all solids.
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        // Works with IsTileSolid to return whether or not a tile is solid by first getting the
        // index of the tile you want ot check based on the x and y provided and then mapping
        // that to the lvlData that stores the tile locations to see if there's something there.
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int)xIndex, (int)yIndex, lvlData);
    }

    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
            return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        // Checks whether or not a tile at a specified index is solid
        int value = lvlData[yTile][xTile];
        // # of sprites and 11 is transparent
        if (value >= 48 || value < 0 || value != 11)
            return true;
        return false;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        // Makes sure an entity isn't stuck inside of a wall and that if they collide with one
        // they're set to the correct position by creating an offset determmined by the distance
        // from the edge of the tile they're located in and edge of their hitbox.
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            // Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        // Makes sure that entities can't jump or fall through a floor by doing the same thing as
        // the method above except for the roof and floor instead of left and right walls
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // Falling - Touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset -1;
        } else {
            // Jumping - Touching ceiling
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below the bottom left and bottom right corners of the entities' hitbox
        // against the lvlData to determine whether or not it's standing on the floor
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return (IsSolid(hitbox.x + + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData));
        else
            return (IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData));
    }

    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int)(firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int)(secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int yTile, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, yTile, lvlData))
                return false;
            return true;
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int yTile, int[][] lvlData) {
        // Determines for enemies whether or not the path to the player is walkable for them
        // and they should pursue or whether they shouldn't detect the player because there's
        // an obstruction preventing them from moving to the player's position
        if (IsAllTilesClear(xStart, xEnd, yTile, lvlData))
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!IsTileSolid(xStart + i, yTile + 1, lvlData))
                     return false;
            }
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        // Works in tandem with the method above for basic enemy pathing and checks whether
        // there is an obstruction between two entities to show whether or not they can actually
        // see eachother
        int firstXTile = (int)(firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int)(secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

    public static int[][] GetLevelData(BufferedImage img) {
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

    public static ArrayList<Crab> GetCrabs(BufferedImage img) {
        // Uses the green value of the level map data to determine where Crab enemies
        // should be located. Currently only works for the first level.
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

    public static ArrayList<Worm> GetWorms(BufferedImage img) {
        ArrayList<Worm> list = new ArrayList<Worm>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == WORM)
                    list.add(new Worm(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                // Magic number currently for testing purposes
                if (value == 100)
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
            }
        return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }

    public static ArrayList<ContainerObject> GetContainers(BufferedImage img) {
        ArrayList<ContainerObject> list = new ArrayList<ContainerObject>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == BOX || value == BARREL)
                    list.add(new ContainerObject(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }
        return list;
    }


    public static ArrayList<Spike> GetSpike(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<Spike>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == SPIKE)
                    list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
            }
        return list;
    }

    public static ArrayList<Cannon> GetCannons(BufferedImage img) {
        ArrayList<Cannon> list = new ArrayList<Cannon>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == CANNON_LEFT || value == CANNON_RIGHT)
                    list.add(new Cannon(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }
        return list;
    }
}
