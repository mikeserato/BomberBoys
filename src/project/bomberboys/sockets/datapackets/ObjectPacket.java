package project.bomberboys.sockets.datapackets;

import java.io.Serializable;

public class ObjectPacket implements Serializable {
	private static final long serialVersionUID = 1L;

	protected float x;
	protected float y;
	protected int life;
	protected int score;


	public ObjectPacket(float x, float y, int life, int score) {
		this.x = x;
		this.y = y;
		this.life = life;
		this.score = score;
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

	public void update(float x, float y, int life, int score) {
		this.x = x;
		this.y = y;
		this.life = life;
		this.score = score;
	}

}
