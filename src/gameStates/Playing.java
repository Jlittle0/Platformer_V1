package gameStates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelHandler;
import main.Game;
import ui.GameOverOverlay;
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
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private boolean paused = false;

    // Borders to create the illusion of movement if the player is too far right or left of the screen
    private int xLvlOffset;
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);

    // Getting information about the level to find the edge since each might be different lengths
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
    private BufferedImage backgroundImg;

    private boolean gameOver = false;

    public Playing(Game game) {
        super(game);
        initClasses();
        // Initialize and create background
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
    }

    private void initClasses() {
        // Initializes all the classes etc. so that the constructor isn't so cluttered
        levelHandler = new LevelHandler(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int)(64 * Game.SCALE), (int)(40 * Game.SCALE), this);
        player.loadLvlData(levelHandler.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
    }

    @Override
    public void update() {
        // Check whether or not the game should actually be updated and then update each
        // individual part of the gamePlay including the level, player, and enemies.
        if (!paused && !gameOver) {
            levelHandler.update();
            player.update();
            enemyManager.update(levelHandler.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        } else {
            pauseOverlay.update();
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
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);

        // Draw the paused or gameOver overlays depending on whether or not the game is paused and over
        if (paused) {
            g.setColor(new Color(0, 0, 0, 175));
            g.fillRect(0, 0, game.GAME_WIDTH, game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
        else if (gameOver) {
            gameOverOverlay.draw(g);
        }
    }

    public void resetAll() {
        // resets everything
        gameOver = false;
        paused = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        // Checks whether or not an enemy was hit by seeing if there's an overlap between the
        // attackBox of the player and the hitbox of one of the enemies
        enemyManager.checkEnemyHit(attackBox);
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
        if (!gameOver)
            if (paused)
                pauseOverlay.mousePressed(e);
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
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Same thing again again
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseMoved(e);
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

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }
}
