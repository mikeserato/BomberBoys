package project.bomberboys.sockets;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import project.bomberboys.MainBoom;
import project.bomberboys.game.Game;

public class Client extends ChatSocket implements Runnable{

	private String ip; private int port, index;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public Client(String ip, int port, MainBoom main) {
		super(main);
		this.ip = ip;
		this.port = port;
		this.username = main.getUsername();
		
		gameWindow = new JFrame("Boom! Client - User: " + this.username);
//		gameWindow.setSize(400, 300);
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
					sendMessage(message);
					chatField.setText("");
				}
				game.requestFocusInWindow();
			}
		});
		chatField.setEditable(false);
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
	
	public void start() {
		try {
			connect();
			setupStream();
//			chat();
			new Thread(this).start();
		} catch (Exception e) {
			
		}
	}
	
	public void run() {
		System.out.println("Started!");
		MessagePacket packet;
		String message, sender;
		sendMessage("::Connected::");
		while(true) {
			try {
				packet = (MessagePacket) input.readObject();
				message = packet.message();
				sender = packet.sender();
				System.out.println("Received: " + message);
				if(message.equals("QUIT")) {
					break;
				} else if(message.equals("::Game Start::") && sender.equals("")) {
					this.game.start();
					chatField.setEditable(true);
//					showMessage(message, false);
				} else if(message.contains("::Create Players::") && sender.equals("")) {
					int total = Integer.parseInt(message.replace("::Create Players:: - ", ""));
					game.createPlayers(total);
//					showMessage(message, false);
				} else if(message.contains("::Create Field::") && sender.equals("")) {
					int terrain= Integer.parseInt(message.replace("::Create Field:: - ", ""));
					game.createField(terrain);
				} else if(sender.equals("")) {
					index = Integer.parseInt(message);
					game.setIndex(index);
				} else {
					System.out.println("Printing message: " + message);
					showMessage(sender + ": " + message, false);
//					server.broadcast(message);
				}
			} catch(Exception e) {
				break;
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
	
	public void connect() throws Exception{
		socket = new Socket(InetAddress.getByName(ip), port);
		System.out.println("\n Connected to " + socket.getInetAddress().getHostName() + "!");
	}
	
	public void sendMessage(String message) {
		try {
			MessagePacket packet = new MessagePacket(message, username, index);
			output.writeObject(packet);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setupStream() throws IOException {
		System.out.println("Setting up streams...");
		System.out.println("Setting up output stream...");
		output = new ObjectOutputStream(socket.getOutputStream());
		output.flush();
		System.out.println("Setting up input stream...");
		input = new ObjectInputStream(socket.getInputStream());
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void clearChatField() {
		this.chatField.setText("");
	}
}
