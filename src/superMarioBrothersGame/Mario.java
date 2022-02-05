package superMarioBrothersGame;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/*
 * Mario Class
 * This is the main class for the Mario object (player will control this character)
 * This contains movement, positions, collision checks (Mario-Block and Mario-Enemy collision)
 */
public class Mario {
	int x, dx; // x coordinate and the change in x coordinate (how much you move your character by each time you press a movement button
	int y; // y coordinate
	int nx; // coordinates of the background - for the moving background
	int changeNX; // represents how much the background is being shifted left or right since it can vary based on if Mario is sprinting or just walking
	int startY; 
	int jumpMax = 155; // Mario's jump reaches a maximum of 140 pixels above his starting position the jump before falling down begins. This number is stored in an int for convenience.
	int beforeJump; // this is used to store the y coordinate of Mario right before a jump commences, to know when he has reached 140 pixels above this position to start falling. 
	int jumpVel = 0; // this variable is used for Mario's jumping and falling. It will switch between negative and positive, when he jumps and falls respectively. 
	int leftX, rightX, topY, bottomY; // these coordinates are used to determine the locations of the 4 vertices of Mario, which is then used for collision.
	int width = 36, length = 72; // the width of Mario is 36 pixels and length (width) of Mario is 72 pixels
	
	
	boolean jumping = false, falling = false; // these two booleans change depending on if the player is moving up in the air, falling down from air, or on the ground/platform to determine jump/fall physics
	boolean moveL = false, moveR = false, sprint = false, jump = false; // when keys are pressed and released, these booleans are changed between true and false, to then change Mario's speed, direction, current sprite.

	String previous = "r"; // this previous variable switches between "r" and "l", to determine what sprite to switch to when Mario is not moving. For example, if he was previously running to the right, then his current sprite will now be standing right.
	
	Image right, left, jumpL, jumpR, runR, runL, current; // Each of these images represent Mario's different sprites, for standing right/left, walking/running right/left, jumping right/left, as well as a "current" sprite that holds the current sprite of Mario
	
	public Mario() {
		// Loading all the different ImageIcons for Mario's sprites
		// Setting the respective Image to the Image from the ImageIcon
		// Scaling each Image to the width and length of Mario
		ImageIcon marioRight = new ImageIcon("marioright.png");
		right = marioRight.getImage();
		right = right.getScaledInstance(width, length, Image.SCALE_DEFAULT);
		
		ImageIcon marioLeft = new ImageIcon("marioleft.png");
		left = marioLeft.getImage();
		left = left.getScaledInstance(width, length, Image.SCALE_DEFAULT);
		
		ImageIcon marioJumpR = new ImageIcon("jumpright.png");
		jumpR = marioJumpR.getImage();
		jumpR = jumpR.getScaledInstance(width, length, Image.SCALE_DEFAULT);
		
		ImageIcon marioJumpL = new ImageIcon("jumpleft.png");
		jumpL = marioJumpL.getImage();
		jumpL = jumpL.getScaledInstance(width, length, Image.SCALE_DEFAULT);
		
		ImageIcon marioRunR = new ImageIcon("mariorunningright.gif");
		runR = marioRunR.getImage();
		runR = runR.getScaledInstance(width, length, Image.SCALE_DEFAULT);
		
		ImageIcon marioRunL = new ImageIcon("mariorunningleft.gif");
		runL = marioRunL.getImage();
		runL = runL.getScaledInstance(width, length, Image.SCALE_DEFAULT);
		
		
		
		current = right; // set the starting sprite of Mario as right facing
		
		x = 75; // starting x position for Mario
		y = 552; // starting y position for Mario
		startY = y; // retaining the starting y position is a separate value to be used in collision
		
		// these 4 variables are used to check collision for the 4 vertices of Mario (imagine if Mario is a box)
		leftX = x;
		rightX = x + width;
		topY = y;
		bottomY = y + length;

		nx = 0; // For the moving background, we want it to start at an x position of 0 (the regular start of the map).
	}
	
	/*
	 * Get method for the Rectangle of Mario. This is used for collision purposes.
	 * Pre: none
	 * Post: Rectangle (The Rectangle of Mario)
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, length);
	}
	
	/*
	 * The move method that is called in the ActionPerformed method in the Board class to update Mario's position
	 * Pre: an ArrayList of Blocks to pass into the collision methods that are called within this method
	 * Post: void
	 */
	public void move(ArrayList<Block> allBlocks) {
		calculateVertices(); // calculating the latest vertices
		horizontalCollision(allBlocks); // checking for left and right collision between Mario and blocks
		calculateVertices(); // calculating the latest vertices in case horizontal collision changed anything
		
		// if Mario is moving to the right
		if(dx > 0) {
			// if Mario + his movement direction are less than 640 pixels (middle of the Frame horizontally)
			if(x + dx <= 640)
				// Since Mario is not at the edge of the right side to trigger the scrolling background, he can move as usual.
				x = x + dx;
			
			// this is if x + dx is greater than 640 which means that we should scroll the background
			else {
				// if the background has not yet scrolled past -8800 which is the end of the level, we subtract dx from nx to move the background instead of the player
				if(nx > -8800) {
					nx -= dx;
				}
			}
		// if dx is not greater than 0, Mario is either standing still or trying to move to the left
		} else {
			// we check if Mario plus his movement direction is greater than 0. 
			// In Super Mario Bros. the background does not scroll to the left to push players to move in the correct direction - the right
			// If x + dx is greater than 0, we know that the player is moving to the left of the screen, but has not reached the edge of the screen yet
			if(x + dx > 0) {
				x = x + dx; // We add dx to the current value of x
			}
		}
		
		y += jumpVel; // incrementing Mario's y coordinate with the jump velocity determined in the previous iteration of the loop (0 to start). 
		calculateVertices(); // updating Mario's vertices based on this
		
		// if jumping boolean is true, Mario is moving upwards.
		if(jumping) {
			jumpVel = -2; // this means his jump velocity should be -2, moving two pixels up each time
			
			// if Mario's y position is smaller than his y position before jumping - the jump height, that means he has reached the peak of the jump
			// alternatively, if he has collided with the bottom of any block, that means he should also be falling now
			if((y <= beforeJump - jumpMax) || bottomCollision(allBlocks)) {
				jumping = false; // set jumping to false since he will not be moving up anymore. 
				falling = true; // set falling to true so we will enter the following else if statement in the next iteration of this method
			} 
		} else if(falling) {
			// since Mario is falling, his jump velocity will bring him 2 pixels down each time
			jumpVel = 2;
			
			// if Mario has collision with the top of any blocks his jump/fall has finished
			// alternatively, if his y position is equal to his startingY position, that means he is currently on the ground, and his jump/fall has also finished
			if(topCollision(allBlocks) || y == startY) {
				// setting both jumping and falling to false since neither are currently happening
				jumping = false; 
				falling = false;
			}
		}
		
		// if Mario is currently not in a falling or jumping state but is also not on the ground or colliding with the top of any of the Blocks
		// This occurs if Mario walks off of a platform, and means that he should be in a falling state
		if(!falling && !jumping && y!= startY && !topCollision(allBlocks)) {
			falling = true;
		}
		
		// setting jump velocity to 0 when Mario is neither falling nor jumping
		if(!falling && !jumping) {
			jumpVel = 0;
		}
		
		chooseSprite(); // calling the chooseSprite method to determine which sprite is best appropriate to display based on Mario's current movement.
	}
	
	/*
	 * Used to update Mario's vertex-finding coordinates to match his current position
	 * Pre: none
	 * Post: void
	 */
	public void calculateVertices() {
		// leftX and topY are the normal x and yo coordinates
		// rightX is Mario's current x along with the width of his sprite
		// topY is Mario's curent y along with the length (height) of his sprite. 
		
		leftX = x;
		rightX = x + width; 
		topY = y;
		bottomY = y + length;
	}
	
	/*
	 * Get method for Mario's x position
	 * Pre: none
	 * Post: int (Mario's x Position)
	 */
	public int getX() {
		return x;
	}
	

	/*
	 * Get method for Mario's y position
	 * Pre: none
	 * Post: int (Mario's y Position)
	 */
	public int getY() {
		return y;
	}
	

	/*
	 * Get method for the nx value (where the background currently is scroleld to)
	 * Pre: none
	 * Post: int (the nx value)
	 */
	public int getNX() {
		return nx;
	}
	

	/*
	 * Get method for Mario's current sprite
	 * Pre: none
	 * Post: Image (Mario's current sprite)
	 */
	public Image getImage() {
		return current;
	}
	
	/*
	 * Used to determine what way Mario is trying to move
	 * Pre: KeyEvent
	 * Post: void
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode(); // getting the keyCode from the KeyEvent passed in through the parameter of this method
		
		if(key == 65) moveL = true; // if the a key is pressed, player is trying to move left
		if(key == 68) moveR = true; // if the d key is pressed, player is trying to move right
		if(key == KeyEvent.VK_SHIFT) sprint = true; // if the shift key is pressed, player is trying to sprint (run faster)
		if(key == KeyEvent.VK_SPACE) jump = true; // if the space key is pressed, player is trying to jump
		calculateMovementPressed(); // calling the calculateMovementPressed method to determine how movement variables should be changed, based on the pressed keys.
	}
	
	/*
	 * When a key is released, we use this method to stop Mario from the direction he was moving in
	 * Pre: KeyEvent
	 * Post: void
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode(); // getting the keyCode from the KeyEvent passed in through the parameter of this method
		
		// if any of these keys are released, we set their state back to false
		if(key == 65) moveL = false;
		if(key == 68) moveR = false;
		if(key == KeyEvent.VK_SHIFT) sprint = false;
		if(key == KeyEvent.VK_SPACE) jump = false;
		calculateMovementReleased(); // calling the movement released method to determine how movement variables should be changed based on the released keys.
		
	}
	
	/*
	 * Called when a key is pressed, to calculate how Mario's movement should be changed
	 * Pre: none
	 * Post: void
	 */
	public void calculateMovementPressed() {
		// if the move left and sprint key are pressed, we set dx to -4 to move to the left faster
		if(moveL && sprint) {
			dx=-4;
		}
		
		// if only the move left key is pressed, we set dx to -3 to move to the left at a regular speed
		else if(moveL) {
			dx=-3;
		}
		
		// if the move right and sprint key are pressed, we set dx to 4 to move to the right faster
		else if(moveR && sprint) {
			dx = 4;
		 }
		
		// if only the move right key is pressed, we set dx to 3 to move to the right at a regular speed
		else if(moveR) {
			dx = 3;
		}
		 
		// if the jump key is pressed and Mario is currently not jumping of falling, we can set jumping to true
		// it has to be ensured that Mario is not currently jumping and falling to prevent infinitely jumping while already in the air
		if(jump && !jumping && !falling) {
			 jumping = true;
			 beforeJump = y; // the current y position of the player will be set to beforeJump, used to measure when the player has reached the peak of their jump
		 }
		
		// if both movement keys are simultaneously being held down, dx is set to 0 and Mario will stand still.
		if(moveR && moveL) {
			dx = 0;
		}
	}
	
	/*
	 * Called when a key is released, to calculate how Mario's movement should be changed
	 * Pre: none
	 * Post: void
	 */
	public void calculateMovementReleased() {
		// all we have to do is set dx to 0 when moveL and moveR are false, since Mario should be standing still now
		if(!moveR && !moveL) { 
			dx = 0;
		}

	}
	
	/*
	 * Note: While there are many different collision methods that may seem to do the same thing
	 * each one is actually used for a different purpose. For example, horizontalCollision is called within the move method to find out Mario's position, 
	 * while leftCollision and rightCollision with a Block as a parameter is used by the Block Class to check for flagpole collision
	 */
	
	
	/*
	 * Checks for left and right collision between Mario and any of the Blocks
	 * Pre: ArrayList of Blocks
	 * Post: boolean, true if collision detected and false is no collision detected
	 */
	public boolean horizontalCollision(ArrayList<Block> allTheBlocks) {
		Rectangle playerRect = getBounds(); // getting a Rectangle of Mario
		playerRect.x += dx; // incrementing the x value of Mario's rectangle with dx
		
		// for-each loop that goes through 
		for(Block theBlock : allTheBlocks) {
			Rectangle wallRect = theBlock.getBounds(); // getting a Rectangle of the Block
			if(playerRect.intersects(wallRect)) {
				playerRect.x -= dx; // decreasing dx from the player rectangle to move away from the collision with the block
				// adding 1 or -1 to playerX using Math.signum of dx's value to move closer and up to the block, until there is collision detected, to get Mario right beside the block
				while(!wallRect.intersects(playerRect)) playerRect.x += Math.signum(dx);
				dx=0; // set dx to 0 to stop Mario from moving into the block
				x = playerRect.x; // setting Mario's x position to the x of the player rectangle
				return true; // returning true since collision was detected
			}
		}
		return false; // returning false since no collision was found
		
	}
	
	/*
	 * Checks for collision between Mario and the top of a Block
	 * Pre: Block object
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean topCollision(Block theBlock) {
		// getting the coordinates to calculate the vertices of the Block
		int blockLeftX = theBlock.getX();
		int blockRightX = theBlock.getX() + theBlock.getWidth();
		int blockTopY = theBlock.getY();
		int blockBottomY = theBlock.getY() + theBlock.getLength();
		
		// checking if player's bottomY coordinate is greater than the the top of the Block and less than 5 pixels into the top of the Block
		// also checks that the either the right side or left side of Mario is within the Block's left and right x coordinates.
		if((bottomY >= blockTopY && (bottomY <= (blockTopY+5)) &&
				((rightX >= blockLeftX && rightX <= blockRightX
					|| leftX >= blockLeftX && leftX <= blockRightX)))){
			y = blockTopY - length; // setting y coordinate to the top of the Block - the length (height) of Mario, to make it seem like mario is standing on the top of the Block
			return true; // returning true since collision was found
		} 
		return false; // returning false since collision was not found

	}
	
	/*
	 * Checks for collision between Mario and the top of a Block
	 * Pre: ArrayList of Block objects
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean topCollision(ArrayList<Block> allTheBlocks) {
		// for-each loop cycling through all the Blocks in the ArrayList of Blocks
		for(Block theBlock : allTheBlocks) {
			// getting the coordinates to calculate the vertices of the Block
			int blockLeftX = theBlock.getX();
			int blockRightX = theBlock.getX() + theBlock.getWidth();
			int blockTopY = theBlock.getY();
			int blockBottomY = theBlock.getY() + theBlock.getLength();
			
			// checking if player's bottomY coordinate is greater than the the top of the Block and less than 5 pixels into the top of the Block
			// also checks that the either the right side or left side of Mario is within the Block's left and right x coordinates.
			if((bottomY >= blockTopY && (bottomY <= (blockTopY+5)) &&
					((rightX > blockLeftX && rightX < blockRightX)
						|| (leftX > blockLeftX && leftX < blockRightX)))){
				return true; // returning true since collision was found
			} 
		}
		return false; // returning false since collision was not found

	}
	
	/*
	 * Checks for collision between Mario and the bottom of a Block
	 * Pre: ArrayList of Block objects
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean bottomCollision(ArrayList<Block> allTheBlocks) {
		// for-each loop cycling through all the Blocks in the ArrayList of Blocks
		for(Block theBlock : allTheBlocks) {
			// getting the coordinates to calculate the vertices of the Block
			int blockLeftX = theBlock.getX();
			int blockRightX = theBlock.getX() + theBlock.getWidth();
			int blockTopY = theBlock.getY();
			int blockBottomY = theBlock.getY() + theBlock.getLength();
			
			// checking if player's topY coordinate is less than the the bottom of the Block and greater than 5 pixels into the bottom of the Block
			// also checks that the either the right side or left side of Mario is within the Block's left and right x coordinates.
			if((topY <= blockBottomY && (topY >= (blockBottomY-5)) &&
					((rightX > blockLeftX && rightX < blockRightX
						|| leftX > blockLeftX && leftX < blockRightX)))){
				return true; // returning true since collision was found
			}
		}
		return false; // returning false since collision was not found
	}
	
	/*
	 * Checks for collision between Mario and the bottom of a Block
	 * Pre: Block object
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean bottomCollision(Block theBlock) {
		// getting the coordinates to calculate the vertices of the Block
		int blockLeftX = theBlock.getX();
		int blockRightX = theBlock.getX() + theBlock.getWidth();
		int blockTopY = theBlock.getY();
		int blockBottomY = theBlock.getY() + theBlock.getLength();
		
		// checking if player's topY coordinate is less than the the bottom of the Block and greater than 5 pixels into the bottom of the Block
		// also checks that the either the right side or left side of Mario is within the Block's left and right x coordinates.
		if((topY <= blockBottomY && (topY >= (blockBottomY-5)) &&
				((rightX > blockLeftX && rightX < blockRightX
					|| leftX > blockLeftX && leftX < blockRightX)))){
			
			return true; // returning true since collision was found
		}
		return false; // returning false since collision was not found
	}
	
	/*
	 * Checks for collision between Mario and the left side of a Block
	 * Pre: ArrayList of Block objects
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean leftCollision(ArrayList<Block> allTheBlocks) {
		// for-each loop cycling through all the Blocks in the ArrayList of Blocks
		for(Block theBlock : allTheBlocks) {
			// getting the coordinates to calculate the vertices of the Block
			int blockLeftX = theBlock.getX();
			int blockRightX = theBlock.getX() + theBlock.getWidth();
			int blockTopY = theBlock.getY();
			int blockBottomY = theBlock.getY() + theBlock.getLength();
			
			// checking if the player's rightX coordinate is greater than the left side of a Block and less than 15 pixels into the left side of the Block
			// also checks that either the topY or bottomY coordinate is within the Block's top and bottom y coordinates
			if(((rightX >= blockLeftX) && (rightX <= (blockLeftX+15)) && ((topY >= blockTopY && topY <= blockBottomY) || (bottomY > blockTopY && bottomY <= blockBottomY)))) {
				return true; // returning true since collision was found
			}
		}
		return false; // returning false since collision was not found
		
	}
	
	/*
	 * Checks for collision between Mario and the left side of a Block
	 * Pre: Block object
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean leftCollision(Block theBlock) {
		// getting the coordinates to calculate the vertices of the Block
		int blockLeftX = theBlock.getX();
		int blockRightX = theBlock.getX() + theBlock.getWidth();
		int blockTopY = theBlock.getY();
		int blockBottomY = theBlock.getY() + theBlock.getLength();
		
		// checking if the player's rightX coordinate is greater than the left side of a Block and less than 15 pixels into the left side of the Block
		// also checks that either the topY or bottomY coordinate is within the Block's top and bottom y coordinates
		if(((rightX >= blockLeftX) && (rightX <= (blockLeftX+5)) && ((bottomY > blockTopY && bottomY <= blockBottomY) || (topY > blockTopY && topY <= blockBottomY)))) {
			return true; // returning true since collision was found
		}
		return false; // returning false since collision was not found
	}
	
	/*
	 * Checks for collision between Mario and the right side of a Block
	 * Pre: ArrayList of Block objects
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean rightCollision(ArrayList<Block> allTheBlocks) {
		// for-each loop cycling through all the Blocks in the ArrayList of Blocks
		for(Block theBlock : allTheBlocks) {
			// getting the coordinates to calculate the vertices of the Block
			int blockLeftX = theBlock.getX();
			int blockRightX = theBlock.getX() + theBlock.getWidth();
			int blockTopY = theBlock.getY();
			int blockBottomY = theBlock.getY() + theBlock.getLength();
			
			// checking if the player's leftX coordinate is less than the right side of a Block and greater than 5 pixels to the left of the right side
			// also checks that either the topY or bottomY coordinate is within the Block's top and bottom y coordinates
			if(((leftX) <= blockRightX && ((leftX) >= (blockRightX-5)) && 
					((topY >= blockTopY && y <= blockBottomY 
							|| (bottomY) > blockTopY && (bottomY) <= (blockBottomY))))) {
				return true; // returning true since collision was found
			}
		}
		return false; // returning false since collision was not found
	}
	
	/*
	 * Checks for collision between Mario and the right side of a Block
	 * Pre: Block object
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean rightCollision(Block theBlock) {
		// getting the coordinates to calculate the vertices of the Block
		int blockLeftX = theBlock.getX();
		int blockRightX = theBlock.getX() + theBlock.getWidth();
		int blockTopY = theBlock.getY();
		int blockBottomY = theBlock.getY() + theBlock.getLength();
		
		// checking if the player's leftX coordinate is less than the right side of a Block and greater than 5 pixels to the left of the right side
					// also checks that either the topY or bottomY coordinate is within the Block's top and bottom y coordinates
		if(((leftX) <= blockRightX && ((leftX) >= (blockRightX-5)) && 
				((topY >= blockTopY && y <= blockBottomY 
						|| (bottomY) > blockTopY && (bottomY) <= (blockBottomY))))) {
			
			return true; // returning true since collision was found
		}
		return false; // returning false since collision was not found
	}
	
	/*
	 * Checks for collision between Mario and the top of an Enemy
	 * Pre: Enemy object
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean topCollision(Enemy anEnemy) {
		// getting the coordinates to calculate the vertices of the Enemy
		int enemyLeftX = anEnemy.getX();
		int enemyRightX = anEnemy.getX() + anEnemy.getWidth();
		int enemyTopY = anEnemy.getY();
		int enemyBottomY = anEnemy.getY() + anEnemy.getLength();
		
		
		// checking if player's bottomY coordinate is greater than the the top of the Enemy and less than 5 pixels into the top of the Enemy
		// also checks that the either the right side or left side of Mario is within the Enemy's left and right x coordinates.
		if((bottomY > enemyTopY && (bottomY < (enemyTopY+5)) &&
				((rightX > enemyLeftX && rightX < enemyRightX
					|| leftX > enemyLeftX && leftX < enemyRightX)))){
			return true; // returning true since collision was found
		} 
		return false; // returning false since collision was not found

	}
	
	/*
	 * Checks for collision between Mario and the right of an Enemy
	 * Pre: Enemy object
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean rightCollision(ArrayList<Enemy> allTheEnemies, String useThis) {
		// for-each loop cycling through all the Blocks in the ArrayList of Blocks
		for(Enemy theEnemy : allTheEnemies) {
			// getting the coordinates to calculate the vertices of the Enemy
			int enemyLeftX = theEnemy.getX();
			int enemyRightX = theEnemy.getX() + theEnemy.getWidth();
			int enemyTopY = theEnemy.getY();
			int enemyBottomY = theEnemy.getY() + theEnemy.getLength();
			
			// checking if the player's leftX coordinate is less than the right side of a Block and greater than 5 pixels to the left of the right side
			// also checks that either the topY or bottomY coordinate is within the Block's top and bottom y coordinates
			if(((leftX) <= enemyRightX && ((leftX) >= (enemyRightX-5)) && 
					((topY >= enemyTopY && y <= enemyBottomY 
							|| (bottomY) > enemyTopY && (bottomY) <= (enemyBottomY))))) {
				
				if(!falling) return true; // will only return true if Mario is currently not falling, because when he is falling on top of an Enemy, this collision may trigger, but when Mario falls on an Enemy he is beating them not getting hit by the side of them. 
			}
		}
		return false; // returning false since collision was not found
	}
	
	/*
	 * Checks for collision between Mario and the left of an Enemy
	 * Pre: Enemy object
	 * Post: boolean, true if collision was found and false if collision was not found
	 */
	public boolean leftCollision(ArrayList<Enemy> allTheEnemies, String useThis) {
		// for-each loop cycling through all the Blocks in the ArrayList of Blocks
		for(Enemy theEnemy : allTheEnemies) {
			// getting the coordinates to calculate the vertices of the Enemy
			int enemyLeftX = theEnemy.getX();
			int enemyRightX = theEnemy.getX() + theEnemy.getWidth();
			int enemyTopY = theEnemy.getY();
			int enemyBottomY = theEnemy.getY() + theEnemy.getLength();
			
			// checking if the player's rightX coordinate is greater than the left side of an Enemy and less than 15 pixels into the left side of the Enemy
			// also checks that either the topY or bottomY coordinate is within the Enemy's top and bottom y coordinates
			if((rightX >= enemyLeftX && (rightX <= (enemyLeftX+5)) && 
					((topY >= enemyTopY && y <= enemyBottomY 
							|| bottomY > enemyTopY && bottomY <= enemyBottomY)))) {
				if(!falling) return true; // will only return true if Mario is currently not falling, because when he is falling on top of an Enemy, this collision may trigger, but when Mario falls on an Enemy he is beating them not getting hit by the side of them. 
			}
		}
		return false; // returning false since collision was not found
	}
	
	public boolean bottomCollision(ArrayList<Enemy> allTheEnemies, String useThis) {
		// for-each loop cycling through all the Blocks in the ArrayList of Blocks
		for(Enemy theEnemy : allTheEnemies) {
			// getting the coordinates to calculate the vertices of the Enemy
			int enemyLeftX = theEnemy.getX();
			int enemyRightX = theEnemy.getX() + theEnemy.getWidth();
			int enemyTopY = theEnemy.getY();
			int enemyBottomY = theEnemy.getY() + theEnemy.getLength();
			
			// checking if player's topY coordinate is less than the the bottom of the Enemy and greater than 5 pixels into the bottom of the Enemy
			// also checks that the either the right side or left side of Mario is within the Enemy's left and right x coordinates.
			if((topY <= enemyBottomY && (topY >= (enemyBottomY-5)) &&
					((rightX > enemyLeftX && rightX < enemyRightX
						|| leftX > enemyLeftX && leftX < enemyRightX)))){
				if(!falling) return true; // will only return true if Mario is currently not falling, because when he is falling on top of an Enemy, this collision may trigger, but when Mario falls on an Enemy he is beating them not getting hit by the side of them. 
			}
		}
		return false; // returning false since collision was not found
	}

	/*
	 * This method is used to determine what sprite is most appropriate to display for Mario based on his current movement
	 */
	public void chooseSprite() {
		// if dx is greater than 0, we set the sprite to the running right sprite and make the previous variable equal to "r", so we know to make the sprite the standing right sprite when Mario stops moving
		if(dx > 0) {
			current = runR;
			previous = "r";
		} 
		// if dx is greater than 0, we set the sprite to the running left sprite and make the previous variable equal to "l", so we know to make the sprite the standing left sprite when Mario stops moving
		else if(dx < 0) {
			current = runL;
			previous = "l";
		}
		
		// if Mario is jumping or falling and moving right, we set the sprite to jumping right and setting previous variable to "r"
		if((jumping || falling) && dx > 0) {
			current = jumpR;
			previous = "r";
		}
		// if Mario is jumping or falling and moving left, we set the sprite to jumping left and setting previous variable to "l"
		else if((jumping || falling) && dx < 0) {
			current = jumpL;
			previous = "l";
		}
		
		// if Mario is standing still
		if(dx==0) {
			// if Mario is not falling and not jumping, he is on the ground/platform and we make the sprite standing right or standing left according to the previous variable
			if(!falling && !jumping) {
				if(previous.equals("r")) current = right;
				else if(previous.equals("l")) current = left;
			} 
			// if Mario is falling or jumping, we make the sprite the jumping right or jumping left sprite based on the previous variable
			else if(falling || jumping) {
				if(previous.equals("r")) current = jumpR;
				else if(previous.equals("l")) current = jumpL;
			}
		}
	}
		
}
