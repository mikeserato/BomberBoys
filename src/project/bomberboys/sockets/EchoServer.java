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
			this.sendMessage(Integer.toString(index), "");
		} catch(Exception e) {
			
		}
	}
	
	public void run() {
		System.out.println("Thread started!");
		MessagePacket packet;
		String message, sender;
		while(true) {
			try {
				packet = (MessagePacket) input.readObject();
				message = packet.message();
				sender = packet.sender();
				if(message.equals("QUIT")) {
					
				} else if(message.equals("::Connected::")) {
					this.sender = sender;
				} else {
					server.showMessage(sender + ": " + message, false);
					server.broadcast(message, sender);
				}
			} catch(Exception e) {
				
			}
		}
	}
	
	public void sendMessage(String message, String sender) {
		try{
			MessagePacket packet = new MessagePacket(message, sender);
			output.writeObject(packet);
//			String additional = (fromServer) ? "1" : "0";
//			output.writeObject(additional + ":" + message);
			output.flush();
		} catch(Exception e) {
			
		}
	}
	
	public String getSender() {
		return this.sender;
	}
 
}
