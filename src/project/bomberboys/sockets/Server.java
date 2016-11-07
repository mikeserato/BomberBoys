package project.bomberboys.sockets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import project.bomberboys.MainBoom;
import project.bomberboys.game.Game;

public class Server {

	private int clientsAccepted = 0, playerCount, port;
	private EchoServer[] servers;
	private JFrame gameWindow;
	private JPanel chatPanel;
	private JTextArea chatArea;
	private JTextField chatField;
	private String username;
	private Game game;
	
	public Server(int playerCount, int roundCount, int port, MainBoom main) {
		this.playerCount = playerCount - 1;
		servers = new EchoServer[playerCount - 1];
//		this.roundCount = roundCount;
		this.port = port;
		this.username = main.getUsername();
		
		
		gameWindow = new JFrame("Boom! Server - User: " + this.username);
//		gameWindow.setSize(new Dimension(800, 400));
		gameWindow.setResizable(false);
		gameWindow.setLayout(new BorderLayout());
		
		chatPanel = new JPanel(new BorderLayout());
		chatPanel.setSize(new Dimension(400, 400));
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
		
		chatField = new JTextField(20);
		chatField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = e.getActionCommand();
				if(!message.equals("")) {
					showMessage(message, true);
					broadcast(message, username);
					chatField.setText("");
				}
				game.requestFocusInWindow();
			}
		});
		
		chatPanel.add(chatField, BorderLayout.SOUTH);
		gameWindow.add(chatPanel, BorderLayout.WEST);
		
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game = new Game(gameWindow, username, chatField);
//		gameWindow.add(game, BorderLayout.CENTER);
		main.getMenuWindow().setVisible(false);
		gameWindow.pack();
		
		display();
		start();
	}
	
	public void display() {
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
	}
	
	public void broadcast(String message, String sender) {
		for(int i = 0; i < playerCount; i++) {
			if(!sender.equals(servers[i].getSender())) {
				servers[i].sendMessage(message, sender);
			}
		}
	}
	
	public void showMessage(String message, boolean fromSelf) {
		SwingUtilities.invokeLater(		//set aside a thread to update part/s of the UI
			new Runnable() {
				public void run() {
					if(fromSelf) {
						chatArea.append("me: " + message + "\n");
					} else {
						chatArea.append(message + "\n");
					}
				}
			}
		);
	}
	
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch(Exception e) {
			
		}
		
		
		for(clientsAccepted = 0; clientsAccepted < playerCount; clientsAccepted++) {
			try{
				System.out.println("Waiting for connection");
				socket = serverSocket.accept();
				System.out.println("Accepted connection " + (clientsAccepted + 1));
			} catch(Exception e) {
				
			}
			servers[clientsAccepted] = new EchoServer(socket, this);
			servers[clientsAccepted].start();
		}
		System.out.println("All players are connected");
		
		this.broadcast("::Game Start::", username);
		game.start();
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void clearChatField() {
		this.chatField.setText("");
	}

}
