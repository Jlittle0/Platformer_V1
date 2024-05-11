package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.BossConstants.*;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;


public class Shocker extends Enemy {
    private int attackDrawOffset;
    private int attackBoxOffsetX;
    private int attackSpecificOffsetR;
    private int attackSpecificOffsetL;


    public Shocker(float x, float y) {
        super(x, y, SHOCKER_WIDTH, SHOCKER_HEIGHT, SHOCKER);
        initHitbox(30, 40);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(30 * Game.SCALE * SHOCKER_SCALE), (int)(19 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 75);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        switch (state) {
            case ATTACK_2:
                attackSpecificOffsetR = (int) (Game.SCALE * 0);
                attackSpecificOffsetL = (int) (Game.SCALE * 50);
                break;
            default:
                attackSpecificOffsetR = (int) (Game.SCALE * 1 * SHOCKER_SCALE);
                attackSpecificOffsetL = (int) (Game.SCALE * 16 * SHOCKER_SCALE);
                break;
        }
        if (walkDir == RIGHT)
            attackBox.x = hitbox.x + hitbox.width + attackSpecificOffsetR;
        else if (walkDir == LEFT)
            attackBox.x = hitbox.x - hitbox.width - attackSpecificOffsetL;
        attackBox.y = hitbox.y + (int) (Game.SCALE * 15);
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
                    newState(WALKING);
                    break;
                case WALKING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerInAttackRange(player))
                            setAttack();
                    }
                    move(lvlData);
                    break;
                case ATTACK_1:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 5 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case ATTACK_3:
                    if (aniIndex == 0 && aniTick == 1) {
                        if (walkDir == RIGHT)
                            hitbox.x -= 10 * Game.SCALE * SHOCKER_SCALE;
                        else if (walkDir == LEFT)
                            hitbox.x += 10 * Game.SCALE * SHOCKER_SCALE;
                    }
                case ATTACK_2:
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

    private void setAttack() {
        int rnd = (int)(Math.random() * 101);
        if (rnd < 50)
            newState(ATTACK_1);
        else if (rnd > 50 && rnd <= 75)
            newState(ATTACK_2);
        else
            newState(ATTACK_3);
    }

    public int flipX() {
        if (walkDir == LEFT)
            return width;
        return 0;
    }

    public int flipW() {
        if (walkDir == LEFT)
            return -1;
        return 1;
    }

    public int getDir() {
        return walkDir;
    }
}
