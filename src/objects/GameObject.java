package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.ObjectConstants.*;

public class GameObject {
        protected int xPos, yPos, objectType;
        protected Rectangle2D.Float hitbox;
        protected boolean animate, isActive = true;
        protected int aniTick, aniIndex;
        protected int xOffset, yOffset;

        public GameObject(int x, int y, int objectType) {
            this.xPos = x;
            this.yPos = y;
            this.objectType = objectType;
        }

        protected void updateAnimationTick() {
            aniTick++;
            if (aniTick >= ANIMATION_SPEED) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= GetSpriteAmount(objectType)) {
                    aniIndex = 0;
                    if (objectType == BARREL || objectType == BOX) {
                        animate = false;
                        isActive = false;
                    } else if (objectType == CANNON_LEFT || objectType == CANNON_RIGHT)
                        animate = false;
                }
            }
        }

        public void reset() {
            aniIndex = 0;
            aniTick = 0;
            isActive = true;
            if (objectType == BOX || objectType == BARREL || objectType == CANNON_LEFT || objectType == CANNON_RIGHT)
                animate = false;
            else
                animate = true;
        }

        public void initHitbox(int width, int height) {
            hitbox = new Rectangle2D.Float(xPos, yPos, (int)(Game.SCALE * width), (int)(Game.SCALE * height));
        }

        public void drawHitbox(Graphics g) {
            g.setColor(Color.RED);
            g.drawRect((int)(hitbox.x + xOffset), (int)(hitbox.y + yOffset), (int)(hitbox.width), (int)hitbox.height);
        }

        public int getObjectType() {
            return objectType;
        }

        public Rectangle2D.Float getHitbox() {
            return hitbox;
        }

        public void setActive(boolean active) {
            this.isActive = active;
        }

        public void setAnimation(boolean animate) {
            this.animate = animate;
        }

        public boolean getActive() {
            return isActive;
        }

        public int getAniIndex() {
            return aniIndex;
        }

        public int getXOffset() {
            return xOffset;
        }

        public int getyOffset() {
            return yOffset;
        }

        public int getAniTick() {
            return aniTick;
        }
}
