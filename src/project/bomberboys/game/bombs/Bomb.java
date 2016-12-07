package project.bomberboys.game.bombs;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import project.bomberboys.game.Game;
import project.bomberboys.game.GameObject;
import project.bomberboys.game.actors.Player;
import project.bomberboys.sockets.ChatSocket;
import project.bomberboys.sockets.Multicast;
import project.bomberboys.sockets.datapackets.BombPacket;
import project.bomberboys.window.Animation;
import project.bomberboys.window.BufferedImageLoader;
import project.bomberboys.window.SpriteSheet;

public class Bomb extends GameObject {

	private long timeLimit = 3000, countDownTimer;
	private Player player;
	private boolean exploding = false, dummy = false, burnt = false;
	private BufferedImageLoader imageLoader;
	private int
		explosionRange = 3,
		wRange,
		aRange,
		sRange,
		dRange;

	protected Animation
		fireStreamNorth,
		fireStreamSouth,
		fireStreamEast,
		fireStreamWest,
		fireStreamVertical,
		fireStreamHorizontal,
		fireStreamCenter;
	protected Animation pulse;
	protected BufferedImage bombSpriteSheet, fireSpriteSheet;
	protected BufferedImage[] 
		bombSprites,
		fireSpritesNorth,
		fireSpritesSouth,
		fireSpritesEast,
		fireSpritesWest,
		fireSpritesVertical,
		fireSpritesHorizontal,
		fireSpritesCenter;
	
	private Multicast udpThread;
	private BombPacket obj;
	private boolean[] playerCollide;
	
	public Bomb(Game game, float x, float y, ChatSocket socket, Player player) {
		super(game, socket.getUsername(), x, y);
		this.player = player;
		this.explosionRange = player.getFirePower();
		if(player.getBombType().equals("remote")) {
			this.countDownTimer = 0;
		} else {
			this.countDownTimer = System.currentTimeMillis();
		}
		obj = new BombPacket(x, y, life, game.getIndex(), countDownTimer);
		
		try{
			this.udpThread = new Multicast(game, obj);
			this.udpThread.broadcast();
		} catch(IOException e) {
			
		}
		
		this.imageLoader = new BufferedImageLoader();
		loadBombSprite();
		loadFireSprite();
		init();
	}
	
	public Bomb(Game game, float x, float y, Player player, long countDownTimer) {
		super(game, "", x, y);
		dummy = true;
		this.player = player;
		
		this.imageLoader = new BufferedImageLoader();
		loadBombSprite();
		loadFireSprite();
		init();
		
		this.countDownTimer = countDownTimer;
	}
	
	public void loadBombSprite() {
		int width, height;
		int animationSpeed = 5;
		
		width = height = 32;
		bombSpriteSheet = imageLoader.load("/img/object/bomb.png");
		bombSprites = new BufferedImage[4];
		for(int i = 0; i < 4; i++) {
			bombSprites[i] = SpriteSheet.grabImage(bombSpriteSheet, 1, i + 1, width, height);
		}
		
		pulse = new Animation(game, animationSpeed, bombSprites);
	}
	
	public void loadFireSprite() {
		int width, height;
		int animationSpeed = 5;
		
		width = height = 64;
		
		fireSpriteSheet = imageLoader.load("/img/object/fire.png");
		
		fireSpritesNorth		 = new BufferedImage[8];
		fireSpritesSouth		 = new BufferedImage[8];
		fireSpritesEast			 = new BufferedImage[8];
		fireSpritesWest			 = new BufferedImage[8];
		fireSpritesVertical		 = new BufferedImage[8];
		fireSpritesHorizontal	 = new BufferedImage[8];
		fireSpritesCenter		 = new BufferedImage[8];
		
		for(int i = 0; i < 8; i++) {
			fireSpritesNorth[i]			 = SpriteSheet.grabImage(fireSpriteSheet, 1, i + 1, width, height);
			fireSpritesSouth[i]			 = SpriteSheet.grabImage(fireSpriteSheet, 2, i + 1, width, height);
			fireSpritesCenter[i]		 = SpriteSheet.grabImage(fireSpriteSheet, 3, i + 1, width, height);
			fireSpritesWest[i]			 = SpriteSheet.grabImage(fireSpriteSheet, 4, i + 1, width, height);
			fireSpritesEast[i]			 = SpriteSheet.grabImage(fireSpriteSheet, 5, i + 1, width, height);
			fireSpritesVertical[i]		 = SpriteSheet.grabImage(fireSpriteSheet, 6, i + 1, width, height);
			fireSpritesHorizontal[i]	 = SpriteSheet.grabImage(fireSpriteSheet, 7, i + 1, width, height);
		}
		
		fireStreamNorth			 = new Animation(game, animationSpeed, fireSpritesNorth);
		fireStreamSouth			 = new Animation(game, animationSpeed, fireSpritesSouth);
		fireStreamEast			 = new Animation(game, animationSpeed, fireSpritesEast);
		fireStreamWest			 = new Animation(game, animationSpeed, fireSpritesWest);
		fireStreamVertical		 = new Animation(game, animationSpeed, fireSpritesVertical);
		fireStreamHorizontal	 = new Animation(game, animationSpeed, fireSpritesHorizontal);
		fireStreamCenter		 = new Animation(game, animationSpeed, fireSpritesCenter);
		
	}
	
	public void init() {
		game.getAllBombs().add(this);
		playerCollide = new boolean[game.getPlayers().length];
		resetCollision();
	}
	
	public void resetCollision() {
		for(int i = 0; i < playerCollide.length; i++) {
			playerCollide[i] = !(game.getPlayers()[i].getBounds().intersects(this.getBounds()));
		}
	}
	
	public int checkObstacle(int x, int y, char dir, int counter) {
		
		if(counter > explosionRange) return 1;
		if(game.getGameBoard()[y][x] == 'x') return 1;
//		if(game.getGameBoard()[y][x] == '+') return 1;
		if(game.getGameBoard()[y][x] == '#') return 1;
		
		counter++;
		
		switch(dir) {
			case 'w': return 1 + checkObstacle(x, y - 1, dir, counter);
			case 's': return 1 + checkObstacle(x, y + 1, dir, counter);
			case 'a': return 1 + checkObstacle(x - 1, y, dir, counter);
			case 'd': return 1 + checkObstacle(x + 1, y, dir, counter);
		}
		
		return 1;
	}
	
	public void updateExplosionRange() {
		wRange	 = checkObstacle((int) this.x, (int) this.y - 1, 'w', 1);
		sRange	 = checkObstacle((int) this.x, (int) this.y + 1, 's', 1);
		aRange	 = checkObstacle((int) this.x - 1, (int) this.y, 'a', 1);
		dRange	 = checkObstacle((int) this.x + 1, (int) this.y, 'd', 1);
//		updated = true;
	}
	
	public void update() {
		if(countDownTimer != 0 && System.currentTimeMillis() - countDownTimer > timeLimit) {
			if(!exploding) this.explode();
			else this.destroy();
		} else {
			if(!exploding) {
				updateExplosionRange();
				resetCollision();
				pulse.animate();
			} else {
				fireStreamNorth.animate();
				fireStreamSouth.animate();
				fireStreamEast.animate();
				fireStreamWest.animate();
				fireStreamVertical.animate();
				fireStreamHorizontal.animate();
				fireStreamCenter.animate();
				if(!burnt) {
					burnObjects();
					burnt = true;
				}
				for(int i = 0; i < game.getPlayers().length; i++) {
					burnPlayer(game.getPlayers()[i]);
				}
			}
			
		}
	}
	
	public void render(Graphics g) {
		if(!exploding) {
			pulse.drawAnimation(g, x, y);
		}
		else {
			fireStreamCenter.drawAnimation(g, this.x, this.y);
			for(int i = 1; i <= explosionRange; i++) {
				if(i == explosionRange) {
					if(i < wRange)	 fireStreamNorth	.drawAnimation(g, this.x, this.y - i);
					if(i < sRange)	 fireStreamSouth	.drawAnimation(g, this.x, this.y + i);
					if(i < aRange)	 fireStreamWest		.drawAnimation(g, this.x - i, this.y);
					if(i < dRange)	 fireStreamEast		.drawAnimation(g, this.x + i, this.y);
				} else {
					if(i < wRange)	 fireStreamVertical		.drawAnimation(g, this.x, this.y - i);
					if(i < sRange)	 fireStreamVertical		.drawAnimation(g, this.x, this.y + i);
					if(i < aRange)	 fireStreamHorizontal	.drawAnimation(g, this.x - i, this.y);
					if(i < dRange)	 fireStreamHorizontal	.drawAnimation(g, this.x + i, this.y);
				}
			}
		}
	}
	
	public void burn(char type, int y, int x) {
		switch(type) {
//		case 'x':
		case '#':
		case '!':
			(game.getObjectBoard()[y][x]).destroy();
			System.out.println(type);
			break;
		case 'o':
			if(!((Bomb)game.getObjectBoard()[y][x]).isExploding()) {
				((Bomb)game.getObjectBoard()[y][x]).resetTimer(this.countDownTimer - 500);
			}
			break;
		default:
			break;
		}
	}
	
	public void burnObjects() {
		int y = (int)this.y, x = (int)this.x;
		for(int i = 0; i <= explosionRange; i++) {
			if(i <= wRange) {
				burn(game.getGameBoard()[y - i][x], y - i, x);
			}
			if(i <= sRange) {
				burn(game.getGameBoard()[y + i][x], y + i, x);
			}
			if(i <= aRange) {
				burn(game.getGameBoard()[y][x - i], y, x - i);
			}
			if(i <= dRange) {
				burn(game.getGameBoard()[y][x + i], y, x + i);
			}
		}
	}
	
	public void burnPlayer(Player player) {
		int y = (int) this.y, x = (int) this.x;
		int 
			playerX = (int)((player.getX() - (int)player.getX() <= 0.5f) ? player.getX() : player.getX() + 1),
			playerY = (int)((player.getY() - (int)player.getY() <= 0.5f) ? player.getY() : player.getY() + 1);
		
		for(int i = 0; i <= explosionRange; i++) {
			if(i <= wRange) {
				if(playerY == y - i && playerX == x) {
					player.destroy();
				}
			}
			if(i <= sRange) {
				if(playerY == y + i && playerX == x) {
					player.destroy();
				}
			}
			if(i <= aRange) {
				if(playerY == y && playerX == x - i) {
					player.destroy();
				}
			}
			if(i <= dRange) {
				if(playerY == y && playerX == x + i) {
					player.destroy();
				}
			}
		}
	}
	
	public void explode() {
		if(!exploding) {
			exploding = true;
			if(!dummy) this.player.decrementLiveBombs();
			burnObjects();
			timeLimit = 900;
			countDownTimer = System.currentTimeMillis();
		}
	}
	
	public void destroy() {
		player.getBombs().remove(this);
		game.getGameBoard()[(int) y][(int) x] = ' ';
		game.getObjectBoard()[(int) y][(int) x] = null;
		game.getAllBombs().remove(this);
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}
	
	public boolean isExploding() {
		return this.exploding;
	}
	
	public boolean[] getPlayerCollide() {
		return this.playerCollide;
	}
	
	public void resetTimer(long timer) {
		this.countDownTimer = System.currentTimeMillis() - (timeLimit - 100);
	}

}
