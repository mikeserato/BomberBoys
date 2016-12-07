package project.bomberboys.window;

import java.io.InputStream;

//import javazoom.jlgui.basicplayer.BasicPlayer;

public class BGSoundLoader {

	private InputStream input;
//	private BasicPlayer bgPlayer;
	
	public void play(String path) {
		input = getClass().getResourceAsStream(path);
//		bgPlayer = new BasicPlayer();
		
		System.out.println(input);
		
		try {
//			bgPlayer.open(input);
//			bgPlayer.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void resume() {
		try {
//			bgPlayer.resume();
		} catch (Exception e) {
			
		}
	}
	
	public void pause() {
		try {
//			bgPlayer.pause();
		} catch (Exception e) {
			
		}
	}
	
	public void stop() {
		try {
//			bgPlayer.stop();
		} catch (Exception e) {
			
		}
	}
	
}