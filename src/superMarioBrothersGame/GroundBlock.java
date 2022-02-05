package superMarioBrothersGame;

import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * GroundBlock class
 * Child class of Block class. This is one type of Block.
 */
public class GroundBlock extends Block {
	ImageIcon ground; // an ImageIcon to get the sprite for the Ground Block
	
	public GroundBlock(int xPosition, int yPosition) {
		super(xPosition, yPosition); // calling the Block constructor with the x and y position of the Ground Block.
		ground = new ImageIcon("groundbrick.png"); // getting the ImageIcon for the Ground Block sprite.
		current = ground.getImage(); // getting the Image from the ImageIcon and setting it to current (the main sprite for a Block).
		current = current.getScaledInstance(48, 48, Image.SCALE_DEFAULT); // scaling the block to 48 pixels by 48 pixels.
	}
	
}