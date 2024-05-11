package utilz;

import main.Game;

// Most likely going to move all of these to their related classes/package but felt that it was
// faster and simplier to just leave them here for now

public class Constants {

    public static final float GRAVITY = 0.04f * Game.SCALE;
    public static final int ANIMATION_SPEED = 35;

    public static class Difficulties {
        public static final int EASY = 1;
        public static final int HARD = 3;
        public static final int HELL = 5;

        public static final int DIFFICULTY_MULTIPLIER = 0;

    }

    public static class Projectiles {
        public static final int CANNON_BALL = 0;
        public static final int ARROW = 1;

        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
        public static final int CANNON_BALL_WIDTH = (int)(CANNON_BALL_DEFAULT_WIDTH * Game.SCALE);
        public static final int CANNON_BALL_HEIGHT = (int)(CANNON_BALL_DEFAULT_HEIGHT * Game.SCALE);
        public static final float CBALLSPEED = 0.75f * Game.SCALE;

    }

    public static class ObjectConstants {
        // Object classifications
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;

        public static final int CONTAINER_DEFAULT_WIDTH = 40;
        public static final int CONTAINER_DEFAULT_HEIGHT = 30;
        public static final int CONTAINER_WIDTH = (int)(CONTAINER_DEFAULT_WIDTH * Game.SCALE);
        public static final int CONTAINER_HEIGHT = (int)(CONTAINER_DEFAULT_HEIGHT * Game.SCALE);

        public static final int SPIKE_DEFAULT_WIDTH = 32;
        public static final int SPIKE_DEFAULT_HEIGHT = 32;
        public static final int SPIKE_WIDTH = (int)(SPIKE_DEFAULT_WIDTH * Game.SCALE);
        public static final int SPIKE_HEIGHT = (int)(SPIKE_DEFAULT_HEIGHT * Game.SCALE);

        public static final int CANNON_DEFAULT_WIDTH = 40;
        public static final int CANNON_DEFAULT_HEIGHT = 26;
        public static final int CANNON_WIDTH = (int)(CANNON_DEFAULT_WIDTH * Game.SCALE);
        public static final int CANNON_HEIGHT = (int)(CANNON_DEFAULT_HEIGHT * Game.SCALE);

        public static int GetSpriteAmount(int objectType) {
            switch (objectType) {
                case BARREL, BOX:
                    return 8;
                case CANNON_LEFT, CANNON_RIGHT:
                    return 7;
            }
            return 1;
        }


    }

    public static class BossConstants {
        public static final float SHOCKER_SCALE = 2.0f;

        public static final int SHOCKER = 3;

        public static final int IDLE = 0;
        public static final int WALKING = 1;
        public static final int JUMPING = 2;
        public static final int FALLING = 3;
        public static final int ATTACK_1 = 4;
        public static final int ATTACK_2 = 5;
        public static final int ATTACK_3 = 6;
        public static final int HIT = 7;
        public static final int DEAD = 8;

        public static final int SHOCKER_DEFAULT_WIDTH = 88;
        public static final int SHOCKER_DEFAULT_HEIGHT = 33;
        public static final int SHOCKER_WIDTH = (int)(SHOCKER_DEFAULT_WIDTH * Game.SCALE * SHOCKER_SCALE);
        public static final int SHOCKER_HEIGHT = (int)(SHOCKER_DEFAULT_HEIGHT* Game.SCALE * SHOCKER_SCALE);

        public static final int SHOCKER_DRAWOFFSET_X = (int)(20 * Game.SCALE *SHOCKER_SCALE);
        public static final int SHOCKER_DRAW_OFFSET_X_LEFT = (int)(54 * Game.SCALE * SHOCKER_SCALE);
        public static final int SHOCKER_DRAWOFFSET_Y = (int)(9 * Game.SCALE * SHOCKER_SCALE);

        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case SHOCKER:
                    switch (enemyState) {
                        case IDLE:
                            return 6;
                        case WALKING:
                        case ATTACK_2:
                            return 8;
                        case ATTACK_1:
                            return 10;
                        case HIT:
                            return 2;
                        case DEAD:
                            return 5;
                        case JUMPING:
                        case FALLING:
                            return 3;
                        case ATTACK_3:
                            return 9;
                    }
            }
            return 0;
        }
    }

    public static class EnemyConstants {
        // Constants for enemy type
        public static final int CRAB = 0;
        public static final int WORM = 1;
        public static final int NIGHTHOUND = 2;
        public static final int SHOCKER = 3;

        // Enemy-specific state constants
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int WORM_DEFAULT_WIDTH = 40;
        public static final int WORM_DEFAULT_HEIGHT = 11;
        public static final int WORM_WIDTH = (int)(WORM_DEFAULT_WIDTH * Game.SCALE);
        public static final int WORM_HEIGHT = (int)(WORM_DEFAULT_HEIGHT * Game.SCALE);

        public static final int WORM_DRAWOFFSET_X = (int)(5 * Game.SCALE);
        public static final int WORM_DRAWOFFSET_Y = (int)(3 * Game.SCALE);

        // Basic dimensions for crab enemy
        public static final int CRAB_DEFAULT_WIDTH = 72;
        public static final int CRAB_DEFAULT_HEIGHT = 32;
        public static final int CRAB_WIDTH = (int)(CRAB_DEFAULT_WIDTH * Game.SCALE);
        public static final int CRAB_HEIGHT = (int)(CRAB_DEFAULT_HEIGHT * Game.SCALE);

        // Difference between the start of the sprite (whole rectangle including empty space to the crab)
        // and the hitbox in both the x and y direction
        public static final int CRABBY_DRAWOFFSET_X = (int)(26 * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_Y = (int)(9 * Game.SCALE);

        // Gets the number of different stages for each animation
        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case CRAB:
                    switch (enemyState) {
                        case IDLE:
                            return 9;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 7;
                        case HIT:
                            return 4;
                        case DEAD:
                            return 5;
                    }
                case WORM:
                    switch (enemyState) {
                        case IDLE:
                            return 1;
                        case RUNNING:
                            return 5;
                        case ATTACK:
                            return 0;
                        case HIT:
                            return 3;
                        case DEAD:
                            return 0;
                    }
            }
            return 0;
        }

        // Returns the max health of an enemy
        public static int GetMaxHealth(int enemyType) {
            switch(enemyType) {
                case CRAB:
                    return 10;
                case SHOCKER:
                    return 100;
                default:
                    return 1;
            }
        }

        // Returns the amount of damage an enemy does
        public static int GetEnemyDamage(int enemyType) {
            switch(enemyType) {
                case CRAB:
                    return 15;
                case SHOCKER:
                    return 25;
                default:
                    return 0;
            }
        }
    }

    public static class Environment {
        // Empty for now
    }

    public static class UI {
        public static class Buttons {
            // Specifications for current buttons on menu
            public static final int MB_DEFAULT_WIDTH = 140;
            public static final int MB_DEFAULT_HEIGHT = 56;
            public static final int MB_WIDTH = (int)(MB_DEFAULT_WIDTH *  Game.SCALE);
            public static final int MB_HEIGHT = (int)(MB_DEFAULT_HEIGHT * Game.SCALE);
        }

        public static class OptionsButtons {
            public static final int OB_DEFAULT_WIDTH = 114;
            public static final int OB_DEFAULT_HEIGHT = 41;
            public static final int OB_WIDTH = (int)(OB_DEFAULT_WIDTH * Game.SCALE + 2);
            public static final int OB_HEIGHT = (int)(OB_DEFAULT_HEIGHT * Game.SCALE);
        }

        public static class PauseButtons {
            // Specifications for each sound button (42 pixels both width and height)
            public static final int SB_DEFAULT_SIZE = 42;
            public static final int SB_SIZE = (int)(SB_DEFAULT_SIZE * Game.SCALE);
        }

        public static class UrmButtons {
            // Specifications for the unpause, redo, and menu buttons (56 pixels both w and h)
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons {
            // Dimensions for the volume button ontop of slider
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            // Height is the same for the slider as it is for the button so not specified again
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final float PLAYER_SCALE = 1.5f;

        // Player States
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int LANDING = 4;
        public static final int ATTACK = 5;
        public static final int ATTACK_2 = 6;
        public static final int ATTACK_3 = 7;
        public static final int ATTACK_4 = 8;
        public static final int HIT = 9;
        public static final int DEAD = 10;

        public static int GetSpriteAmount(int playerAction) {
            // Returns the number of parts for each action's animation)
            switch (playerAction) {
                case ATTACK_3:
                    return 10;
                case DEAD:
                case RUNNING:
                    return 8;
                case ATTACK_2:
                case ATTACK_4:
                    return 5;
                case IDLE:
                    return 9;
                case HIT:
                    return 2;
                case JUMP:
                case ATTACK:
                case FALLING:
                case LANDING:
                    return 4;
                default:
                    return 1;
            }
        }
    }
}
