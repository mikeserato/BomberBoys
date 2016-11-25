package project.bomberboys.sockets.datapackets;


public class BombPacket extends ObjectPacket {

	private static final long serialVersionUID = 1L;
	private int index,
		aRange,
		sRange,
		wRange,
		dRange;
	private long countDownTimer;
	
	public BombPacket(float x, float y, int life, int score, int index, long countDownTimer) {
		super(x, y, life, score);
		this.index = index;
		this.countDownTimer = countDownTimer;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public int getWRange() {
		return wRange;
	}
	
	public int getARange() {
		return aRange;
	}
	
	public int getSRange() {
		return sRange;
	}
	
	public int getDRange() {
		return dRange;
	}
	
	public long getCountDownTimer() {
		return countDownTimer;
	}
	
	public void update(int w, int a, int s, int d) {
		aRange = a;
		sRange = s;
		wRange = w;
		dRange = d;
	}

}
