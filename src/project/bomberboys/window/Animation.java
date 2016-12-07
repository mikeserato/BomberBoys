package project.bomberboys.window;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import project.bomberboys.game.Game;

public class Animation {
	private int speed, index = 0, count = 0, frames;

	private BufferedImage[] images;
	private BufferedImage image;
	private Game game;

	public Animation(Game game, int speed, BufferedImage... images) {
		this.speed = speed;
		this.images = new BufferedImage[images.length];
		this.frames = images.length;
		this.game = game;
		for(int i = 0; i < images.length; i++) {
			this.images[i] = images[i];
		}
		this.image = images[0];
	}

	public Animation(int speed, BufferedImage... Images) {
		this.speed = speed;
		this.images = new BufferedImage[images.length];
		this.frames = images.length;
		for(int i = 0; i < images.length; i++) {
			this.images[i] = images[i];
		}
		this.image = images[0];
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void restart() {
		count = index = 0;
		nextFrame();
	}

	public void animate() {
		count++;
		if(count > speed) {
			count = 0;
			nextFrame();
		}
	}

	public void nextFrame() {
		for(int i = 0; i < frames; i++) {
			if(i == index) {
				image = images[i];
				break;
			}
		}
		index++;

		index = index % frames;
	}

	public void drawAnimation(Graphics g, float x, float y) {
		Graphics2D g2d = (Graphics2D) g;
		if(g2d == null) {
			System.out.println("Graphics printer is null");
		} else if(image == null) {
			System.out.println("Current image is null");
		} else if(game == null) {
			System.out.println("Game is null");
		} else {
			g2d.drawImage(image, (int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize()* game.getScale(), null);
		}
	}

	public void drawAnimation(Graphics g, float x, float y, int scaleX, int scaleY){
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, (int)(x * game.getObjectSize()) * game.getScale(), (int)(y * game.getObjectSize()) * game.getScale(), game.getObjectSize() * game.getScale() * scaleX, game.getObjectSize() * game.getScale() * scaleY, null);
	}

	public void drawAnimation(Graphics g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, width, height, null);
	}
}
