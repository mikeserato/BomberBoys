package project.bomberboys;

import java.awt.*;
import javax.swing.*;

import project.bomberboys.window.BufferedImageLoader;

import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Main{
	private static BufferedImageLoader imageLoader = new BufferedImageLoader();
			
	public static void main(String[] args) throws Exception{
		final JFrame frame = new JFrame("Bomberman World");
		BufferedImage titlescreen = imageLoader.load("/img/interface/titlescreenClean.png");
		
		frame.setContentPane(new JLabel(new ImageIcon(titlescreen)));

		final Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setPreferredSize(new Dimension(666, 465));

		BufferedImage startSButton = imageLoader.load("/img/interface/start.png");
		BufferedImage startSButtonHover = imageLoader.load("/img/interface/startHover.png");
		BufferedImage startSButtonActive = imageLoader.load("/img/interface/start.png");

		JButton startSBackupButton = new JButton(new ImageIcon(startSButton));
		startSBackupButton.setRolloverIcon(new ImageIcon(startSButtonHover));
		startSBackupButton.setPressedIcon(new ImageIcon(startSButtonActive));
		startSBackupButton.setBorder(BorderFactory.createEmptyBorder());
		startSBackupButton.setFocusable(false);

		BufferedImage instructButton = imageLoader.load("/img/interface/instructions.png");
		BufferedImage instructButtonHover = imageLoader.load("/img/interface/instructionsHover.png");
		BufferedImage instructButtonActive = imageLoader.load("/img/interface/instructions.png");

		JButton instructBackupButton = new JButton(new ImageIcon(instructButton));
		instructBackupButton.setRolloverIcon(new ImageIcon(instructButtonHover));
		instructBackupButton.setPressedIcon(new ImageIcon(instructButtonActive));
		instructBackupButton.setBorder(BorderFactory.createEmptyBorder());
		instructBackupButton.setFocusable(false);

		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(666,270));
		topPanel.setOpaque(false);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(startSBackupButton);
		bottomPanel.add(instructBackupButton);
		bottomPanel.setOpaque(false);

		startSBackupButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.setVisible(false);
				MainBoom main = new MainBoom();
				main.init(main);
			}
		});
		
//		startSBackupButton.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				frame.setVisible(false);
//				
//				final JFrame serverFrame = new JFrame("Server");
//		 		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		 		BufferedImage servercreen = imageLoader.load("/img/interface/serverClean.png");
//				
//		 		serverFrame.setContentPane(new JLabel(new ImageIcon(servercreen)));
//				
//		 		BufferedImage startButton = imageLoader.load("/img/interface/start.png");
//				BufferedImage startButtonHover = imageLoader.load("/img/interface/startHover.png");
//				BufferedImage startButtonActive = imageLoader.load("/img/interface/start.png");
//
//				JButton startBackupButton = new JButton(new ImageIcon(startButton));
//				startBackupButton.setRolloverIcon(new ImageIcon(startButtonHover));
//				startBackupButton.setPressedIcon(new ImageIcon(startButtonActive));
//				startBackupButton.setBorder(BorderFactory.createEmptyBorder());
//				startBackupButton.setFocusable(false);
//
//				BufferedImage backButton = imageLoader.load("/img/interface/back1.png");
//				BufferedImage backButtonHover = imageLoader.load("/img/interface/back1Hover.png");
//				BufferedImage backButtonActive = imageLoader.load("/img/interface/back1.png");
//
//				JButton backBackupButton = new JButton(new ImageIcon(backButton));
//				backBackupButton.setRolloverIcon(new ImageIcon(backButtonHover));
//				backBackupButton.setPressedIcon(new ImageIcon(backButtonActive));
//				backBackupButton.setBorder(BorderFactory.createEmptyBorder());
//				backBackupButton.setFocusable(false);
//
//				Container serverC = serverFrame.getContentPane();
//				serverC.setLayout(new BorderLayout());
//				serverC.setPreferredSize(new Dimension(666, 465));
//				serverFrame.setVisible(true);
//				serverFrame.pack();
//
//				JPanel serverPanel = new JPanel();
//				serverPanel.setPreferredSize(new Dimension(700, 130));
//				
//				JTextField username = new JTextField(20);
//				username.setText("Anonymous");
//				serverPanel.add(username);
//				
//				JTextField numPlayers = new JTextField(20);
//				numPlayers.setText("2");
//				serverPanel.add(numPlayers);
//				
//				JTextField numRounds = new JTextField(20);
//				numRounds.setText("3");
//				serverPanel.add(numRounds);
//				
//				JTextField serverPort = new JTextField(20);
//				serverPort.setText("8000");
//				serverPanel.add(serverPort);
//				
//				serverPanel.add(startBackupButton);
//				serverPanel.add(backBackupButton);
//				serverPanel.setOpaque(false);
//				serverC.add(serverPanel, BorderLayout.SOUTH);
//
//				startBackupButton.addActionListener(new ActionListener(){
//					public void actionPerformed(ActionEvent e){
//						serverFrame.setVisible(false);
//						MainBoom main = new MainBoom();
//						main.init(main);
//					}
//				});		
//				
//				backBackupButton.addActionListener(new ActionListener(){
//					public void actionPerformed(ActionEvent e){
//						serverFrame.setVisible(false);
//						frame.setVisible(true);
//					}
//				});		
//			}
//		});

		instructBackupButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				frame.setVisible(false);

				final JFrame instructFrame = new JFrame("Instructions");
				instructFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				BufferedImage manualscreen = imageLoader.load("/img/interface/manual.png");
				instructFrame.setContentPane(new JLabel(new ImageIcon(manualscreen)));

				BufferedImage backButton = imageLoader.load("/img/interface/back.png");
				BufferedImage backButtonHover = imageLoader.load("/img/interface/backHover.png");
				BufferedImage backButtonActive = imageLoader.load("/img/interface/back.png");

				JButton backBackupButton = new JButton(new ImageIcon(backButton));
				backBackupButton.setRolloverIcon(new ImageIcon(backButtonHover));
				backBackupButton.setPressedIcon(new ImageIcon(backButtonActive));
				backBackupButton.setBorder(BorderFactory.createEmptyBorder());
				backBackupButton.setFocusable(false);

				Container instructC = instructFrame.getContentPane();
				instructC.setLayout(new BorderLayout());
				instructC.setPreferredSize(new Dimension(666, 465));
				instructFrame.setVisible(true);
				instructFrame.pack();

				JPanel instructPanel = new JPanel();
				instructPanel.setPreferredSize(new Dimension(700, 130));
				instructPanel.add(backBackupButton);
				instructPanel.setOpaque(false);
				instructC.add(instructPanel, BorderLayout.SOUTH);

				backBackupButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						instructFrame.setVisible(false);
						frame.setVisible(true);
					}
				});
			}
		});

		c.add(topPanel, BorderLayout.NORTH);
		c.add(bottomPanel, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
}
