package project.bomberboys.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

//Still not connected to other parts of the game

public class Multicast implements Runnable{
	
	MulticastSocket socket;
	int port = 4445;
	InetAddress group = InetAddress.getByName("225.0.0.0");
	String object;

	// Object still a placeholder, should be the player object to be broadcasted 
	public Multicast (String object) throws IOException{
		
		socket = new MulticastSocket(port);
		socket.joinGroup(group);
		this.object = object;
		
		start();
	}
	
	public void start() {
		try {
			new Thread(this).start();
		} catch (Exception e) {
			
		}
	}
	
	public void broadcast() throws IOException {
		DatagramPacket broadcast = new DatagramPacket(object.getBytes(), object.length(), group, port);
		socket.send(broadcast);
	}
	
	public void receive() throws IOException {
		byte buffer[] = new byte[1000];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
	}
	
	public void update(String object) {
		this.object = object;
	}

	public void run() {
		while(true){
			try {
				broadcast();
				receive();
				//updateGameState(); update game state after receiving packet
			} catch (IOException e) {
	
			}
		}
	}
}
