package project.bomberboys.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import project.bomberboys.game.actors.Player;
import project.bomberboys.window.BufferedImageLoader;
import project.bomberboys.window.SpriteSheet;

public class PlayerStatus extends Canvas {
	private static final long serialVersionUID = 1L;

	private Game game;
	private Player plyr;
	
	private int scale;
	
	private BufferedImage playerStats[], numbers[], timeMarker, bombTypes[];
	private BufferedImageLoader imageLoader = new BufferedImageLoader();
	
	public PlayerStatus(Game game, Dimension size) {
		this.setSize(size);
		this.game = game;
		this.scale = game.getScale();
	}
	
	public void init() {
		this.plyr = this.game.getPlayers()[game.getIndex()];
		BufferedImage numberSprite = imageLoader.load("/img/misc/number.png/");
		timeMarker = imageLoader.load("/img/misc/number.png");
		timeMarker = SpriteSheet.grabImage(timeMarker, 2, 2, 8, 14);
		
		numbers = new BufferedImage[10];
		
		for(int i = 0; i < 10; i++) {
			numbers[i] = SpriteSheet.grabImage(numberSprite, 1, i + 1, 8, 14);
		}
		
		BufferedImage bonuses = imageLoader.load("/img/object/bonus.png");
		playerStats = new BufferedImage[4];
		for(int j = 0; j < 4; j++) {
			playerStats[j] = SpriteSheet.grabImage(bonuses, 1, j + 1, 32, 32);
		}
		
		bombTypes = new BufferedImage[4];
		for(int i = 0; i < 4; i++) {
			bombTypes[i] = SpriteSheet.grabImage(bonuses, 1, i + 5, 32, 32);
		}
	}
	
	public void render() {
		Toolkit.getDefaultToolkit().sync();
		
		BufferStrategy statusBuffer = this.getBufferStrategy();
		
		if(statusBuffer == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics statusGraphics = statusBuffer.getDrawGraphics();
		
		statusGraphics.setColor(Color.darkGray);
		statusGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		statusGraphics.drawImage(numbers[plyr.getLife()], 9 * scale, 4 * scale, 8 * scale, 14 * scale,null);
		
		// Fire power
		statusGraphics.drawImage(playerStats[1], 22 * scale, 1 * scale + 5, 16 * scale, 16 * scale, null); 					// + 13
		statusGraphics.drawImage(numbers[plyr.getFirePower() / 10], 40 * scale, 4 * scale, 8 * scale, 14 * scale,null);		// + 18
		statusGraphics.drawImage(numbers[plyr.getFirePower() % 10], 48 * scale, 4 * scale, 8 * scale, 14 * scale,null);		// + 8
		
		// Bomb count
		statusGraphics.drawImage(playerStats[0], 61 * scale, 1 * scale + 5, 16 * scale, 16 * scale, null);
		statusGraphics.drawImage(numbers[plyr.getBombLimit() / 10], 79 * scale, 4 * scale, 8 * scale, 14 * scale,null);
		statusGraphics.drawImage(numbers[plyr.getBombLimit() % 10], 87 * scale, 4 * scale, 8 * scale, 14 * scale,null);

		// Speed or Boots
		statusGraphics.drawImage(playerStats[3], 100 * scale, 1 * scale + 5, 16 * scale, 16 * scale, null);
		statusGraphics.drawImage(numbers[plyr.getBoots() / 10], 118 * scale, 4 * scale, 8 * scale, 14 * scale,null);
		statusGraphics.drawImage(numbers[plyr.getBoots() % 10], 126 * scale, 4 * scale, 8 * scale, 14 * scale,null);
		
		statusGraphics.dispose();
		statusBuffer.show();
	}

}
