package objects;

import main.Game;

public class Ballista extends GameObject{
    private int tileY;

    public Ballista(int x, int y, int objectType) {
        super(x, y, objectType);
        tileY = y / Game.TILES_SIZE;
        // Placeholder numbers
        initHitbox(40, 26);
        hitbox.x -= (int)(5 * Game.SCALE);
        hitbox.y += (int)(5 * Game.SCALE);
    }

    public void update() {
        if (animate)
            updateAnimationTick();
    }

    public int getTileY() {
        return tileY;
    }


}
