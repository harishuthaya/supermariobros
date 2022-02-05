package superMarioBrothersGame;

import java.awt.*;
import javax.swing.*;

// Block class
// This is the parent class for all the blocks in the game
// Has length and width of sprite, type of block, getImage method for drawing the sprite
public class Block {
	
	int x, y; // x and y will be the changing variable for the Block's position as the background scrolls
	int length = 48, width = 48; // the default length and width except for flagpole is 48 by 48 pixels
	
	// startX and startY will be constants, because they just represent the x and y starting coordinates
	final int startX, startY;
	
	Image current, alternateSprite; // current represents the main sprite, alternateSprite is the sprite that QuestionBlock switches to once the coins (points) have been collected

	String type; // for special blocks such as question block, they will have 
	
	public Block(int xPos, int yPos) {
		type = ""; // for all non-special blocks, their type is set to "", which is essentially nothing
		// set x and startX to the xPos parameter and y and startY to the yPos parameter
		x = xPos;
		startX = xPos;
		y = yPos;
		startY = yPos;	
	}
	
	/*
	 * Used to move the blocks on screen, to follow the scrolling background
	 * Pre: an int (how much the background is moving)
	 * Post: void
	 */
	public void move(int nx) {
		x = startX + nx;
	}
	
	/*
	 * Used to return a Rectangle of the Block in its current position with its width and length.
	 * Acts as a hitbox for collision purposes.
	 * Pre: none
	 * Post: a Rectangle
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, length);
	}
	
	/*
	 * Used to get the sprite of the Block for drawing the Block
	 * Pre: none
	 * Post: an Image (the current (main) sprite of the Block)
	 */
	public Image getImage() {
		return current;
	}
	
	/*
	 * get method for the x-coordinate of the block
	 * Pre: none
	 * Post: an int
	 */
	public int getX() {
		return x;
	}
	
	/*
	 * get method for the y-coordinate of the block
	 * Pre: none
	 * Post: an int
	 */
	public int getY() {
		return y;
	}
	
	/*
	 * get method for the width of the block. This method is called for collision purposes. 
	 * Pre: none
	 * Post: an int
	 */
	public int getWidth() {
		return width;
	}
	
	/*
	 * get method for the length of the block. This method is called for collision purposes. 
	 * Pre: none
	 * Post: an int
	 */
	public int getLength() {
		return length;
	}

}
