package project.bomberboys.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

import project.bomberboys.game.blocks.Block;
import project.bomberboys.game.blocks.HardBlock;
import project.bomberboys.game.blocks.SoftBlock;
import project.bomberboys.window.BufferedImageLoader;

public class Field {

	private Game game;
	private char[][] gameBoard;
	private GameObject[][] objectBoard;
	private static int width = 5, height = 5;
	private LinkedList<Block> blocks;
	private static BufferedImageLoader loader;
	private static BufferedImage fieldImage;
	
	public Field(Game game) { 
		this.game = game;
		this.gameBoard = game.getGameBoard();
//		this.gameBoard = new char[height][width];
		this.objectBoard = game.getObjectBoard();
//		this.objectBoard = new GameObject[height][width];
		this.blocks = new LinkedList<Block>();
		initField();
	}
	
	public static Dimension checkField() {
		loader = new BufferedImageLoader();
		fieldImage = loader.load("/img/field/field32.png");
		int i;
		for(i = 0;; i++) {
			int pixel = fieldImage.getRGB(0, i);
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;
			
			if ((red == 0) && (green == 0) && (blue == 0)) {
				break;
			}
		}
		
		int j;
		for(j = 0;; j++) {
			int pixel = fieldImage.getRGB(j, 0);
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;
			
			if ((red == 0) && (green == 0) && (blue == 0)) {
				break;
			}
		}
		height = i;
		width = j;
		return(new Dimension(i, j));
//		initField();
	}
	
	public void initField() {
//		fieldImage = loader.load("/img/field/field64.png");
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				int pixel = fieldImage.getRGB(j, i);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
//				if(i == 0 || j == 0 || (j + 1) == width || (i + 1) == height || (i % 2 == 0 && j % 2 == 0)) {
				if(((red == 255) && (green == 255) && (blue == 255)) || (red == 0) && (green == 255) && (blue == 0)) {
					gameBoard[i][j] = 'x';
					HardBlock b = new HardBlock(game, j, i);
					objectBoard[i][j] = b;
					blocks.add(b);
				} else if ((red == 0) && (green == 0) && (blue == 255)) {
					gameBoard[i][j] = '+';
				} else if ((red == 255) && (green == 0) && (blue == 0)) {
					gameBoard[i][j] = 'v';
				} else {
					gameBoard[i][j] = ' ';
				}
			}
		}
//		randomizeField();
	}
	
	public void randomizeField() {
		Random rand = new Random();
//		int limit = (height * width) - (height * width) / 4;
		int limit = 200;
		
		for(int i = 0; i < limit;) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			
			if(gameBoard[y][x] == ' ') {
				gameBoard[y][x] = '#';
				SoftBlock b = new SoftBlock(game, x, y);
				objectBoard[y][x] = b;
				blocks.add(b);
				i++;
			}
		}
		
	}

	public void update() {
		for(int i = 0; i < blocks.size(); i++) {
			blocks.get(i).update();
		}
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < blocks.size(); i++) {
			blocks.get(i).render(g);
		}
	}
	
	public LinkedList<Block> getBlocks() {
		return this.blocks;
	}

}
