package entities;

import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelperMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private BufferedImage[][] test;
    private boolean moving = false, attacking = false, landing = false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffset = 7 * Game.SCALE * PLAYER_SCALE;
    private float yDrawOffset = 0 * Game.SCALE;
    private int previousAttack, currentAttack;
    private long lastCheck = System.currentTimeMillis();

    // Jumping and Gravity
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    //StatusBarUI
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int)(58 * Game.SCALE);
    private int statusBarX = (int)(10 * Game.SCALE);
    private int statusBarY = (int)(10 * Game.SCALE);
    //Changing Red Bar
    private int healthBarWidth = (int)(150 * Game.SCALE);
    private int healthBarHeight = (int)(4 * Game.SCALE);
    private int healthBarXStart = (int)(34 * Game.SCALE);
    private int healthBarYStart = (int)(14 * Game.SCALE);
    // Changing Yellow Bar
    private int energyBarWidth = (int)(103 * Game.SCALE);
    private int energyBarHeight = (int)(3 * Game.SCALE);
    private int energyBarXStart = (int)(45.5 * Game.SCALE);
    private int energyBarYStart = (int)(34 * Game.SCALE);

    // Health stats
    private int healthWidth = healthBarWidth;

    // Stamina stats
    private int maxStamina = 100;
    private int currentStamina = maxStamina;
    private int staminaWidth = energyBarWidth;

    // Charge Bar (coming soon)

    //attackBox

    // Flipping the direction of the player
    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked = false;
    private Playing playing;

    private int tileY = 0;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(13, 19);
        initAttackBox();
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.playing = playing;
        this.state = IDLE;
        this.walkSpeed = Game.SCALE * 1.0f;
    }

    public void setSpawn(Point spawn) {
            this.x = spawn.x;
            this.y = spawn.y;
            hitbox.x = x;
            hitbox.y = y;
    }

    private void initAttackBox() {
        // Initializes the attack box where the player's attacks actually have an effect
        attackBox = new Rectangle2D.Float(x, y, (int)(30 * Game.SCALE), (int)(15 * Game.SCALE));
    }

    public void update() {
        // Updates everything and each is explained in their own section
        updateStatusBar();

        if (currentHealth <= 0) {
            if (state != DEAD) {
                state = DEAD;
                resetAniTick();
                playing.setPlayerDying(true);
            } else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANIMATION_SPEED - 1) {
                playing.setGameOver(true);
            } else
                updateAnimationTick();
            return;
        }

        updateOffsets();
        updateAttackBox();
        updatePos();
        if (moving) {
            checkTrapsTouched();
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
        if (attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void updateOffsets() {
        if (left && !right)
                xDrawOffset = 70 * Game.SCALE * PLAYER_SCALE;
        else if (right && !left)
                xDrawOffset = 7 * Game.SCALE * PLAYER_SCALE;
    }

    private void checkTrapsTouched() {
        playing.checkTrapsTouched(this);
    }

    private void checkAttack() {
        // Checks whether or not an attack has already started and if so return early
        if (attackChecked || (aniIndex != 0 && aniTick != 8))
            return;
        attackChecked = true;
        // Eventually checks of an enemy hitbox overlaps with the player's attack and deals damage
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
    }

    private void updateAttackBox() {
        // Changes the position of the attackbox based on the direction the player is facing
        if (right) {
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 0);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 5);
        }
        attackBox.y = hitbox.y + (int)(Game.SCALE * 10);

    }

    private void updateStatusBar() {
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
        staminaWidth = (int)(currentStamina / (float)maxStamina * staminaWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[state][aniIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y - yDrawOffset), width * flipW, height, null);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        // Draws the user interface (currently just the status bar)
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        g.setColor(Color.YELLOW);
        g.fillRect(energyBarXStart + statusBarX, energyBarYStart + statusBarY, staminaWidth, energyBarHeight);
    }

    private void updateAnimationTick() {
        // Updates the animation tick and index for each animation based on the desired speed so
        // that animations can be slowed down or sped up accordingly and are equally paced.
        aniTick++;
        if (aniTick >= ANIMATION_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {
        // Sets the current animation for the player based on its current action.
        int startAni = state;
        if (moving) {
            state = RUNNING;
        } else {
                state = IDLE;
        }
        if (inAir) {
            if (airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALLING;
            }
        }
        if (landing) {
            state = LANDING;
        }
        if (attacking) {
            state = getCurrentAttack();
        }
        if (startAni != state)
            resetAniTick();
    }

    private void resetAniTick() {
        // Resets animation tick and index
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        // Updates the position of the player
        moving = false;
        if (jump)
            jump();
        // If the player isn't moving, return early
        if (!inAir) {
            if ((!left && !right) || (right && left)) {
                if (landing == true && aniIndex == 3 && aniTick == 10)
                    landing = false;
                return;
            }
        }
        float xSpeed = 0;

        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right && !left) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (!inAir) {
            // Check whether or not the player is in the air and it isn't already known
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
            landing = false;
        }

        if (inAir) {
            // Check whether or not the player can actually move to the location they're trying
            // to get to whether they're jumping or falling.
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
                landing = false;
            } else {
                // If they can't, reset their positon to the closest avaiable spot next to solid
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                    landing = true;
                }
                else {
                    // if the player is hitting something above them, set new speed accordingly
                    airSpeed = fallSpeedAfterCollision;
                    landing = false;
                }
                updateXPos(xSpeed);
            }
        } else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump() {
        // Movement for a jump
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                 hitbox.x += xSpeed;
       } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
       }
    }

    public void changeHealth(int value) {
        currentHealth += value;
        if (currentHealth <= 0) {
            currentHealth = 0;
            //gameOver();
        } else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    public void kill() {
        currentHealth = 0;
    }

    public void changeStamina(int value) {
        currentStamina += value;
        if (currentStamina <= 0)
            currentStamina = 0;
        if (currentStamina >= maxStamina)
            currentStamina = maxStamina;
    }

    private void loadAnimations() {
        // Loads all the player animations from the atlas into a 2D array of images
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            animations = new BufferedImage[11][10];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 91, j * 19, 91, 19);
            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
            BufferedImage img2 = LoadSave.GetSpriteAtlas(LoadSave.CHARACTER_TEST);
            test = new BufferedImage[2][6];
            for (int j = 0; j < test.length; j++)
                for (int i = 0; i < test[j].length; i++)
                    test[j][i] = img2.getSubimage(i * 125, j * 100, 125, 100);


    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
            this.attacking = attacking;
            if (previousAttack == ATTACK && (System.currentTimeMillis() - lastCheck <= 1000)) {
                previousAttack = ATTACK_2;
                lastCheck = System.currentTimeMillis();
                currentAttack = ATTACK_2;
                attackChecked = false;
            } else {
                lastCheck = System.currentTimeMillis();
                previousAttack = ATTACK;
                currentAttack = ATTACK;
            }
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        landing = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public int getTileY() {
        return tileY;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public void setAttackChecked(boolean attackChecked) {
        this.attackChecked = attackChecked;
    }

    public Playing getPlaying() {
        return playing;
    }


}
