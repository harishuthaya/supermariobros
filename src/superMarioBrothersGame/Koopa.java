package superMarioBrothersGame;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/*
 * Koopa Class
 * Child of Enemy class
 * This is one type of enemy
 * This class loads sprites and stores the location and vertices (hitbox) for the Koopa.
 */
public class Koopa extends Enemy {
	ImageIcon koopaLeft; // ImageIcon for the left sprite of Koopa
	ImageIcon koopaRight; // ImageIcon for the right sprite of koopa
	
	public Koopa(int xPosition, int yPosition) {
		// calling the constructor of the parent class -  Enemy.  
		// since koopas are 48 pixels taller than a Goomba and the gridmap has them on the same row, we need to subtract 48 from its y position to bring it up so the bottom of the sprite is on the ground as it should be.
		super(xPosition, yPosition-48); 
		// the width and length (height) of Koopas are 48 by 96 pixels
		width = 48;
		length = 96;
		
		koopaLeft = new ImageIcon("koopaleft.gif"); // getting the ImageIcon for the Koopa left sprite.
		spriteLeft = koopaLeft.getImage(); // getting the Image from the ImageIcon
		spriteLeft = spriteLeft.getScaledInstance(width, length, Image.SCALE_DEFAULT); // scaling the sprite to 48 by 96 pixels.
		
		koopaRight = new ImageIcon("kooparight.gif"); // getting the ImageIcon for the Koopa right sprite
		spriteRight = koopaRight.getImage(); // getting the Image from the ImageIcon
		spriteRight = spriteRight.getScaledInstance(width, length, Image.SCALE_DEFAULT); // scaling the sprite to 48 by 96 pixels.
		
		current = spriteLeft; // setting the main sprite as Koopa's left sprite (since all enemies start by moving to the left)
		
		// calculating the x and y coordinates of the different vertices which is used for collision
		leftX = x;
		rightX = x + width;
		topY = y;
		bottomY = y + length;
		
	}
	
}
