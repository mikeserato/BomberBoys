package project.bomberboys;

import java.awt.CardLayout;
import java.awt.Color;
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
	//private JPanel bgPanel = new BgPanel();
	private JTextField playerCountTF, roundCountTF, ipTF, portTF, username;
	private boolean server;
	//MyPanel panel;
    //private BufferedImage img;
    //private static BufferedImageLoader imageLoader = new BufferedImageLoader();

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
		serverPanel.setOpaque(false);
		JPanel subPanel;
		JLabel label;

		//top
		subPanel = new JPanel(new FlowLayout());
		subPanel.setOpaque(false);
		label = new JLabel("Number of Players:");
		label.setForeground(Color.WHITE);
		subPanel.add(label);
		playerCountTF = new JTextField(10);
		playerCountTF.setText("2");
		subPanel.add(playerCountTF);
		serverPanel.add(subPanel);

		//upper mid
		subPanel = new JPanel(new FlowLayout());
		subPanel.setOpaque(false);
		label = new JLabel("Number of Rounds:");
		label.setForeground(Color.WHITE);
		subPanel.add(label);
		roundCountTF = new JTextField(10);
		roundCountTF.setText("3");
		subPanel.add(roundCountTF);
		serverPanel.add(subPanel);

		//lower mid
		subPanel = new JPanel(new FlowLayout());
		subPanel.setOpaque(false);
		label = new JLabel("Server port:");
		label.setForeground(Color.WHITE);
		subPanel.add(label);
		portTF = new JTextField(10);
		portTF.setText("8000");
		subPanel.add(portTF);
		serverPanel.add(subPanel);

		//bot
		subPanel = new JPanel(new FlowLayout());
		subPanel.setOpaque(false);
		subPanel.add(createButton("Back", "menu", this, true));
		subPanel.add(createButton("Start!", "menu", this, true));
		serverPanel.add(subPanel);
	}

	private void initClientPanel() {
		clientPanel = new JPanel(new GridLayout(3, 1));
		clientPanel.setOpaque(false);
		JPanel subPanel;
		JLabel label;

		//top
		subPanel = new JPanel(new FlowLayout());
		subPanel.setOpaque(false);
		label = new JLabel("Server IP address:");
		label.setForeground(Color.WHITE);
		subPanel.add(label);
		ipTF = new JTextField(10);
		ipTF.setText("localhost");
		subPanel.add(ipTF);
		clientPanel.add(subPanel);

		//mid
		subPanel = new JPanel(new FlowLayout());
		subPanel.setOpaque(false);
		label = new JLabel("Server port:");
		label.setForeground(Color.WHITE);
		subPanel.add(label);
		portTF = new JTextField(10);
		portTF.setText("8000");
		subPanel.add(portTF);
		clientPanel.add(subPanel);

		//bot
		subPanel = new JPanel(new FlowLayout());
		subPanel.setOpaque(false);
		subPanel.add(createButton("Back", "menu", this, true));
		subPanel.add(createButton("Connect", "menu", this, true));
		clientPanel.add(subPanel);
		
		
	}

	private void initMainOptionPanel() {
		mainOptionPanel = new JPanel();
		mainOptionPanel.setOpaque(false);
		mainOptionPanel.setLayout(new FlowLayout());
		mainOptionPanel.add(createButton("Start as server", "menu", this, true));
		mainOptionPanel.add(createButton("Start as client", "menu", this, true));

		username = new JTextField(20);
		username.setText("Anonymous");
		mainOptionPanel.add(username);

		mainOptionPanel.setBackground(Color.BLACK);
		mainOptionPanel.setForeground(Color.WHITE);

        //bgPanel.setLayout(new BorderLayout());
        //bgPanel.add(mainOptionPanel, BorderLayout.CENTER);

        //FrameTestBase t = new FrameTestBase();
        //menuWindow.setContentPane(bgPanel);
        //mainOptionPanel.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //t.setSize(250, 100);
        //t.setVisible(true);
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

		//img = imageLoader.load("/img/interface/titlescreenClean.png");
		//panel= new MyPanel();
		//menuWindow.add(panel);
		//menuWindow.setContentPane(bgPanel);

		menuWindow.getContentPane().setBackground(Color.BLACK);
		menuWindow.setLayout(new CardLayout());
		menuWindow.setSize(300, 150);
		//menuWindow.setSize(666, 450);
		menuWindow.setResizable(false);
		menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private Button createButton(String text, String type, MainBoom main, boolean enabled) {
		Button button = new Button(type, main);
		button.setText(text);
		button.setEnabled(enabled);
		button.setBackground(Color.RED);
		button.setForeground(Color.WHITE);
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

	public void init(MainBoom main) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				main.display();	//constantly checks for the display of the bomber man display
			}
		});
	}

//	private class MyPanel extends JPanel{
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
//        }
//    }

//	class BgPanel extends JPanel {
//		BufferedImage img = imageLoader.load("/img/interface/titlescreenClean.png");
//	    @Override
//	    public void paintComponent(Graphics g) {
//	        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
//	    }
//	}
}
