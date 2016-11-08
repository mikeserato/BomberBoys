package project.bomberboys.window;

import project.bomberboys.game.Game;
import project.bomberboys.game.actors.Player;

public class Camera {

	private float x, y;
	private Game game;
	
	public Camera(float x, float y, Game game) {
		this.x = x;
		this.y = y;
		this.game = game;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void update(Player player) {
		x = -player.getX() * game.getObjectSize() * game.getScale() + (game.getWidth() / 2 - (game.getObjectSize() * game.getScale()) / 2);
		y = -player.getY() * game.getObjectSize() * game.getScale() + (game.getHeight() / 2 - (game.getObjectSize() * game.getScale()) / 2);
	}

}
