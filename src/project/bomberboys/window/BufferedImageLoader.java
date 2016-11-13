package project.bomberboys.window;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class BufferedImageLoader {

	public BufferedImage load(String path) {
		try {
			return ImageIO.read(getClass().getResource(path));
		} catch(Exception e) {
			System.out.println("Cannot load image at " + path + ".");
		}
		return null;
	}

}
