package project.bomberboys.game.actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JTextField;

import project.bomberboys.game.Game;
import project.bomberboys.game.GameObject;
import project.bomberboys.game.blocks.Block;
import project.bomberboys.sockets.ChatSocket;

public class Player extends GameObject{
	
	private boolean chatActive;
	private JTextField chatField;

	public Player(Game game, float x, float y, ChatSocket socket) {
		super(game, socket.getUsername(), x, y);
		this.speed = 0.1f;
		this.chatActive = false;
		this.chatField = socket.getChatField();
	}
	
	public void stop(int k) {
		switch(k) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			velY = 0;
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			velX = 0;
			break;
		case KeyEvent.VK_ENTER:
			velX = velY = 0;
			break;
		}
	}
	
	public void act(int k) {
		switch(k) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			velY = -speed;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			velY = speed;
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			velX = -speed;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			velX = speed;
			break;
		case KeyEvent.VK_ENTER:
			chatActive = true;
			if(chatActive) {
				stop(k);
				chatField.requestFocusInWindow();
			}
			chatActive = false;
			break;
		case KeyEvent.VK_ESCAPE:
			game.stop();
			break;
		}
	}
	
	public void update() {
		x += velX;
		y += velY;
		
		collide();
	}
	
	public void collide() {
		LinkedList<Block> blocks = game.getField().getBlocks();
		
		for(int i = 0; i <  blocks.size(); i++) {
			Block block = blocks.get(i);
			if(block.getBounds().intersects(getBoundsBot())) {
				this.y = block.getY() - 1;
			} else if(block.getBounds().intersects(getBoundsTop())) {
				this.y = block.getY() + 1;
			} else if(block.getBounds().intersects(getBoundsLeft())) {
				this.x = block.getX() + 1;
			} else if(block.getBounds().intersects(getBoundsRight())) {
				this.x = block.getX() - 1;
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
//		int 
//			playerX = (int)((x - (int)x <= 0.5f) ? x : x + 1),
//			playerY = (int)((y - (int)y <= 0.5f) ? y : y + 1);
		g.fillRect((int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize()* game.getScale());
	}
	
	public void destroy() {
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)(x * game.getObjectSize()) * game.getScale(), (int)(y * game.getObjectSize()) * game.getScale(), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}
	
	public Rectangle getBoundsBot() {
		return new Rectangle((int)(x * game.getObjectSize() + game.getObjectSize()/4) * game.getScale(), (int)(y * game.getObjectSize() + (game.getObjectSize() - game.getObjectSize()/4))  * game.getScale(), game.getObjectSize()/2 * game.getScale(), game.getObjectSize()/4 * game.getScale());
	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle((int)(x * game.getObjectSize() + game.getObjectSize()/4) * game.getScale(),(int) (y * game.getObjectSize()) * game.getScale(), game.getObjectSize()/2 * game.getScale(), game.getObjectSize()/4 * game.getScale());
	}
	
	public Rectangle getBoundsLeft() {
		return new Rectangle((int)(x * game.getObjectSize()) * game.getScale(), (int)(y * game.getObjectSize() + (game.getObjectSize()/4)) * game.getScale(), game.getObjectSize()/4 * game.getScale(), game.getObjectSize()/2 * game.getScale());
	}
	
	public Rectangle getBoundsRight() {
		return new Rectangle((int)(x * game.getObjectSize() + game.getObjectSize() - game.getObjectSize()/4) * game.getScale(), (int)(y * game.getObjectSize() + (game.getObjectSize()/4)) * game.getScale(), game.getObjectSize()/4 * game.getScale(), game.getObjectSize()/2 * game.getScale());
	}

}
