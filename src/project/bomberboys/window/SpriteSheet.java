package project.bomberboys.window;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	public static BufferedImage grabImage(BufferedImage image, int row, int col, int width, int height){
		BufferedImage img = image.getSubimage((col * width) - width, (row * height) - height, width, height);
		return img;
	}
}
