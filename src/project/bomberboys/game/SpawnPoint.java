package project.bomberboys.game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class SpawnPoint extends GameObject{

	private boolean used;
	
	public SpawnPoint(Game game, float x, float y) {
		super(game, "", x, y);
		this.used = false;
	}
	
	public boolean isUsed() {
		return used;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		
	}
	
	public void destroy() {
		
	}
	
	public Rectangle getBounds() {
		return null;
	}

}
