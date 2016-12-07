package project.bomberboys.sockets;

import java.io.Serializable;

public class MessagePacket implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String sender;
	private int index;
	public MessagePacket(String message, String sender, int index) {
		this.message = message;
		this.sender = sender;
		this.index = index;
	}
	
	public String message() {
		return this.message;
	}
	
	public String sender() {
		return this.sender;
	}
	
	public int index() {
		return this.index;
	}

}
