package entities;

import gameStates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabArr;
    private ArrayList<Crab> crabs = new ArrayList<Crab>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        // Gets the # of crabs and their locations from the level data and adds it to crabs
        crabs = level.getCrabs();
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
        if (!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crab c : crabs)
            if (c.isActive()) {
            g.drawImage(crabArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - CRABBY_DRAWOFFSET_X - xLvlOffset + c.flipX(), (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y, CRAB_WIDTH * c.flipW(), CRAB_HEIGHT, null);
//            c.drawAttackBox(g, xLvlOffset);
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crab c: crabs)
            if (c.isActive())
                if (attackBox.intersects(c.getHitbox())) {
                    c.hurt(10);
                    return;
                }
    }

    private void loadEnemyImgs() {
        // 5 different states with at most 9 indexes
        crabArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabArr.length; j++)
            for (int i = 0; i < crabArr[j].length; i++)
                crabArr[j][i] = temp.getSubimage(i * CRAB_DEFAULT_WIDTH, j * CRAB_DEFAULT_HEIGHT, CRAB_DEFAULT_WIDTH, CRAB_DEFAULT_HEIGHT);
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

// Add a boss that's a goblin with a large sword that pops out of the tree after the user enters the boss area and then there's a pause on the screen
// with a ... until eventually there's a text bubble that is a growling and an animation players where the goblin king pops out of the massive tree stump