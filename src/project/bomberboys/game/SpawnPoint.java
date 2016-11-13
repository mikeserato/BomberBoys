package project.bomberboys.game;

public class SpawnPoint {

	private int x, y;
	private boolean used;
	
	public SpawnPoint(int y, int x) {
		this.x = x;
		this.y = y;
		this.used = false;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

}
