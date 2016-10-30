package project.bomberboys.window;

import javax.swing.JButton;

import project.bomberboys.MainBoom;
import project.bomberboys.listeners.MouseHandler;

public class Button extends JButton {
	private static final long serialVersionUID = 1L;
	
	private String type;
	
	public Button(String type, MainBoom main) {
		this.type = type;
		this.addMouseListener(new MouseHandler(type, main));
	}
	
	public String getType() {
		return type;
	}

}
