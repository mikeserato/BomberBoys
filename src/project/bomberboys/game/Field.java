package project.bomberboys.game;

import java.awt.Graphics;
import java.util.LinkedList;

import project.bomberboys.game.blocks.Block;

public class Field {

	private Game game;
	private char[][] gameBoard;
	private GameObject[][] objectBoard;
	private int width = 5, height = 5;
	private LinkedList<Block> blocks;
	
	public Field(Game game) {
		this.game = game;
		this.width = game.getBoardWidth();
		this.height = game.getBoardHeight();
		this.gameBoard = game.getGameBoard();
		this.gameBoard = new char[height][width];
		this.objectBoard = game.getObjectBoard();
		this.objectBoard = new GameObject[height][width];
		this.blocks = new LinkedList<Block>();
		initField();
	}
	
	private void initField() {
		int counter = 0;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(i == 0 || j == 0 || (j + 1) == width || (i + 1) == height || (i % 2 == 0 && j % 2 == 0)) {
					gameBoard[i][j] = 'x';
					Block b = new Block(game, j, i);
					objectBoard[i][j] = b;
					blocks.add(b);
					counter++;
				}
			}
		}
		System.out.println("Created " + counter + " blocks");
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
