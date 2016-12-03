package project.bomberboys.game.blocks;

import java.awt.Graphics;
import java.awt.Rectangle;

import project.bomberboys.game.Game;

public class BonusBlock extends SoftBlock {
	
	private boolean firstBurn;

	public BonusBlock(Game game, float x, float y, int index, int bonusIndex, boolean dummy) {
		super(game, x, y, index, bonusIndex, dummy);
	}

	@Override
	public void update() {
		if(firstBurn) {
			
		} else {
			super.update();
		}
	}

	@Override
	public void render(Graphics g) {
		if(firstBurn) {
			
		} else {
			super.render(g);
		}
	}

	@Override
	public void destroy() {
		if(firstBurn) {
			
		} else {
			super.destroy();
			firstBurn = true;
		}
	}

	@Override
	public Rectangle getBounds() {
		return super.getBounds();
	}

}
