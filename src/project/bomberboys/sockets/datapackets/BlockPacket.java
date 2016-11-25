package project.bomberboys.sockets.datapackets;

public class BlockPacket extends ObjectPacket{
	private static final long serialVersionUID = 1L;
	
	private int index, blockType, life;
	private boolean burning;
	private long burningTimer;

	public BlockPacket(float x, float y, int index, int blockType, int life, int score, boolean burning, long burningTimer) {
		super(x, y, life, score);
		this.index = index;
		this.blockType = blockType;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getBlockType() {
		return blockType;
	}
	
	public int getLife() {
		return life;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean isBurning() {
		return burning;
	}
	
	public long getBurningTimer() {
		return burningTimer;
	}
	
	public void update(int life, boolean burning, long burningTimer) {
		this.life = life;
		this.burning = burning;
		this.burningTimer = burningTimer;
	}

}
