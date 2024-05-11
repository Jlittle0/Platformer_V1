package entities;

import gameStates.Playing;
import main.Game;
import utilz.Constants;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.BossConstants.SHOCKER_SCALE;
import static utilz.Constants.GRAVITY;
import static utilz.HelperMethods.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity {
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int tileY;
    // In the future don't have an actual value for this and declare it inside each individual enemy
    protected float attackDistance;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        setWalkSpeed();
        setAttackDistance();
    }

    private void setAttackDistance() {
        switch (enemyType) {
            case CRAB:
                attackDistance = Game.TILES_SIZE;
                break;
            case SHOCKER:
                attackDistance = (int)(Game.TILES_SIZE * 2.5);
        }
    }

    private void setWalkSpeed() {
        switch (enemyType) {
            case CRAB:
                walkSpeed = Game.SCALE * 0.35f;
                break;
            case WORM:
                walkSpeed = Game.SCALE * 0.1f;
                break;
            case SHOCKER:
                walkSpeed = Game.SCALE * 0.35f;
                break;
            default:
                walkSpeed = Game.SCALE * 0.35f;
        }

    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        // Essentially just movement but in air which has more checks and nuiances
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
                if (hitbox.height > Game.TILES_SIZE)
                    tileY += hitbox.height / Game.TILES_SIZE;
        }
    }

    protected void move(int[][] lvlData) {
        // Moves the enemy
        float xSpeed = 0;
        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        changeWalkDir();
    }

    protected void turnTowardsPlayer(Player player) {
        // Changes the direction of the enemy to face the player
        if (player.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
        }
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        // Checks whether or not there's an object obstructing enemies line of vision to the player
        // which would prevent them from following the player.
        int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player)) {
                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }
        return false;
    }

    // Viewing Range
    protected boolean isPlayerInRange(Player player) {
        // Checks if they player is in range for viewing and tracking the player
        int absValue =(int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    // Attacking Range
    protected boolean isPlayerInAttackRange(Player player) {
        // Checks if the player is within the set attack range for each enemy
        int absValue =(int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    protected void newState(int state) {
        // Applies a new state to the enemy
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int amount) {
        // Hurts the enemy to simulate taking damage
        currentHealth -= amount;
        if (currentHealth <= 0) {
            currentHealth = 0;
            if (enemyType == SHOCKER)
                newState(Constants.BossConstants.DEAD);
            else
                newState(DEAD);
        } else
            if (enemyType == SHOCKER)
                newState(Constants.BossConstants.HIT);
            else
                newState(HIT);
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDamage(enemyType) * player.getPlaying().getDifficulty());
        attackChecked = true;
    }

    protected void updateAnimationTick() {
        if (enemyType != SHOCKER) {
            aniTick++;
            if (aniTick >= ANIMATION_SPEED) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                    aniIndex = 0;
                    switch (state) {
                        case ATTACK:
                        case HIT:
                            state = IDLE;
                            break;
                        case DEAD:
                            active = false;
                    }
                }
            }
        }
        // This is a massive waste but I wanted to make sure boss worked and the states didn't
        // allign in terms of the order of their sprite atlas' and I didn't want to spend time fixing
        else {
            aniTick++;
            if (aniTick >= ANIMATION_SPEED) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= Constants.BossConstants.GetSpriteAmount(enemyType, state)) {
                    aniIndex = 0;
                    switch (state) {
                        case Constants.BossConstants.ATTACK_2:
                            if (walkDir == RIGHT)
                                hitbox.x += 40 * Game.SCALE * SHOCKER_SCALE;
                            else
                                hitbox.x -= 40 * Game.SCALE * SHOCKER_SCALE;
                        case Constants.BossConstants.ATTACK_1:
                        case Constants.BossConstants.ATTACK_3:
                        case Constants.BossConstants.HIT:
                            state = IDLE;
                            break;
                        case Constants.BossConstants.DEAD:
                            active = false;
                            break;

                    }
                }
            }
        }
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }
}
