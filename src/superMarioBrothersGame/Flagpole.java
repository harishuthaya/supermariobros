package superMarioBrothersGame;

import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * Child class of Block class. This is one type of Block.
 */
public class Flagpole extends Block {
	ImageIcon flag; // ImageIcon for the flag sprite
	
	public Flagpole(int xPosition, int yPosition) {
		super(xPosition, yPosition); // calling the Block constructor with the x and y position of the Flagpole.
		type = "end"; // since the flagpole is a special block, its type is uniquely set to "end"
		
		flag = new ImageIcon("flagpole.png"); // getting the ImageIcon for the Flagpole sprite.
		current = flag.getImage(); // getting the Image from the ImageIcon and setting it to current (the main sprite for a Block).
		
		current = current.getScaledInstance(48, 48, Image.SCALE_DEFAULT); // scaling the flag to 48 pixels by 48 pixels.
		
		// extending the length (height) of the flagpole sprite, since it is not the typical size of a 48x48 block, and it very tall.
		// the sprite is 48 by 48 pixels because it just for the top flag, and the pole was actually a part of the map, but it has to be included for collision
		length = 624 - y; 
	}

}
