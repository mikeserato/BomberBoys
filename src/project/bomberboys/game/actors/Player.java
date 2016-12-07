package project.bomberboys.game.actors;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JTextField;

import project.bomberboys.game.Game;
import project.bomberboys.game.GameObject;
import project.bomberboys.game.SpawnPoint;
import project.bomberboys.game.blocks.Block;
import project.bomberboys.game.blocks.BonusBlock;
import project.bomberboys.game.bombs.Bomb;
import project.bomberboys.sockets.ChatSocket;
import project.bomberboys.sockets.Multicast;
import project.bomberboys.sockets.datapackets.PlayerPacket;
import project.bomberboys.window.Animation;
import project.bomberboys.window.BufferedImageLoader;
import project.bomberboys.window.SpriteSheet;

public class Player extends GameObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean chatActive;
	private JTextField chatField;
	private Multicast udpThread;

	private PlayerPacket obj;
	protected LinkedList<Bomb> bombs;
	protected ChatSocket socket;
	protected Animation forward, backward, leftward, rightward, waiting, dying, winning, invulnerableAnimation, abilityAnimation, cooldownAnimation, frozenAnimation;
	protected BufferedImage[] front, back, left, right, idle, death, victory, invulnerableImages, abilityImages, cooldownImages, frozenImages;
	protected BufferedImage sprite, invulnerableSprite, abilitySprite, cooldownSprite, frozenSprite;
	protected String action = "s", pastAction;
	protected BufferedImageLoader imageLoader;
	protected int firePower = 3, bombLimit = 3, boots = 1;
	protected String bombType = "";
	protected LinkedList<SpawnPoint> spawnPoints = null;
	protected LinkedList<Player> enemies = null;
	protected SpawnPoint sp;

	protected long deathTimer, invulnerableTimer;
	protected boolean alive, invulnerable;

	private boolean dummy = false;
	private String username;

	public Player(Game game, float x, float y, int score, ChatSocket socket) {
		super(game, socket.getUsername(), x, y);
		this.life = 1;
		this.socket = socket;
		this.speed = 0.1f;
		this.chatActive = false;
		this.chatField = socket.getChatField();
		this.alive = true;
		this.score = 0;
		this.username = this.getUser();

		obj = new PlayerPacket(socket.getUsername(), x, y, life, score, game.getIndex());

		try {
			this.udpThread = new Multicast(game, obj);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.bombs = new LinkedList<Bomb>();
		loadImage();
		createAnimation();
	}

	public Player(Game game, float x, float y, int score) {
		super(game, "", x, y);
		this.life = 1;
		this.alive = true;
		this.dummy = true;
		this.score = 0;
		this.bombs = new LinkedList<Bomb>();
		this.score = 0;
		this.username = this.getUser();
		loadImage();
		createAnimation();
	}

	public void createAnimation(){
		forward		 = new Animation(game, 3, front);
		backward	 = new Animation(game, 3, back);
		leftward	 = new Animation(game, 3, left);
		rightward	 = new Animation(game, 3, right);
		waiting		 = new Animation(game, 5, idle);
		dying		 = new Animation(game, 8, death);
		winning		 = new Animation(game, 10, victory);

		forward.animate();
		backward.animate();
		leftward.animate();
		rightward.animate();
		waiting.animate();
		dying.animate();
		winning.animate();

		invulnerableAnimation = new Animation(game, 14, invulnerableImages);
		invulnerableAnimation.animate();
	}

	public void loadImage() {
		imageLoader = new BufferedImageLoader();
		sprite = imageLoader.load("/img/player/player" + 1 + ".png/");
		invulnerableSprite = imageLoader.load("/img/player/respawnInvulnerable.png/");
		front	 = new BufferedImage[8];
		back	 = new BufferedImage[8];
		left	 = new BufferedImage[8];
		right	 = new BufferedImage[8];
		idle	 = new BufferedImage[8];
		death	 = new BufferedImage[8];
		victory	 = new BufferedImage[8];

		invulnerableImages = new BufferedImage[20];
		for(int i = 0; i < 8; i++) {
			front[i]	 = SpriteSheet.grabImage(sprite, 1, i + 1, 18, 24);
			back[i]		 = SpriteSheet.grabImage(sprite, 2, i + 1, 18, 24);
			left[i]		 = SpriteSheet.grabImage(sprite, 3, i + 1, 18, 24);
			right[i]	 = SpriteSheet.grabImage(sprite, 4, i + 1, 18, 24);
			idle[i]		 = SpriteSheet.grabImage(sprite, 5, i + 1, 18, 24);
			death[i]	 = SpriteSheet.grabImage(sprite, 6, i + 1, 18, 24);
			victory[i]	 = SpriteSheet.grabImage(sprite, 7, i + 1, 18, 24);
		}

		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 10; j++) {
				invulnerableImages[(10 * i) + j] = SpriteSheet.grabImage(invulnerableSprite, i + 1, j + 1, 192, 192);
			}
		}
	}

	public void stop(int k) {
		switch(k) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			velY = 0;
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			velX = 0;
			break;
		case KeyEvent.VK_ENTER:
			velX = velY = 0;
			break;
		}
	}

	public void act(int k) {
		switch(k) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			velY = -speed;
			action = "w";
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			velY = speed;
			action = "s";
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			velX = -speed;
			action = "a";
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			velX = speed;
			action = "d";
			break;
		case KeyEvent.VK_ENTER:
			chat();
			break;
		case KeyEvent.VK_SPACE:
			setBomb();
			break;
		case KeyEvent.VK_E:
			Bomb b = bombs.getFirst();
			b.explode();
			bombs.remove(b);
			bombs.add(b);
			break;
		case KeyEvent.VK_ESCAPE:
			game.stop();
			break;
		}
	}

	public void chat() {
		chatActive = true;
		if(chatActive) {
			stop(10);
			chatField.requestFocusInWindow();
		}
		chatActive = false;
	}

	public void setBomb() {
		int
			playerX = (int)((x - (int)x <= 0.5f) ? x : x + 1),
			playerY = (int)((y - (int)y <= 0.5f) ? y : y + 1);
		if(bombLimit > 0 && (game.getGameBoard()[playerY][playerX] == ' ' || game.getGameBoard()[playerY][playerX] == 'v' || game.getGameBoard()[playerY][playerX] == ' ')) {
			game.getGameBoard()[playerY][playerX] = 'o';
			Bomb b = new Bomb(game, playerX, playerY, socket, this);
			bombs.add(b);
			game.getObjectBoard()[playerY][playerX] = b;
			bombLimit--;
		}
	}

	public void update() {

		for(int i = 0; i < bombs.size(); i++) {
			bombs.get(i).update();
		}
		if(alive) {
			if(!dummy) {
				sp.setUsed(false);
				x += velX;
				y += velY;
				collide();
				obj.update(x, y, life, score, velX, velY, action);
				
				udpThread.update(obj);

				broadcast();
			}

			if(invulnerable) {
				invulnerableAnimation.animate();
				if(System.currentTimeMillis() - invulnerableTimer >= 5000) {
					invulnerable = false;
				}
			}

			if(velX < 0) 		leftward.	animate();
			else if(velX > 0)	rightward.	animate();
			else if(velY < 0)	backward.	animate();
			else if(velY > 0)	forward.	animate();
		} else {
			dying.animate();
			if(System.currentTimeMillis() - deathTimer > 1000) {
				life--;
				respawn();
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

	public void collide() {
		LinkedList<Block> blocks = game.getField().getBlocks();

		for(int i = 0; i <  blocks.size(); i++) {
			Block block = blocks.get(i);
			if(block instanceof BonusBlock && ((BonusBlock) block).getFirstBurn()){
				if(block.getBounds().intersects(getBoundsBot()) || block.getBounds().intersects(getBoundsTop()) || block.getBounds().intersects(getBoundsLeft()) || block.getBounds().intersects(getBoundsRight())) {
					if(((BonusBlock) block).getBonusIndex() == 0) this.bombLimit++;
					if(((BonusBlock) block).getBonusIndex() == 1) this.firePower++;
					if(((BonusBlock) block).getBonusIndex() == 2) this.firePower+=3;
					if(((BonusBlock) block).getBonusIndex() == 3){
						this.boots++;
						this.speed = this.speed + this.boots/100f;
					}
					game.getGameBoard()[block.getIntY()][block.getIntX()] = ' ';
					game.getObjectBoard()[block.getIntY()][block.getIntX()] = null;
					game.getField().getBlocks().remove(block);
				}				
			}
			else{
				if(block.getBounds().intersects(getBoundsBot())) {
					this.y = block.getY() - 1;
				} else if(block.getBounds().intersects(getBoundsTop())) {
					this.y = block.getY() + 1;
				} else if(block.getBounds().intersects(getBoundsLeft())) {
					this.x = block.getX() + 1;
				} else if(block.getBounds().intersects(getBoundsRight())) {
					this.x = block.getX() - 1;
				}				
			}

		}
		
		LinkedList<Bomb> bombs = game.getAllBombs();
		for(int i = 0; i < bombs.size(); i++) {
			Bomb bomb = bombs.get(i);
			if(bomb.getPlayerCollide()[game.getIndex()]) {
				if(bomb.getBounds().intersects(getBoundsBot())) {
					this.y = bomb.getY() - 1;
				} else if(bomb.getBounds().intersects(getBoundsTop())) {
					this.y = bomb.getY() + 1;
				} else if(bomb.getBounds().intersects(getBoundsLeft())) {
					this.x = bomb.getX() + 1;
				} else if(bomb.getBounds().intersects(getBoundsRight())) {
					this.x = bomb.getX() - 1;
				}
			}
		}
	}

	public void render(Graphics g) {
		for(int i = 0; i < bombs.size(); i++) {
			Bomb b = bombs.get(i);
			b.render(g);
		}
		if(alive) {
			if(invulnerable) {
				invulnerableAnimation.drawAnimation(g, x - 0.5f, y - 0.5f, 2, 2);
			}
			switch(action) {
			case "s":
				if(velY == 0) g.drawImage(front[front.length - 1], (int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize()* game.getScale(), null);
				else forward.drawAnimation(g, x, y);
				break;
			case "w":
				if(velY == 0) g.drawImage(back[back.length - 1], (int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize()* game.getScale(), null);
				else backward.drawAnimation(g, x, y);
				break;
			case "a":
				if(velX == 0) g.drawImage(left[left.length - 1], (int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize()* game.getScale(), null);
				else leftward.drawAnimation(g, x, y);
				break;
			case "d":
				if(velX == 0) g.drawImage(right[right.length - 1], (int) (x * game.getObjectSize() * game.getScale()), (int) (y * game.getObjectSize() * game.getScale()), game.getObjectSize() * game.getScale(), game.getObjectSize()* game.getScale(), null);
				else rightward.drawAnimation(g, x, y);
				break;
			}
		} else {
			dying.drawAnimation(g, x, y);
		}
	}

	public void destroy() {
		if(!invulnerable) {
//			game.getSoundLoader().play("/sfx/player/dead.wav");
			dying.restart();
			dying.animate();
			alive = false;
			deathTimer = System.currentTimeMillis();
			invulnerable = true;
		}
	}

	public void respawn() {
		this.alive = true;
		this.resetStats();
		if(!dummy) {
			if(spawnPoints == null) {
				spawnPoints = game.getField().getSpawnPoints();
			}
			Random rand = new Random();
			sp = spawnPoints.get(rand.nextInt(spawnPoints.size()));

			while(sp.isUsed()) {
				sp = spawnPoints.get(rand.nextInt(spawnPoints.size()));
			}

			sp.setUsed(true);
			x = sp.getX(); y = sp.getY();
		} else {
			x = 1; y = 1;
		}
		invulnerableTimer = System.currentTimeMillis();
		invulnerableAnimation.restart();
		bombType = "normal";
		action = "s";
	}
	
	public void resetStats(){
		this.speed = 0.1f;
		this.boots = 1;
		this.firePower = 3;
		this.bombLimit = 3;
	}

	public LinkedList<Bomb> getBombs() {
		return this.bombs;
	}

	public Rectangle getBounds() {
		return new Rectangle((int)(x * game.getObjectSize()) * game.getScale(), (int)(y * game.getObjectSize()) * game.getScale(), game.getObjectSize() * game.getScale(), game.getObjectSize() * game.getScale());
	}

	public Rectangle getBoundsBot() {
		return new Rectangle((int)(x * game.getObjectSize() + game.getObjectSize()/4) * game.getScale(), (int)(y * game.getObjectSize() + (game.getObjectSize() - game.getObjectSize()/4))  * game.getScale(), game.getObjectSize()/2 * game.getScale(), game.getObjectSize()/4 * game.getScale());
	}

	public Rectangle getBoundsTop() {
		return new Rectangle((int)(x * game.getObjectSize() + game.getObjectSize()/4) * game.getScale(),(int) (y * game.getObjectSize()) * game.getScale(), game.getObjectSize()/2 * game.getScale(), game.getObjectSize()/4 * game.getScale());
	}

	public Rectangle getBoundsLeft() {
		return new Rectangle((int)(x * game.getObjectSize()) * game.getScale(), (int)(y * game.getObjectSize() + (game.getObjectSize()/4)) * game.getScale(), game.getObjectSize()/4 * game.getScale(), game.getObjectSize()/2 * game.getScale());
	}

	public Rectangle getBoundsRight() {
		return new Rectangle((int)(x * game.getObjectSize() + game.getObjectSize() - game.getObjectSize()/4) * game.getScale(), (int)(y * game.getObjectSize() + (game.getObjectSize()/4)) * game.getScale(), game.getObjectSize()/4 * game.getScale(), game.getObjectSize()/2 * game.getScale());
	}

	public String getBombType() {
		return this.bombType;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void decrementLiveBombs() {
		this.bombLimit++;
	}

	public void increaseScore() {
		this.score++;
	}

	public void replenishLife() {
		this.life = 1;
	}

	public int getScore() {
		return this.score;
	}

	public void setSpawnPoints(LinkedList<SpawnPoint> spawnPoints) {
		this.spawnPoints = spawnPoints;
	}

	public int getLife() {
		return this.life;
	}

	public String getUsername() {
		return this.username;
	}

	public int getFirePower() {
		return this.firePower;
	}

	public int getBombLimit() {
		return this.bombLimit;
	}

	public int getBoots() {
		return this.boots;
	}
	
	public void setUser(String username) {
		this.user = username;
		this.username = username;
	}

}
