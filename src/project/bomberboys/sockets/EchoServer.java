package project.bomberboys.sockets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EchoServer extends Thread{

	private Server server;
	private Socket clientSocket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String sender;
	private int index;
	private boolean fieldCreated;
	
	public EchoServer(Socket clientSocket, Server server, int index) {
		this.clientSocket = clientSocket;
		this.server = server;
		this.index = index;
		System.out.println("Setting up streams...");
		setupStream();
	}
	
	public void setupStream() {
		try {
			System.out.println("Setting up output stream...");
			this.output = new ObjectOutputStream(clientSocket.getOutputStream());
			this.output.flush();
			System.out.println("Setting up input stream...");
			this.input = new ObjectInputStream(clientSocket.getInputStream());
			this.sendMessage(Integer.toString(index), "", index);
		} catch(Exception e) {
			
		}
	}
	
	public void run() {
		System.out.println("Thread started!");
		MessagePacket packet;
		String message, sender;
		int index;
		while(true) {
			try {
				packet = (MessagePacket) input.readObject();
				message = packet.message();
				sender = packet.sender();
				index = packet.index();
				if(message.equals("QUIT")) {
					sendMessage("QUIT", "", index);
					break;
				} else if(message.equals("::Connected::")) {
					this.sender = sender;
				} else if(message.equals("::Field Created::")) {
					this.fieldCreated = true;
				} else {
					server.showMessage(sender + ": " + message, false);
					server.broadcast(message, sender, index);
				}
			} catch(Exception e) {
				break;
			}
		}
	}
	
	public void sendMessage(String message, String sender, int index) {
		try{
			MessagePacket packet = new MessagePacket(message, sender, index);
			output.writeObject(packet);
			output.flush();
		} catch(Exception e) {
			
		}
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public boolean isFieldCreated() {
		return this.fieldCreated;
	}
 
}
