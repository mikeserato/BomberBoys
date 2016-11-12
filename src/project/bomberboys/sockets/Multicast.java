package project.bomberboys.sockets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class Multicast implements Runnable {
	
	MulticastSocket socket;
	int port = 4445;
	InetAddress group = InetAddress.getByName("225.0.0.0");
	ObjectPacket object;

	public Multicast (ObjectPacket object) throws IOException{
		
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
				update.getX();
				update.getX();
				// Place Update on Map
			} catch (IOException e) {
	
			}
		}
	}
}
