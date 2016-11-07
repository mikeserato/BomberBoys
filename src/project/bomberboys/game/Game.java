package project.bomberboys.game;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JTextField;

import project.bomberboys.game.actors.Player;
import project.bomberboys.listeners.GameKeyListener;


public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private final int HEIGHT = 400, WIDTH = 400, OBJECTSIZE = 12;
	private boolean running = false;
	private Player player;
	private JFrame gameFrame;
	private String frameTitle;

	public Game(JFrame gameFrame, String user, JTextField chatField) {
		this.gameFrame = gameFrame;
		this.frameTitle = gameFrame.getTitle();
		Container c = this.gameFrame.getContentPane();
//		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setSize(new Dimension(WIDTH, HEIGHT));
		c.add(this, BorderLayout.CENTER);
//		this.gameFrame.add(this, BorderLayout.CENTER);
		System.out.println("Created game instance");
//		start();
		player = new Player(this, user, WIDTH/2, HEIGHT/2, chatField);
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
		this.stop();
	}
	
	/** GAME ENGINE METHODS **/
	public void update() {
		//update game objects here
		player.update();
	}
	
	public void render() {
		BufferStrategy gameBuffer = this.getBufferStrategy();
		
		if(gameBuffer == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics gameGraphics = gameBuffer.getDrawGraphics(); //to be passed to objects for drawing
		
		/*
		 * Place all rendering stuff below
		 */
		gameGraphics.setColor(Color.GRAY);
		gameGraphics.fillRect(0, 0, WIDTH, HEIGHT);
		player.render(gameGraphics);
		/*
		 * Place all rendering stuff above
		 */
		
		gameGraphics.dispose();
		gameBuffer.show();
	}
	
	public int getHeight() {
		return this.HEIGHT;
	}
	
	public int getWidth() {
		return this.WIDTH;
	}
	
	public int getObjectSize() {
		return this.OBJECTSIZE;
	}
	
	
	

}
