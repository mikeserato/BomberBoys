package project.bomberboys.game.blocks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import project.bomberboys.game.Game;
import project.bomberboys.game.GameObject;

public class Block extends GameObject {

	public Block(Game game, float x, float y) {
		super(game, "game", x, y);
	}

	public void update() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect((int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}

	public void destroy() {
		
	}

	public Rectangle getBounds() {
		return new Rectangle((int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}

}
