package project.bomberboys.game.blocks;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import project.bomberboys.game.Game;
import project.bomberboys.window.Animation;
import project.bomberboys.window.SpriteSheet;

public class HardBlock extends Block{

	private BufferedImage[] images = new BufferedImage[3];
	
	public HardBlock(Game game, float x, float y, int index, boolean dummy) {
		super(game, x, y, index, 0, 1, dummy);
		this.life = new Random().nextInt(6) + 5;
		loadImages();
		burningAnimation = new Animation(game, 15, images);
	}
	
	public void loadImages(){
		for(int i = 0; i < 3; i++){
			images[i] = SpriteSheet.grabImage(terrain, index, i + 4, 32, 32);
		}
	}
	
	public void render(Graphics g) {
		if(burning) burningAnimation.drawAnimation(g, x, y); 
		else g.drawImage(images[0], (int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale(), null);
	}

}
