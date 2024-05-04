package gameStates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelHandler;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelHandler levelHandler;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;
    private boolean playerDying = false;

    // Borders to create the illusion of movement if the player is too far right or left of the screen
    private int xLvlOffset;
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);

    private int maxLvlOffsetX;
    private BufferedImage backgroundImg;

    private boolean gameOver = false;
    private boolean lvlCompleted = false;

    public Playing(Game game) {
        super(game);
        initClasses();
        // Initialize and create background
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelHandler.loadNextLevel();
        player.setSpawn(levelHandler.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
            enemyManager.loadEnemies(levelHandler.getCurrentLevel());
            objectManager.loadObjects(levelHandler.getCurrentLevel());
    }

    private void calcLvlOffset() {
            maxLvlOffsetX = levelHandler.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        // Initializes all the classes etc. so that the constructor isn't so cluttered
        levelHandler = new LevelHandler(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player = new Player(200, 200, (int)(64 * Game.SCALE), (int)(40 * Game.SCALE), this);
        player.loadLvlData(levelHandler.getCurrentLevel().getLevelData());
        player.setSpawn(levelHandler.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        // Check whether or not the game should actually be updated and then update each
        // individual part of the gamePlay including the level, player, and enemies.
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver){
//            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        }else {
            levelHandler.update();
            objectManager.update(levelHandler.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelHandler.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        // Creates the illusion of the level moving based on the player being too close to either
        // edge of the screen
        int playerX = (int)(player.getHitbox().x);
        int diff = playerX - xLvlOffset;

        // Check if screen needs to be moved based on player pos
        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        // Restrict offset to the edges of the level so that a blank screen isn't shown
        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        // Draws the background
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        // Draws each individual part of the game in the order of level tiles, player, then enemies
        levelHandler.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);

        // Draw the paused or gameOver overlays depending on whether or not the game is paused and over
        if (paused) {
            g.setColor(new Color(0, 0, 0, 175));
            g.fillRect(0, 0, game.GAME_WIDTH, game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
        else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted)
            levelCompletedOverlay.draw(g);
    }

    public void resetAll() {
        // resets everything
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        objectManager.checkObjectHit(attackbox);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        // Checks whether or not an enemy was hit by seeing if there's an overlap between the
        // attackBox of the player and the hitbox of one of the enemies
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkTrapsTouched(Player player) {
        objectManager.checkTrapsTouched(player);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Attacking action if left mouse is clicked, might change to pressed
        if (!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // MousePressed currently has no role other than in the pause screen
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        // Same this as mousePressed
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Same thing again
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Same thing again again
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // If the game is over, have different keybinds but generally a and d for left and right
        // movement of the player then space to jump and esc to pause the game. Standard.
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
    }

    public void setMaxLevelOffset(int lvlOffset)  {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public void setLevelCompleted(boolean complete) {
        this.lvlCompleted = complete;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
