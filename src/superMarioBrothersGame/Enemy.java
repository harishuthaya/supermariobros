package superMarioBrothersGame;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

/*
 * Enemy Class
 * The main class for all enemies
 * Contains: x and y positions, sprites, get and set methods for coordinates, movement direction (dx), and collision checks.
 */
public class Enemy {
	
	int x, y; // x and y are variables that represent the current position for the Enemy
	int dx; // dx represents the direction and speed that the Enemy is moving in (horizontally)
	int leftX, rightX, topY, bottomY; // these variables are used to find the vertices of the Enemy, for collision purposes.
	int length, width; // length and width are the length (height) and width of the sprite, which is used calculate rightX and bottomY.
	int previousNX = 0; // previousNX holds the value of the nx in the previous iteration of the move method. Used to determine the new x position of the enemy if the background has scrolled.
	
	Image current, spriteLeft, spriteRight; // current is the sprite that is actively being displayed, which switches between spriteLeft and spriteRight depending on direction of movement. 
	
	// this basically is a boolean determining whether or not the Enemy has been defeated.
	// when Mario defeats an Enemy, this boolean is set to false, so that it's x and y coordinates will not be updated (it will be sent to a far negative coordinate as well so that it is not seen anymore). 
	boolean active; 
	
	public Enemy(int xPos, int yPos) {
		// setting x and y to xPos and yPos passed through the parameters of the constructor.
		x = xPos;
		y = yPos;
		
		dx = -1; // starting the movement direction for an Enemy as -1 (moving left to start)
		
		active = true; // by default the Enemy is set to active since it has not been defeated yet
	}

	/*
	 * Get method for the current sprite of the Enemy
	 * Pre: none
	 * Post: Image (of the current sprite)
	 */
	public Image getImage() {
		return current;
	}
	
	/*
	 * Get method for the current x coordinate of the Enemy
	 * Pre: none
	 * Post: int (the x coordinate of the Enemy)
	 */
	public int getX() {
		return x;
	}
	
	/*
	 * Get method for the current y coordinate of the Enemy
	 * Pre: none
	 * Post: int (the y coordinate of the Enemy)
	 */
	public int getY() {
		return y;
	}
	
	/*
	 * Set method for the x coordinate of the Enemy
	 * Pre: int (the new x value)
	 * Post: void
	 */
	public void setX(int newX) {
		x = newX;
	}
	
	/*
	 * Set method for the y coordinate of the Enemy
	 * Pre: int (the new y value)
	 * Post: void
	 */
	public void setY(int newY) {
		y = newY;
	}
	
	/*
	 * Get method for the length (height) of the Enemy
	 * Pre: none
	 * Post: int (the length (height) of the Enemy)
	 */
	public int getLength() {
		return length;
	}
	
	/*
	 * Get method for the width of the Enemy
	 * Pre: none
	 * Post: int (the width of the Enemy)
	 */
	public int getWidth() {
		return width;
	}
	
	/*
	 * Used to move the Enemy in the direction of its movement and move it with the scrolling background
	 * Pre: int (the current position of the background)
	 * Post: void
	 */
	public void move(int nx) {
		// switching between right and left sprite depending on if the Enemy is moving right or left respectively.
		if(dx > 0) {
			current = spriteRight;
		}
		else if(dx < 0) {
			current = spriteLeft;
		}
		
		
		// if the Enemy is active (alive, the character has not beaten it yet), then it will move as regular.
		if(active) {
			x = x + dx; // incrementing x with dx (the movement variable)
			if(nx != previousNX) {
				x += (nx - previousNX);
			}
			
			previousNX = nx; // setting previousNX to the current nx value for the next iteration of this method
			
			// recalculating the vertex coordinates with the new x and y values of the Enemy
			leftX = x;
			rightX = x + width;
			topY = y;
			bottomY = y + length;
			
		}
	}
	
	/*
	 * Checks for collision between Enemy and the left side of any Block
	 * Pre: ArrayList<Block> which is an ArrayList of all the BLocks
	 * Post: boolean | returns true if there are any cases of collision, returns false if Enemy is not colliding with any Blocks.
	 */
	public boolean leftCollision(ArrayList<Block> allBlocks) {
		// for-each loop that cycles through all the Blocks in the ArrayList of Blocks
		for(Block theBlock : allBlocks) {
			// recalculating the vertex coordinates with the new x and y values of the Enemy
			int blockLeftX = theBlock.getX();
			int blockRightX = theBlock.getX() + theBlock.getWidth();
			int blockTopY = theBlock.getY();
			int blockBottomY = theBlock.getY() + theBlock.getLength();
			
			// if the right edge of the Enemy is within the left side of the Block (max 5 pixels inside to avoid triggering collision when on other side)
			// the top edge or bottom edge must be equal to or within the Block's top and bottom edges
			// Overall, the top right or bottom right vertices must be on or inside of the right side of the Block
			if((rightX >= blockLeftX && rightX <= blockLeftX+5 && 
					((topY >= blockTopY && y <= blockBottomY 
							|| (bottomY >= blockTopY && bottomY <= blockBottomY))))) {
				x = blockLeftX - width - 1; // shifts the x coordinate of the Enemy one pixel to the left, to prevent double collision errors
				return true; // returns true since left collision has been found
			}
			
		}
		return false; // returns false since no left collision was found

	}
	
	/*
	 * Checks for collision between Enemy and the right side of any Block
	 * Pre: ArrayList<Block> which is an ArrayList of all the Blocks
	 * Post: boolean | returns true if there are any cases of collision, returns false if Enemy is not colliding with any Blocks.
	 */
	public boolean rightCollision(ArrayList<Block> allBlocks) {
		for(Block theBlock : allBlocks) {
			// recalculating the vertex coordinates with the new x and y values of the Enemy
			int blockLeftX = theBlock.getX();
			int blockRightX = theBlock.getX() + theBlock.getWidth();
			int blockTopY = theBlock.getY();
			int blockBottomY = theBlock.getY() + theBlock.getLength();
			
			// if the left edge of the Enemy is within the right side of the Block (max 5 pixels inside to avoid triggering collision when on other side)
			// the top edge or bottom edge must be equal to or within the Block's top and bottom edges
			// Overall, the top left or bottom left vertices must be on or inside of the right side of the Block
			if((leftX <= blockRightX && leftX >= (blockRightX-5) && 
					((topY >= blockTopY && topY <= blockBottomY 
							|| (bottomY) >= blockTopY && (bottomY) <= (blockBottomY))))) {
				x = blockRightX + 1; // shifts the x coordinate of the Enemy one pixel to the right, to prevent double collision errors.
				return true; // returns true since right collision has been found
			}
		}
			return false; // returns false since no right collision was found


	}
	
}
