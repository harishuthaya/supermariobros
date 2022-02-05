package superMarioBrothersGame;

import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * RegularBrick class
 * Child class of Block class. This is one type of Block.
 */
public class RegularBrick extends Block {
	ImageIcon regular; // an ImageIcon to get the sprite for the Regular Brick
	
	public RegularBrick(int xPosition, int yPosition) {
		super(xPosition, yPosition); // calling the Block constructor with the x and y position of the Regular Brick.
		
		regular = new ImageIcon("regbrick.png"); // getting the ImageIcon for the Regular Brick sprite.
		current = regular.getImage(); // getting the Image from the ImageIcon and setting it to current (the main sprite for a Block).
		current = current.getScaledInstance(48, 48, Image.SCALE_DEFAULT); // scaling the brick to 48 pixels by 48 pixels.
	}

}
