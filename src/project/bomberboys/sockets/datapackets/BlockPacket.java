package project.bomberboys.sockets.datapackets;

public class BlockPacket extends ObjectPacket{
	private static final long serialVersionUID = 1L;
	
	private int index, blockType, bonusIndex;

	public BlockPacket(float x, float y, int index, int blockType, int life, int bonusIndex) {
		super(x, y, life);
		this.index = index;
		this.blockType = blockType;
		this.bonusIndex = bonusIndex;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getBlockType() {
		return blockType;
	}
	
	public int getBonusIndex() {
		return bonusIndex;
	}

}
