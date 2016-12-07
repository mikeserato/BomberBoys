package project.bomberboys.sockets;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import project.bomberboys.MainBoom;
import project.bomberboys.game.Game;

public class Server extends ChatSocket {

	private int clientsAccepted = 0, playerCount, port, index;
	private EchoServer[] servers;

	public Server(int playerCount, int roundCount, int port, MainBoom main) {
		super(main);
		this.playerCount = playerCount - 1;
		servers = new EchoServer[playerCount - 1];
//	this.roundCount = roundCount;
		this.port = port;
		this.username = main.getUsername();


		gameWindow = new JFrame("Boom! Server - User: " + this.username);
//		gameWindow.setSize(new Dimension(800, 400));
		gameWindow.setResizable(false);
		gameWindow.setLayout(new BorderLayout());

		chatPanel = new JPanel(new BorderLayout());
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

		chatField = new JTextField(20);
		chatField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = e.getActionCommand();
				if(!message.equals("")) {
					showMessage(message, true);
					broadcast(message, username, index);
					chatField.setText("");
				}
				game.requestFocusInWindow();
			}
		});

		chatPanel.add(chatField, BorderLayout.SOUTH);
		gameWindow.add(chatPanel, BorderLayout.WEST);

		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game = new Game(this);
		main.getMenuWindow().setVisible(false);
		gameWindow.pack();

		display();
		start();
	}

	public void display() {
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
	}

	public void broadcast(String message, String sender, int index) {
		System.out.println("Broadcasting: " + message);
		for(int i = 0; i < playerCount; i++) {
			if(index != i) {
				servers[i].sendMessage(message, sender, index);
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
			servers[clientsAccepted] = new EchoServer(socket, this, clientsAccepted);
			servers[clientsAccepted].start();
		}
		index = playerCount;
		game.setIndex(index);
		
		this.broadcast("::Create Players:: - " + (playerCount + 1), "", index);
		game.createPlayers(playerCount + 1);
		
		Random rand = new Random();
		int terrain = rand.nextInt(7) + 1;
		this.broadcast("::Create Field:: - " + terrain, "", index);
		game.createField(terrain);
		System.out.println(servers.length);
		
		boolean allTrue = false;
		while(!allTrue) {
			boolean hasFalse = false;
			for(int i = 0; i < servers.length; i++) {
				if(!servers[i].isFieldCreated()) {
					hasFalse = true;
					break;
				}
			}
			allTrue = !hasFalse;
			System.out.println(allTrue);
		}
		System.out.println("loop broken");
		game.setFieldCreated(true);
		game.getField().randomizeField();
		
		System.out.println("All players are connected");

//		this.broadcast("::Game Start::", "");
//		this.showMessage("::Game Start::", false);
//		game.start();
	}

	public String getUsername() {
		return this.username;
	}

	public void clearChatField() {
		this.chatField.setText("");
	}

}
