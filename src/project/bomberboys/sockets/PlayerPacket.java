package project.bomberboys.sockets;

public class PlayerPacket extends ObjectPacket {
	
	private static final long serialVersionUID = 1L;
	protected int index;

	public PlayerPacket(float x, float y, int index) {
		super(x, y, index);

	}

}
