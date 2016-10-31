package project.bomberboys.sockets;

import java.io.Serializable;

public class MessagePacket implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String sender;
	public MessagePacket(String message, String sender) {
		this.message = message;
		this.sender = sender;
	}
	
	public String message() {
		return this.message;
	}
	
	public String sender() {
		return this.sender;
	}

}
