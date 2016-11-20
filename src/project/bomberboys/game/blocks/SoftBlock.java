package project.bomberboys.game.blocks;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import project.bomberboys.game.Game;
import project.bomberboys.window.Animation;
import project.bomberboys.window.SpriteSheet;

public class SoftBlock extends Block {
	
	private BufferedImage[] images = new BufferedImage[3];

	public SoftBlock(Game game, float x, float y, int index, boolean dummy) {
		super(game, x, y, index, 0, dummy);
		this.life = 1;
		loadImages();
		burningAnimation = new Animation(game, 15, images);
	}
	
	public void loadImages(){
		for(int i = 0; i < 3; i++){
			images[i] = SpriteSheet.grabImage(terrain, index, i + 1, 32, 32);
		}
	}
	
	public void render(Graphics g) {
		if(burning) burningAnimation.drawAnimation(g, x, y); 
		else g.drawImage(images[0], (int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale(), null);
	}

}
