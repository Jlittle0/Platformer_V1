package main;
import gameStates.*;
import gameStates.Menu;
import ui.SoundControls;

import java.awt.*;

import static utilz.Constants.Difficulties.*;

public class Game implements Runnable{
    // Private instance variables
    private GameViewer gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private int difficulty = HARD;

    private Playing playing;
    private Menu menu;
    private OptionsState options;
    private Tutorial tutorial;
    private SoundControls soundControls;


    // Size of each "tile" used in the level maps
    public final static int TILES_DEFAULT_SIZE = 32;
    // Found that changing the size of every single asset in my GUI game was one of the most
    // annoying things ever so just made a scale variable to make sure everything is the
    // same size proportionally and so that it's easy to make things bigger or smaller if wanted
    public final static float SCALE = 1.75f;
    // Visible number of tiles for game screen
    public final static int TILES_IN_WIDTH  = 26;
    public final static int TILES_IN_HEIGHT  = 14;

    public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    // Determines the size of the window by using the # of desired tiles
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameViewer(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        soundControls = new SoundControls();
        menu = new Menu(this);
        options = new OptionsState(this);
        playing = new Playing(this, difficulty);
        tutorial = new Tutorial(this);

    }

    private void startGameLoop() {
        // Idea to run the game on a thread from StackOverview not sure if I'll need this but
        // better to have something and not need it than to need it and not be able to implement
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        // Updates the window based on the current state of the game and calls its own update
        switch (Gamestate.state) {
            case PLAYING:
                playing.update();
                break;
            case MENU:
                menu.update();
                break;
            case OPTIONS:
                options.update();
                break;
            case TUTORIAL:
                tutorial.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        // Same thing as update except redrawing instead of just updating all the positions
        // and other variables that need to be checked
        switch (Gamestate.state) {
            case PLAYING:
                playing.draw(g);
                break;
            case MENU:
                menu.draw(g);
                break;
            case OPTIONS:
                options.draw(g);
                break;
            case TUTORIAL:
                tutorial.draw(g);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        // Forgot how to double buffer and felt that this was how every game I know of does stuff
        // so I made a FPS and UPS loop, knew the basic set up simply because I play games
        // but used a tip from a user on a programming discord in terms of the idea for
        // using current time and then subtracting that from the previous check so that if
        // time is ever lost between an update because of lag or something, it doesn't lose the
        // difference and just subtracts one so that the next update is faster and it's recovered.

        // Find the time between each update and frame by using set FPS and UPS above
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate =  1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while(true) {
            long currentTime = System.nanoTime();

            // Sets the change in # of updates and frames between previous check
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            // If the time between updates is greater than one update length, decrease by one and update
            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            // If the time between frames is greater than one time between each frame, repaint.
            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            // If more than a second has passed, reset frame and update count for next sec.
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                frames = 0;
                updates = 0;
            }


        }
    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }


    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public OptionsState getOptions() {
        return options;
    }

    public Tutorial getTutorial() {
        return tutorial;
    }

    public SoundControls getSoundControls() {
        return soundControls;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
