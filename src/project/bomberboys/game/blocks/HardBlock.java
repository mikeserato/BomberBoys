package project.bomberboys.game.blocks;

import java.awt.Color;
import java.awt.Graphics;

import project.bomberboys.game.Game;

public class HardBlock extends Block{

	public HardBlock(Game game, float x, float y) {
		super(game, x, y);
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect((int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}

}
