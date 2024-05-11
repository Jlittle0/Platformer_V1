package entities;

import gameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;

public class Crab extends Enemy {

    //AttackBox
    private int attackBoxOffsetX;

    public Crab(float x, float y) {
        super(x, y, CRAB_WIDTH, CRAB_HEIGHT, CRAB);
        initHitbox(22, 19);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 30);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);
        if (inAir) {
            updateInAir(lvlData);
        } else {
            // Patrol
            switch (state) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerInAttackRange(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 3 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }

    public int flipX() {
        // Flips the direction of the crab in the X direction for movement
        if (walkDir == RIGHT)
            return width;
        return 0;
    }

    public int flipW() {
        // Flips the width of the crab so that it appears to be moving the other direction
        if (walkDir == RIGHT)
            return -1;
        return 1;
    }
}
