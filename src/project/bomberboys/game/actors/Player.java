package project.bomberboys.game.actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import project.bomberboys.game.Game;
import project.bomberboys.game.GameObject;

public class Player extends GameObject{
	
	private boolean chatActive;
	private JTextField chatField;

	public Player(Game game, String user, float x, float y, JTextField chatField) {
		super(game, user, x, y);
		this.speed = 1f;
		this.chatActive = false;
		this.chatField = chatField;
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
		}
	}
	
	public void update() {
		x += velX;
		y += velY;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		int 
			playerX = (int)((x - (int)x <= 0.5f) ? x : x + 1),
			playerY = (int)((y - (int)y <= 0.5f) ? y : y + 1);
		g.fillRect(playerX, playerY, game.getObjectSize(), game.getObjectSize());
	}
	
	public void destroy() {
		
	}
	
	public Rectangle getBounds() {
		return null;
	}

}
