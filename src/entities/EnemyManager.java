package entities;

import gameStates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.BossConstants.*;


public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabArr, wormArr, shockerArr;
    private ArrayList<Crab> crabs = new ArrayList<Crab>();
    private ArrayList<Worm> worms = new ArrayList<Worm>();
    private ArrayList<Shocker> shockers = new ArrayList<Shocker>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        // Gets the # of crabs and their locations from the level data and adds it to crabs
        crabs = level.getCrabs();
        worms = level.getWorms();
        shockers = level.getShockers();
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;

        // Updates each crab based on the lvlData and player information to determine their
        // movement
        for (Crab c : crabs)
            if (c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }
        for (Worm w : worms)
            if (w.isActive()) {
                w.update(lvlData, player);
            }
        for (Shocker s : shockers)
            if (s.isActive()) {
                s.update(lvlData, player);
                isAnyActive = true;
            }
        if (!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
        drawWorms(g, xLvlOffset);
        drawShockers(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crab c : crabs)
            if (c.isActive()) {
            g.drawImage(crabArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - CRABBY_DRAWOFFSET_X - xLvlOffset + c.flipX(), (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y, CRAB_WIDTH * c.flipW(), CRAB_HEIGHT, null);
//            c.drawAttackBox(g, xLvlOffset);
        }
    }

    private void drawWorms(Graphics g, int xLvlOffset) {
        for (Worm w : worms)
            if (w.isActive()) {
                g.drawImage(wormArr[w.getEnemyState()][w.getAniIndex()], (int) w.getHitbox().x - WORM_DRAWOFFSET_X - xLvlOffset + w.flipX(), (int) w.getHitbox().y - WORM_DRAWOFFSET_Y, WORM_WIDTH * w.flipW(), WORM_HEIGHT, null);
            }
    }

    private void drawShockers(Graphics g, int xLvlOffset) {
        for (Shocker s : shockers)
            if (s.isActive()) {
                if (s.getDir() == RIGHT) {
                    g.drawImage(shockerArr[s.getEnemyState()][s.getAniIndex()], (int) s.getHitbox().x - SHOCKER_DRAWOFFSET_X - xLvlOffset + s.flipX(), (int) s.getHitbox().y - SHOCKER_DRAWOFFSET_Y, SHOCKER_WIDTH * s.flipW(), SHOCKER_HEIGHT, null);
                }
                else
                    g.drawImage(shockerArr[s.getEnemyState()][s.getAniIndex()], (int) s.getHitbox().x - SHOCKER_DRAW_OFFSET_X_LEFT - xLvlOffset + s.flipX(), (int) s.getHitbox().y - SHOCKER_DRAWOFFSET_Y, SHOCKER_WIDTH * s.flipW(), SHOCKER_HEIGHT, null);
            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crab c: crabs)
            if (c.isActive())
                if (attackBox.intersects(c.getHitbox())) {
                    c.hurt((int)(10 / playing.getDifficulty()));
                    return;
                }
        for (Shocker s : shockers)
            if (s.isActive())
                if (attackBox.intersects(s.getHitbox())) {
                    s.hurt((int)(10 / playing.getDifficulty()));
                    return;
                }
//        for (Worm w : worms)
//            if (w.isActive())
//                if (attackBox.intersects(w.getHitbox())) {
//                    w.hurt(10);
//                    return;
//                }
    }

    private void loadEnemyImgs() {
        // 5 different states with at most 9 indexes
        crabArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabArr.length; j++)
            for (int i = 0; i < crabArr[j].length; i++)
                crabArr[j][i] = temp.getSubimage(i * CRAB_DEFAULT_WIDTH, j * CRAB_DEFAULT_HEIGHT, CRAB_DEFAULT_WIDTH, CRAB_DEFAULT_HEIGHT);

        wormArr = new BufferedImage[2][5];
        temp = LoadSave.GetSpriteAtlas(LoadSave.WORM_ATLAS);
        for (int j = 0; j < wormArr.length; j++)
            for (int i = 0; i < wormArr[j].length; i++)
                wormArr[j][i] = temp.getSubimage(i * WORM_DEFAULT_WIDTH, j * WORM_DEFAULT_HEIGHT, WORM_DEFAULT_WIDTH, WORM_DEFAULT_HEIGHT);

        shockerArr = new BufferedImage[9][10];
        temp = LoadSave.GetSpriteAtlas(LoadSave.SHOCKER_ATLAS);
        for (int j = 0; j < shockerArr.length; j++)
            for (int i = 0; i < shockerArr[j].length; i++)
                shockerArr[j][i] = temp.getSubimage(i * SHOCKER_DEFAULT_WIDTH, j * SHOCKER_DEFAULT_HEIGHT, SHOCKER_DEFAULT_WIDTH, SHOCKER_DEFAULT_HEIGHT);

    }

    public void resetAllEnemies() {
        // Create a method for each enemy type
        resetCrabs();
    }

    public void resetCrabs() {
        for (Crab c : crabs)
            c.resetEnemy();
    }
}