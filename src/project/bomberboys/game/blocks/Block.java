package project.bomberboys.game.blocks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import project.bomberboys.game.Game;
import project.bomberboys.game.GameObject;
import project.bomberboys.sockets.Multicast;
import project.bomberboys.sockets.datapackets.BlockPacket;
import project.bomberboys.window.Animation;
import project.bomberboys.window.BufferedImageLoader;

public class Block extends GameObject {
	
	private Multicast udpThread;
	private BlockPacket obj;
	
	protected Animation burningAnimation;
	protected int life, intX, intY, blockType;
	protected int index;
	protected boolean burning, dummy;
	protected long burningTimer;
	protected BufferedImage terrain;
	protected BufferedImage[] terrainSprites;
	protected BufferedImageLoader imageLoader;
	

	public Block(Game game, float x, float y, int index, int bonusIndex, int blockType, boolean dummy) {
		super(game, "game", x, y);
		this.intX = (int)x;
		this.intY = (int)y;
		this.game = game;
		this.index = index;
		this.blockType = blockType;
		this.dummy = dummy;
		if(blockType == 0) {
			System.out.println(dummy);
		}
		if(!dummy && blockType != 1) {
			obj = new BlockPacket(x, y, index, blockType, life, bonusIndex);
			
			try{
				this.udpThread = new Multicast(game, obj);
			} catch (IOException e) {
				
			}
			
			broadcast();
		}
		
		imageLoader = new BufferedImageLoader();
		loadTerrain();
	}
	
	public void loadTerrain() {
		terrain = imageLoader.load("/img/field/terrain.png/");
	}

	public void update() {
		if(burning) {
			burningAnimation.animate();
			if(System.currentTimeMillis() - burningTimer >= 900) {
				game.getGameBoard()[intY][intX] = ' ';
				game.getObjectBoard()[intY][intX] = null;
				game.getField().getBlocks().remove(this);
			}
		}
	}
	
	public void broadcast() {
		try {
			udpThread.broadcast();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect((int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}

	public void destroy() {
		if(!burning) {
			life--;
			if(this.life == 0) {
				burning = true;
				System.out.println(burning);
				burningTimer = System.currentTimeMillis();
			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}

}
