package project.bomberboys.sockets.datapackets;


public class PlayerPacket extends ObjectPacket {
	
	private static final long serialVersionUID = 1L;
	protected int index;

	private float velX, velY;
	private String action;
	
	public PlayerPacket(float x, float y, int index) {
		super(x, y);
		this.index = index;
	}
	
	public void update(float x, float y, float velX, float velY, String action) {
		super.update(x, y);
		this.velX = velX;
		this.velY = velY;
		this.action = action;
	}
	
	public int getIndex() {
		return index;
	}
	
	public float getVelX() {
		return velX;
	}
	
	public float getVelY() {
		return velY;
	}
	
	public String getAction() {
		return action;
	}

}
