package project.bomberboys.sockets;

import java.io.Serializable;

public class ObjectPacket implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private float x;
	private float y;
	
	public ObjectPacket(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void update(float x, float y) {
		this.x = x;
		this.y = y;
	}

}
