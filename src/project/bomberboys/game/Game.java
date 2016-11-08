package project.bomberboys.game;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import project.bomberboys.game.actors.Player;
import project.bomberboys.listeners.GameKeyListener;
import project.bomberboys.sockets.ChatSocket;
import project.bomberboys.window.Camera;


public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private final int HEIGHT = 400, WIDTH = 400, OBJECTSIZE = 12, scale = 3;
	private boolean running = false;
	private Player player;
	private JFrame gameFrame;
	private String frameTitle;
	private ChatSocket socket;
	private Camera camera;
	private final int boardHeight = 5, boardWidth = 5;
	private char[][] gameBoard;
	private GameObject[][] objectBoard;
	private Field field;

	public Game(ChatSocket socket) {
		this.socket = socket;
		this.gameFrame = socket.getGameWindow();
		this.frameTitle = gameFrame.getTitle();
		Container c = this.gameFrame.getContentPane();
//		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setSize(new Dimension(WIDTH, HEIGHT));
		c.add(this, BorderLayout.CENTER);
//		this.gameFrame.add(this, BorderLayout.CENTER);
		System.out.println("Created game instance");
//		start();
		player = new Player(this, 1, 1, socket);
		camera = new Camera(0, 0, this);
		field = new Field(this);
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
	
	public void run() {
		this.addKeyListener(new GameKeyListener(player));
		System.out.println("Game Thread started");
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
		player.update();
		camera.update(player);
	}
	
	public void render() {
//		Toolkit.getDefaultToolkit().sync();
		BufferStrategy gameBuffer = this.getBufferStrategy();
		
		if(gameBuffer == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics gameGraphics = gameBuffer.getDrawGraphics(); //to be passed to objects for drawing
		Graphics2D gameGraphics2D = (Graphics2D) gameGraphics;
		
		/*
		 * Place all rendering stuff below
		 */
		gameGraphics.setColor(Color.BLACK);
		gameGraphics.fillRect(0, 0, WIDTH, HEIGHT);
		
		gameGraphics2D.translate(camera.getX(), camera.getY());
		
		field.render(gameGraphics);
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

}
