package project.bomberboys.game.blocks;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import project.bomberboys.game.Game;
import project.bomberboys.window.SpriteSheet;

public class BonusBlock extends SoftBlock {
	
	private boolean firstBurn;
	protected BufferedImage bonus;
	protected BufferedImage bonusSpriteSheet = imageLoader.load("/img/object/bonus.png");

	public BonusBlock(Game game, float x, float y, int index, int bonusIndex, boolean dummy) {
		super(game, x, y, index, bonusIndex, dummy);
		this.bonus = SpriteSheet.grabImage(bonusSpriteSheet, 1, bonusIndex + 1, 32, 32);
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
			g.drawImage(bonus, (int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale(), null);
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
	
	public boolean getFirstBurn(){
		return this.firstBurn;
	}

	@Override
	public Rectangle getBounds() {
		return super.getBounds();
	}

}
