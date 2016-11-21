package project.bomberboys;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import project.bomberboys.window.Button;

public class MainBoom {

	private JFrame menuWindow;
	private JPanel mainOptionPanel, serverPanel, serverWaitPanel, clientPanel, clientWaitPanel, errorPanel;
	private JTextField playerCountTF, roundCountTF, ipTF, portTF, username;
	private boolean server;

	public MainBoom() {
		server = false;
		initializeMenuWIndow();
		initializeContents();
		display();
	}

	public void display() {
		menuWindow.setLocationRelativeTo(null);
		menuWindow.setVisible(true);
	}

	private void initializeContents() {
		initMainOptionPanel();
		menuWindow.add(mainOptionPanel, "mainOptionPanel");
		initServerPanel();
		menuWindow.add(serverPanel, "serverPanel");
		initErrorPanel();
		menuWindow.add(errorPanel, "errorPanel");
		initClientPanel();
		menuWindow.add(clientPanel, "clientPanel");
		initWaitPanels();
		menuWindow.add(serverWaitPanel, "serverWaitPanel");
		menuWindow.add(clientWaitPanel, "clientWaitPanel");
	}

	private void initWaitPanels() {
		serverWaitPanel = new JPanel();
		serverWaitPanel.add(new JLabel("Waiting for players"));
		clientWaitPanel = new JPanel();
		clientWaitPanel.add(new JLabel("Connecting to server"));
	}

	private void initServerPanel() {
		serverPanel = new JPanel(new GridLayout(4, 1));
		JPanel subPanel;
		JLabel label;
		//top
		subPanel = new JPanel(new FlowLayout());
		label = new JLabel("Number of Players:");
		subPanel.add(label);
		playerCountTF = new JTextField(10);
		playerCountTF.setText("4");
		subPanel.add(playerCountTF);
		serverPanel.add(subPanel);

		//upper mid
		subPanel = new JPanel(new FlowLayout());
		label = new JLabel("Number of Rounds:");
		subPanel.add(label);
		roundCountTF = new JTextField(10);
		roundCountTF.setText("3");
		subPanel.add(roundCountTF);
		serverPanel.add(subPanel);

		//lower mid
		subPanel = new JPanel(new FlowLayout());
		label = new JLabel("Server port:");
		subPanel.add(label);
		portTF = new JTextField(10);
		portTF.setText("8000");
		subPanel.add(portTF);
		serverPanel.add(subPanel);

		//bot
		subPanel = new JPanel(new FlowLayout());
		subPanel.add(createButton("Back", "menu", this, true));
		subPanel.add(createButton("Start!", "menu", this, true));
		serverPanel.add(subPanel);
	}

	private void initClientPanel() {
		clientPanel = new JPanel(new GridLayout(3, 1));
		JPanel subPanel;
		JLabel label;
		//top
		subPanel = new JPanel(new FlowLayout());
		label = new JLabel("Server IP address:");
		subPanel.add(label);
		ipTF = new JTextField(10);
		ipTF.setText("localhost");
		subPanel.add(ipTF);
		clientPanel.add(subPanel);

		//mid
		subPanel = new JPanel(new FlowLayout());
		label = new JLabel("Server port:");
		subPanel.add(label);
		portTF = new JTextField(10);
		portTF.setText("8000");
		subPanel.add(portTF);
		clientPanel.add(subPanel);

		//bot
		subPanel = new JPanel(new FlowLayout());
		subPanel.add(createButton("Back", "menu", this, true));
		subPanel.add(createButton("Connect", "menu", this, true));
		clientPanel.add(subPanel);
	}

	private void initMainOptionPanel() {
		mainOptionPanel = new JPanel();
		mainOptionPanel.setLayout(new FlowLayout());
		mainOptionPanel.add(createButton("Start as server", "menu", this, true));
		mainOptionPanel.add(createButton("Start as client", "menu", this, true));
		username = new JTextField(20);
		username.setText("Anonymous");
		mainOptionPanel.add(username);
//		mainOptionPanel.add(createButton("Settings", "menu", this, true));
	}

	private void initErrorPanel() {
		JLabel label, label2;
		errorPanel = new JPanel();
		errorPanel.setLayout(new FlowLayout());
		label = new JLabel("Even number of players should have");
		label2 = new JLabel("odd number of rounds, and vice-versa.");
		errorPanel.add(label);
		errorPanel.add(label2);
		errorPanel.add(createButton("Back", "menu", this, true));
	}

	private void initializeMenuWIndow() {
		menuWindow = new JFrame("Boom!");
		menuWindow.setLayout(new CardLayout());
		menuWindow.setSize(300, 150);
		menuWindow.setResizable(false);
		menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private Button createButton(String text, String type, MainBoom main, boolean enabled) {
		Button button = new Button(type, main);
		button.setText(text);
		button.setEnabled(enabled);
		return button;
	}

	public boolean isServer() {
		return this.server;
	}

	public JFrame getMenuWindow() {
		return this.menuWindow;
	}

	public int getPlayerCountTF() {
		return Integer.parseInt(playerCountTF.getText());
	}

	public int getRoundCountTF() {
		return Integer.parseInt(roundCountTF.getText());
	}

	public int getPortTF() {
		return Integer.parseInt(portTF.getText());
	}

	public String getIPTF() {
		return ipTF.getText();
	}

	public String getUsername() {
		return username.getText();
	}

	public static void main(String args[]) {
		final MainBoom main = new MainBoom();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				main.display();	//constantly checks for the display of the bomber man display
			}
		});
	}

}
