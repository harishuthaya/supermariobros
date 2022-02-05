package superMarioBrothersGame;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/*
 * Goomba Class
 * Child of Enemy class
 * This is one type of enemy
 * This class loads sprites and stores the location and vertices (hitbox) for the Goomba.
 */
public class Goomba extends Enemy {
	ImageIcon goombaLeft, goombaRight; // ImageIcons for the left and right sprites of Goombas (although, the same sprite is used for left and right Goomba movement). 
	
	public Goomba(int xPosition, int yPosition) {
		super(xPosition, yPosition);
		// the width and length (height) of Goombas are 48 by 48 pixels. 
		width = 48;
		length = 48;
		
		goombaLeft = new ImageIcon("goomba.gif"); // getting the ImageIcon for the Goomba left sprite.
		spriteLeft = goombaLeft.getImage(); // getting the Image from the ImageIcon
		spriteLeft = spriteLeft.getScaledInstance(width, length, Image.SCALE_DEFAULT); // scaling the sprite to 48 by 48 pixels.
		
		goombaRight = new ImageIcon("goomba.gif"); // getting the ImageIcon for the Goomba right sprite.
		spriteRight = goombaRight.getImage(); // getting the Image from the ImageIcon
		spriteRight = spriteRight.getScaledInstance(width, length, Image.SCALE_DEFAULT); // scaling the sprite to 48 by 48 pixels.
	
		// setting the main sprite as Koopa's left sprite (since all enemies start by moving to the left)
		// since Koopa has the same sprite moving left and right this doesn't really matter, but to fit in with the requirements of Enemy objects this must be done
		current = spriteLeft; 
		
		// calculating the x and y coordinates of the different vertices which is used for collision
		leftX = x;
		rightX = x + width;
		topY = y;
		bottomY = y + length;
	}

}
