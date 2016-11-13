package project.bomberboys.sockets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import project.bomberboys.game.Game;


public class Multicast implements Runnable {
	
	Game game;
	MulticastSocket socket;
	int port = 4445;
	InetAddress group = InetAddress.getByName("225.0.0.0");
	ObjectPacket object;

	public Multicast (Game game, ObjectPacket object) throws IOException{
		this.game = game;
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
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this.object);
//		System.out.println("sending (" + object.getX() + ", " + object.getY() + ")");
		final byte[] data = baos.toByteArray();

		final DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
		socket.send(packet);

	}
	
	public ObjectPacket receive() throws IOException {
		ObjectPacket ObjectPacket = null;
		
		byte buffer[] = new byte[6400];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		
		final ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		final ObjectInputStream ois = new ObjectInputStream(bais);
		
		try {
			ObjectPacket = (ObjectPacket)ois.readObject();
			
		} catch (ClassNotFoundException e) {

		}
		
		return(ObjectPacket);
		
	}
	
	public void update(ObjectPacket object) {
		this.object = object;
	}

	public void run() {
		while(true){
			try {
				ObjectPacket update = receive();
				System.out.println("received (" + update.getX() + ", " + update.getY() + ") for index: " + update.getIndex());
				game.getPlayers()[update.getIndex()].setX(update.getX());
				game.getPlayers()[update.getIndex()].setY(update.getY());
				// Place Update on Map
			} catch (IOException e) {
	
			}
		}
	}
}
