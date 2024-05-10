package objects;

import entities.Player;
import gameStates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.*;
import static utilz.HelperMethods.CanCannonSeePlayer;
import static utilz.HelperMethods.IsProjectileHittingLevel;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] containerImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage spikeImg, cannonBallImg;
    private ArrayList<ContainerObject> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void checkTrapsTouched(Player player) {
        for (Spike s : spikes)
            if (s.getHitbox().intersects(player.getHitbox()))
                player.kill();

    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        // Essentially go through each of the desired object within their arraylist above once
        // added and check whether it's active or not. If it's active, then check if the hitbox
        // intersects with the hitbox of the specific object, then either set active to false
        // and apply the given affect or change a variable like inRange or whatever that allows
        // the user to interact with the object.

    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
            for (ContainerObject c : containers)
                if (c.getActive()) {
                    if (c.getHitbox().intersects(attackbox)) {
                        c.setAnimation(true);
                        // Types for if anything is supposed to happen based on whether a box
                        // or a barrel was broken, for example maybe an item should drop then
                        // change type to constant value equal to that item.
//                        int type = 0;
//                        if (c.getObjectType() == BARREL)
//                            type = 1;
                        return;
                    }
                }
    }

    public void loadObjects(Level newLevel) {
        containers = new ArrayList<ContainerObject>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
        projectiles.clear();
    }

    private void loadImgs() {
        BufferedImage containerSprites = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprites.getSubimage(40 * i, 30 * j, 40, 30);

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < cannonImgs.length; i++)
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
    }

    public void update(int[][] lvlData, Player player) {
        for (ContainerObject c : containers)
            if (c.getActive())
                c.update();
        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-25);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData)) {
                    p.setActive(false);
                }
            }


    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue =(int)Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE* 5;
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
        if (c.getObjectType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x)
                return true;
        }
        else if (c.getHitbox().x < player.getHitbox().x)
            return true;
        return false;
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : cannons) {
            if (!c.animate)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInFrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
                                c.setAnimation(true);
            c.update();
            if (c.getAniIndex() == 4 && c.getAniTick() == 5)
                shootCannon(c);
        }
    }

    private void shootCannon(Cannon c) {
        int dir = 1;
        if (c.getObjectType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int)c.getHitbox().x, (int)c.getHitbox().y, dir, CANNON_BALL));
    }


    public void draw(Graphics g, int xLvlOffset) {
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImg, (int)(p.getHitbox().x - xLvlOffset), (int)p.getHitbox().y, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int)(c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjectType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[c.getAniIndex()], x, (int)(c.getHitbox().y), width, CANNON_HEIGHT, null);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : spikes)
            // Make sure to account for xOffset if required in the future for other traps
            g.drawImage(spikeImg, (int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y - s.getyOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (ContainerObject c : containers)
            if (c.isActive) {
                int type = 0;
                if (c.getObjectType() == BARREL)
                    type = 1;
                g.drawImage(containerImgs[type][c.getAniIndex()], (int)(c.getHitbox().x - c.getXOffset() - xLvlOffset), (int)(c.getHitbox().y - c.getyOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
    }

    public void resetAllObjects() {
        for (ContainerObject c : containers)
            c.reset();
        for (Cannon c : cannons)
            c.reset();
    }


}
