package project.bomberboys.sockets;

import java.io.Serializable;

public class ObjectPacket implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private float x;
	private float y;
	private int index;
	
	public ObjectPacket(float x, float y, int index) {
		this.x = x;
		this.y = y;
		this.index = index;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void update(float x, float y) {
		this.x = x;
		this.y = y;
	}

}
