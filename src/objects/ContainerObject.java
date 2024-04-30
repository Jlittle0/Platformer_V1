package objects;

import main.Game;

import static utilz.Constants.ObjectConstants.*;

public class ContainerObject extends GameObject {

    public ContainerObject(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {
        if (objectType == BOX) {
            initHitbox(25, 18);
            xOffset = (int)(7 * Game.SCALE);
            yOffset = (int)(12 * Game.SCALE);
        } else {
            initHitbox(23, 25);
            xOffset = (int)(8 * Game.SCALE);
            yOffset = (int)(5 * Game.SCALE);
        }

        hitbox.y += yOffset + (int)(Game.SCALE * 2);
        hitbox.x += xOffset / 2;

    }

    public void update() {
        if (animate)
            updateAnimationTick();
    }
}
