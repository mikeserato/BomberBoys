package project.bomberboys.sockets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import project.bomberboys.MainBoom;
import project.bomberboys.game.Game;

public class ChatSocket {

	protected JFrame gameWindow;
	protected JPanel chatPanel;
	protected JTextArea chatArea;
	protected JTextField chatField;
	protected String username;
	protected Game game;
	protected boolean isServer = false;
	protected MainBoom main;
	
	public ChatSocket(MainBoom main) {
		this.main = main;
	}
	
	public JFrame getGameWindow() {
		return this.gameWindow;
	}
	
	public JTextField getChatField() {
		return this.chatField;
	}
	
	public MainBoom getMain() {
		return this.main;
	}
	
	public String getUsername() {
		return this.username;
	}

}
