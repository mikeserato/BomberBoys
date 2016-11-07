package project.bomberboys.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import project.bomberboys.game.actors.Player;

public class GameKeyListener extends KeyAdapter {

	private Player player;
	public GameKeyListener(Player player) {
		this.player = player;
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		player.act(k);
	}
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		player.stop(k);
	}

}
