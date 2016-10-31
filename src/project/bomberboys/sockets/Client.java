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

public class Client implements Runnable{

	private String ip; private int port;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private JFrame gameWindow;
	private JPanel chatPanel;
	private JTextArea chatArea;
	private JTextField chatField;
	private String username;
	
	public Client(String ip, int port, MainBoom main) {
		this.ip = ip;
		this.port = port;
		this.username = main.getUsername();
		
		gameWindow = new JFrame("Boom! Client - User: " + this.username);
		gameWindow.setSize(400, 300);
		gameWindow.setResizable(false);
		gameWindow.setLayout(new BorderLayout());
		
		chatPanel = new JPanel(new BorderLayout());
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
		
		chatField = new JTextField(100);
		chatField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				broadcast(e.getActionCommand());
				String message = e.getActionCommand();
				showMessage(message, true);
				sendMessage(message);
				chatField.setText("");
			}
		});
		
		chatPanel.add(chatField, BorderLayout.SOUTH);
		gameWindow.add(chatPanel, BorderLayout.WEST);
		
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.getMenuWindow().setVisible(false);
		
		
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
				if(message.equals("QUIT")) {
					
				} else {
					showMessage(sender + ": " + message, false);
//					server.broadcast(message);
				}
			} catch(Exception e) {
				
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
//		showMessage(message);
		try {
			MessagePacket packet = new MessagePacket(message, username);
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
	
}
