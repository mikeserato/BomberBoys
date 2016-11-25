package project.bomberboys.game;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {

	protected String user;
	protected Game game;
	protected float x, y, velX, velY, speed;
	protected int life, score;

	public GameObject(Game game, String user, float x, float y) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.user = user;
	}

	public String getUser() {
		return this.user;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public int getLife() {
		return this.life;
	}

	public int getScore() {
		return this.score;
	}

	public float getVelX() {
		return this.velX;
	}

	public float getVelY() {
		return this.velY;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public abstract void update();
	public abstract void render(Graphics g);
	public abstract void destroy();
	public abstract Rectangle getBounds();

}
