package project.bomberboys.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.bomberboys.game.Game;
import project.bomberboys.sockets.Client;
import project.bomberboys.sockets.Server;

public class ChatActionListener implements ActionListener{

	private boolean isServer;
	private Game game;
	private Client client;
	private Server server;
	
	public ChatActionListener(boolean isServer, Game game, Server server) {
		this.isServer = isServer;
		this.game = game;
		this.server = server;
	}
	
	public ChatActionListener(boolean isServer, Game game, Client client) {
		this.isServer = isServer;
		this.game = game;
		this.client = client;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String message = e.getActionCommand();
		if(message.equals("")) {
			if(isServer) {
				server.showMessage(message, true);
				server.broadcast(message, server.getUsername());
				server.clearChatField();
			} else {
				client.showMessage(message, true);
				client.sendMessage(message);
				client.clearChatField();
			}
		}
		game.requestFocusInWindow();
	}

}
