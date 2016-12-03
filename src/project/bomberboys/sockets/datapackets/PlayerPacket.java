package project.bomberboys.sockets.datapackets;


public class PlayerPacket extends ObjectPacket {
	
	private static final long serialVersionUID = 1L;
	protected int index;
	
	private String username;
	private float velX, velY;
	private String action;
	private int score;
	
	public PlayerPacket(String username, float x, float y, int life, int score, int index) {
		super(x, y, life);
		this.index = index;
		this.username = username;
		this.score = score;
	}
	
	public void update(float x, float y, int life, int score, float velX, float velY, String action) {
		super.update(x, y, life);
		this.velX = velX;
		this.velY = velY;
		this.action = action;
		this.score = score;
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
	
	public String getUsername() {
		return username;
	}
	
	public int getScore() {
		return this.score;
	}

}
