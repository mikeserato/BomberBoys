package project.bomberboys.game;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import project.bomberboys.game.actors.Player;
import project.bomberboys.game.bombs.Bomb;
import project.bomberboys.listeners.GameKeyListener;
import project.bomberboys.sockets.ChatSocket;
import project.bomberboys.sockets.Server;
import project.bomberboys.window.BGSoundLoader;
import project.bomberboys.window.Camera;
import project.bomberboys.window.SoundLoader;


public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private final int HEIGHT = 400, WIDTH = 400, OBJECTSIZE = 12, scale = 2;
	private final int boardHeight = 31, boardWidth = 31;

	private JFrame gameFrame;
	private JPanel gamePanel;
	private Camera camera;
	private PlayerStatus statusCanvas;
	private SoundLoader soundLoader;
	private BGSoundLoader bgSoundLoader;

	private Player player;
	private Player players[];

	private ChatSocket socket;

	private char[][] gameBoard;
	private GameObject[][] objectBoard;
	private Field field;
	private LinkedList<Bomb> allBombs;

	private int index, total;
	private String frameTitle;
	private boolean running = false, server;
	private boolean fieldCreated;

	public Game(ChatSocket socket) {
		this.socket = socket;
		if(socket instanceof Server) server = true;
		this.gameFrame = socket.getGameWindow();
		this.frameTitle = gameFrame.getTitle();

		Container c = this.gameFrame.getContentPane();

		gamePanel = new JPanel(new BorderLayout());
		this.setSize(new Dimension(WIDTH, HEIGHT));
		gamePanel.add(this, BorderLayout.CENTER);

		statusCanvas = new PlayerStatus(this, new Dimension(WIDTH, HEIGHT/8));
		gamePanel.add(statusCanvas, BorderLayout.NORTH);

		c.add(gamePanel, BorderLayout.CENTER);

		//System.out.println("Created game instance");

		camera = new Camera(0, 0, this);
		soundLoader = new SoundLoader();
		bgSoundLoader = new BGSoundLoader();

		Dimension fieldSize = Field.checkField();
		this.gameBoard = new char[fieldSize.height][fieldSize.width];
		this.objectBoard = new GameObject[fieldSize.height][fieldSize.width];
	}

	public void start() {
		if(running) return;
		else {
			running = true;
			new Thread(this).start();
		}
	}

	public void stop() {
		if(!running) return;
		else {
			running = false;
			gameFrame.dispose();
			this.socket.getMain().display();
		}
	}

	public void createPlayers(int total) {
		System.out.println("creating players");
		players = new Player[total];
		this.total = total;
		player = new Player(this, 1, 1, 0, socket);
		for(int i = 0; i < this.total; i++) {
			if(i == index) {
				players[i] = player;
			} else {
				players[i] = new Player(this, 1, 1, 0);
			}
		}
		statusCanvas.init();
	}

	public void createField(int terrain) {
		System.out.println("creating field");
		field = new Field(this, terrain);
	}

	public void spawnPlayers() {
		for(int i = 0; i < total; i++) {
			players[i].respawn();
		}
	}

	public void run() {

		allBombs = new LinkedList<Bomb>();
		this.addKeyListener(new GameKeyListener(player));
		System.out.println("Game Thread " + index + " started");
		spawnPlayers();
		this.requestFocusInWindow();

		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ms = 1000000000/ amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;

		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ms;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				gameFrame.setTitle(frameTitle + " | FPS/U:" + frames + "/" + updates);
				frames = 0;
				updates = 0;
			}
		}
//		this.stop();
	}

	/** GAME ENGINE METHODS **/
	public void update() {
		//update game objects here
		field.update();

		int counter = 0;
		int winnerIndex = 0;
		String winnerName = "";
		//checks if only one 1 player has life of > -1
		for(int i = 0; i < total; i++) {
			if(players[i].getLife() > 0) {
				counter++;
				winnerName = players[i].getUsername();
				winnerIndex = i;
			}
		}

		if(counter == 1) {
			players[winnerIndex].increaseScore();
			int roundsToWin = (socket.getMain().getRoundCountTF()/2)+1;
			if(players[winnerIndex].getScore() == roundsToWin) {
				JFrame endFrame = new JFrame("End of Game");
				
				endFrame.getContentPane().setBackground(Color.BLACK);
				UIManager.put("OptionPane.background", Color.BLACK);
				UIManager.put("Panel.background", Color.BLACK);
				UIManager.put("OptionPane.messageForeground", Color.WHITE);
				JOptionPane.showMessageDialog(endFrame, winnerName + " is the winner!","End of Game",JOptionPane.INFORMATION_MESSAGE);
				for(int i = 0; i < total; i++) stop();
			} else {
				JFrame roundFrame = new JFrame("End of Round");
				roundFrame.getContentPane().setBackground(Color.BLACK);
				UIManager.put("OptionPane.background", Color.BLACK);
				UIManager.put("Panel.background", Color.BLACK);
				UIManager.put("OptionPane.messageForeground", Color.WHITE);
				JOptionPane.showMessageDialog(roundFrame, winnerName + " is the winner for this round!","End of Round",JOptionPane.INFORMATION_MESSAGE);
				for(int i = 0; i < total; i++) players[i].replenishLife();
			}
		} else {
			for(int i = 0; i < total; i++) {
				players[i].update();
			}
			camera.update(player);
		}
	}

	public void render() {
		Toolkit.getDefaultToolkit().sync();
		BufferStrategy gameBuffer = this.getBufferStrategy();

		statusCanvas.render();

		if(gameBuffer == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics gameGraphics = gameBuffer.getDrawGraphics(); //to be passed to objects for drawing
		Graphics2D gameGraphics2D = (Graphics2D) gameGraphics;

		/*
		 * Place all rendering stuff below
		 */
		gameGraphics.setColor(Color.black);
		gameGraphics.drawImage(field.getTerrain(), 0, 0, getWidth() * getScale(), getHeight() * getScale(), null);

		gameGraphics2D.translate(camera.getX(), camera.getY());

		field.render(gameGraphics);
		for(int i = 0; i < total; i++) {
			if(i == index) continue;
			players[i].render(gameGraphics);
		}
		player.render(gameGraphics);

		gameGraphics2D.translate(-camera.getX(), -camera.getY());
		/*
		 * Place all rendering stuff above
		 */

		gameGraphics.dispose();
		gameBuffer.show();
	}
	/** GAME ENGINE METHODS **/

	public int getHeight() {
		return this.HEIGHT;
	}

	public int getWidth() {
		return this.WIDTH;
	}

	public int getScale() {
		return this.scale;
	}

	public int getObjectSize() {
		return this.OBJECTSIZE;
	}

	public int getBoardHeight() {
		return this.boardHeight;
	}

	public int getBoardWidth() {
		return this.boardWidth;
	}

	public char[][] getGameBoard()  {
		return this.gameBoard;
	}

	public GameObject[][] getObjectBoard() {
		return this.objectBoard;
	}

	public Field getField() {
		return this.field;
	}

	public Player[] getPlayers() {
		return this.players;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ChatSocket getChatSocket() {
		return this.socket;
	}

	public boolean isServer() {
		return server;
	}

	public LinkedList<Bomb> getAllBombs() {
		return this.allBombs;
	}

	public boolean getFieldCreated() {
		return this.fieldCreated;
	}

	public SoundLoader getSoundLoader() {
		return this.soundLoader;
	}

	public BGSoundLoader getBGSoundLoader() {
		return this.bgSoundLoader;
	}

	public void setFieldCreated(boolean fieldCreated) {
		this.fieldCreated = fieldCreated;
	}

}
