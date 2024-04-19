package entities;

import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelperMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    // Jumping and Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

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
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // Stamina stats
    private int maxStamina = 100;
    private int currentStamina = maxStamina;
    private int staminaWidth = energyBarWidth;

    // Charge Bar (coming soon)

    //attackBox
    private Rectangle2D.Float attackBox;

    // Flipping the direction of the player
    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked = false;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, (int)(20 * Game.SCALE), (int)(27 * Game.SCALE));
        initAttackBox();
        this.playing = playing;
    }

    private void initAttackBox() {
        // Initializes the attack box where the player's attacks actually have an effect
        attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update() {
        // Updates everything and each is explained in their own section
        updateStatusBar();
        updateStatusBar();

        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }

        updateAttackBox();
        updatePos();
        if (attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        // Checks whether or not an attack has already started and if so return early
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        // Eventually checks of an enemy hitbox overlaps with the player's attack and deals damage
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        // Changes the position of the attackbox based on the direction the player is facing
        if (right) {
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
        }
        attackBox.y = hitbox.y + (int)(Game.SCALE * 10);

    }

    private void updateStatusBar() {
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
        staminaWidth = (int)(currentStamina / (float)maxStamina * staminaWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y - yDrawOffset), width * flipW, height, null);
        // Shows hitbox and attackbox for testing purposes
//        drawHitbox(g, lvlOffset);
//        drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlXOffset) {
        g.setColor(Color.RED);
        g.drawRect((int)attackBox.x - lvlXOffset, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        // Draws the user interface (currently just the status bar)
        // XY
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        g.setColor(Color.YELLOW);
        g.fillRect(energyBarXStart + statusBarX, energyBarYStart + statusBarY, staminaWidth, energyBarHeight);
    }

    private void updateAnimationTick() {
        // Updates the animation and index for each animation based on the desired speed so
        // that animations can be slowed down or sped up accordingly and are equally paced.
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {
        // Sets the current animation for the player based on its current action.
        int startAni = playerAction;

        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
        if (inAir) {
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }
        if (attacking) {
            playerAction = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if (startAni != playerAction)
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
        if (!inAir)
            if ((!left && !right) || (right && left))
                return;

        float xSpeed = 0;

        if (left) {
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (!inAir) {
            // Check whether or not the player is in the air and it isn't already known
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            // Check whether or not the player can actually move to the location they're trying
            // to get to whether they're jumping or falling.
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                // If they can't, reset their positon to the closest avaiable spot next to solid
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    // if the player is hitting something above them, set new speed accordingly
                    airSpeed = fallSpeedAfterCollision;
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
            animations = new BufferedImage[7][8];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
            this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
}
