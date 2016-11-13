package project.bomberboys.game.blocks;

import java.awt.Color;
import java.awt.Graphics;

import project.bomberboys.game.Game;

public class SoftBlock extends Block {

	public SoftBlock(Game game, float x, float y) {
		super(game, x, y);
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawRect((int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}

}
