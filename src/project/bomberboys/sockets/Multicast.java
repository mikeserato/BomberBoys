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
import project.bomberboys.game.blocks.Block;
import project.bomberboys.game.blocks.BonusBlock;
import project.bomberboys.game.blocks.HardBlock;
import project.bomberboys.game.blocks.SoftBlock;
import project.bomberboys.game.bombs.Bomb;
import project.bomberboys.sockets.datapackets.BlockPacket;
import project.bomberboys.sockets.datapackets.BombPacket;
import project.bomberboys.sockets.datapackets.ObjectPacket;
import project.bomberboys.sockets.datapackets.PlayerPacket;


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
		final byte[] data = baos.toByteArray();

		final DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
		socket.send(packet);

	}

	public ObjectPacket receive() throws IOException {
		ObjectPacket  ObjectPacket = null;

		byte buffer[] = new byte[6400];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);

		final ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		final ObjectInputStream ois = new ObjectInputStream(bais);

		try {
			ObjectPacket = (ObjectPacket) ois.readObject();
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
				ObjectPacket packet = (ObjectPacket)receive();

				if(packet instanceof PlayerPacket){
					PlayerPacket update = (PlayerPacket) packet;
					//System.out.println("received (" + update.getX() + ", " + update.getY() + ") for index: " + update.getIndex());
					game.getPlayers()[update.getIndex()].setUser(update.getUsername());
					game.getPlayers()[update.getIndex()].setX(update.getX());
					game.getPlayers()[update.getIndex()].setY(update.getY());
					game.getPlayers()[update.getIndex()].setLife(update.getLife());
					game.getPlayers()[update.getIndex()].setScore(update.getScore());
					game.getPlayers()[update.getIndex()].setVelX(update.getVelX());
					game.getPlayers()[update.getIndex()].setVelY(update.getVelY());
					game.getPlayers()[update.getIndex()].setAction(update.getAction());
				}

				else if(packet instanceof BombPacket){
					BombPacket update = (BombPacket) packet;

					float x = packet.getX();
					float y = packet.getY();

					int playerX = (int)((x - (int)x <= 0.5f) ? x : x + 1);
					int playerY = (int)((y - (int)y <= 0.5f) ? y : y + 1);

					if(game.getGameBoard()[playerY][playerX] != 'o'){
						game.getGameBoard()[playerY][playerX] = 'o';
						Bomb b = new Bomb(game,update.getX(),update.getY(), game.getPlayers()[update.getIndex()], update.getCountDownTimer());
						game.getPlayers()[update.getIndex()].getBombs().add(b);
						game.getObjectBoard()[playerY][playerX] = b;
					}
				}

				else if(packet instanceof BlockPacket) {
					BlockPacket update = (BlockPacket) packet;

					float x = update.getX();
					float y = update.getY();
					int intX = (int)update.getX();
					int intY = (int)update.getY();

					if(game.getGameBoard()[intY][intX] == ' ') {
						Block b = null;
						if(update.getBlockType() == 0) {
							b = new SoftBlock(game, x, y, update.getIndex(), true);
						} else if(update.getBlockType() == 1) {
							b = new HardBlock(game, x, y, update.getIndex(), true);
						} else {
							b = new BonusBlock(game, x, y, update.getIndex(), update.getBonusIndex(), true);
						}
						game.getGameBoard()[intY][intX] = '#';
						game.getObjectBoard()[intY][intX] = b;
						game.getField().getBlocks().add(b);
					}


				}
			} catch (IOException e) {

			}
		}
	}
}
