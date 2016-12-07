package project.bomberboys.window;

import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class SoundLoader {

	private InputStream input;
	private AudioStream sfx;

	public void play(String path) {
		try {
			input = getClass().getResourceAsStream(path);
			sfx = new AudioStream(input);
			
			AudioPlayer.player.start(sfx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}