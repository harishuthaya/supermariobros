package superMarioBrothersGame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Board extends JPanel implements ActionListener {
	Mario player; // declaring a Mario object (the protagonist of the game that the player will control)
	Score gameScore; // declaring a Score object, that will hold the Score that the player has accumulated
	Image level1; // an image for the blank level background
	Timer time; // a Timer that will be triggered every 5 milliseconds to trigger the ActionPerformed method and 
	
	boolean finalScoreCalculated = false; // if the user wins the game and score is accumulated for remaining time, this will be set to true, so that if the user hits the X on the dialog box that appears, they won't get more score for remaining time again.
	
	ArrayList<Block> allBlocks; // the ArrayList that will contain all the Blocks in the level
	ArrayList<Enemy> allEnemies; // the ArrayList that will contain all the Enemies in the level
	
	GameTimer levelTimer; // this GameTimer will be used to stop the game once the total starting time of 100 seconds depletes to 0

	public Board() {
		init(); // calling the initialize method
		time = new Timer(5, this); // every 5 ms an ActionPerformed will be triggered, updating player, block, and enemy locations and update them on screen as well
		time.start(); // starting the timer
	}
	
	/*
	 * Initializes the Board. This is used instead of putting everything in the constructor, so that this method can be called, to restart the game. 
	 */
	public void init() {
		player = new Mario(); // instantiating player as a new Mario object
		levelTimer = new GameTimer(); // instantiating levelTimer as a new gameTimer object.
		
		// calling the MapCreator methods to set the allBlocks and allEnemies ArrayLists to have all the Blocks and Enemies respectively.
		allBlocks = MapCreator.createMapBlocks("gridmap1.png");
		allEnemies = MapCreator.createMapEnemies("gridmap1.png");
		
		addKeyListener(new AL()); // adding a key listener using the private AL class
		setFocusable(true); // focusing the Frame on this Board.
		ImageIcon back = new ImageIcon("level1-1.png"); // ImageIcon of the background of the level.
		level1 = back.getImage(); // Image of the background of the level
		
		gameScore = new Score(); // instantiating gameScore as a new Score object
		
		finalScoreCalculated = false; // allows final score to be calculated when hitting the flagpole and the first dialog box pops up (won't do it again if the dialog box is forced close by clicking X)
	}
	
	/*
	 * Opens a dialog box to reset or close the game when the player wins or loses
	 * Pre: String that is either "lose" or "win" to determine the slightly different text that wil be show in the dialog box
	 * Post: void
	 */
	public void resetStatus(String status) {
		if(status.equals("lose")) { // if the player lost the game
			// result of the dialog box is set to int n
			int n = JOptionPane.showConfirmDialog(null, "You lost and your final score was: " + gameScore.getScore() + ". Do you want to restart the game?","Game Over",JOptionPane.YES_NO_OPTION);
			
			switch(n) {
			case 0: init(); break; // if yes was chosen, the resetGame method is called and we break from this.
			case 1: System.exit(0); break; // if no was chosen, the program is terminated and we break from this.
			}
		} else if(status.equals("win")) { // else if the player won the game
			// if the final score has not been calculated yet, we make a new gameSCore by multiplying each remaining second by 10 (10 points per remaining second).
			// then finalSCoreCalculated is set to false so it is not calulated again.
			if(!finalScoreCalculated) {
				gameScore.newScore(levelTimer.getRemainingTime() * 10);
				finalScoreCalculated = true;
			}
			// results of the diagog box is set to int n
			int n = JOptionPane.showConfirmDialog(null, "You won and your final score was: " + gameScore.getScore() + ". Do you want to restart the game?","Game Over",JOptionPane.YES_NO_OPTION);
			
			switch(n) {
			case 0: init(); break; // if yes was chosen, the resetGame method is called and we break from this.
			case 1: System.exit(0); break; // if no was chosen, the program is terminated and we break from this.
			}
		} 
	}

	/*
	 * Triggered every 5 milliseconds from the timer
	 * Pre: ActionEvent
	 * Post: void
	 */
	public void actionPerformed(ActionEvent e) {
		// checks if time has run out and calls the resetStatus method with "lose" since the player has lost.
		if(levelTimer.getRemainingTime() <= 0) {
			resetStatus("lose");
		}
		
		player.move(allBlocks); // calling the player move method to move Mario
		
		// checks for left or right collision of all Blocks for each Enemy
		// if there is collision, the Enemy's dx is set to the opposite sign to make it move in the opposite direction
		for(Enemy theEnemy : allEnemies) {
			if(theEnemy.leftCollision(allBlocks) || theEnemy.rightCollision(allBlocks)) {
				theEnemy.dx = -theEnemy.dx;
			}
			theEnemy.move(player.getNX()); // moving Enemy and passing the current nx value to see if the Enemy needs to adjust for scrolling background
		}
		checkCollisions(); // calls the checkCollisions method
		repaint(); // removes what is currently on screen and calls the paint method
	}
	
	/*
	 * Used to paint Mario, Enemies, Blocks, background, timer, score on screen
	 */
	public void paint(Graphics g) {
		super.paint(g); // calling the parent paint method and passing the Graphics object
		Graphics2D g2d = (Graphics2D) g; // Initializing g2d as g casted as a Graphics2D object
		if(player.getY() > player.startY) player.y = player.startY; // Emergency case to bring player back to surface level is player falls throguh ground
		g2d.drawImage(level1, player.getNX(), 0, null); // drawing the background level in the horizontal position of nx to use scrolling background effect
		
		// for-each loop that cycles through all the blocks and calls their respective move methods and draws them
		for(Block theBlock : allBlocks) {
			theBlock.move(player.getNX());
			g2d.drawImage(theBlock.getImage(), theBlock.getX(), theBlock.getY(), null);
		}
		
		// for-each loop that cycles through all the enemies and draws them.
		for(Enemy theEnemy : allEnemies) {
			g2d.drawImage(theEnemy.getImage(), theEnemy.getX(), theEnemy.getY(), null);
		} 
		
		g2d.drawImage(player.getImage(), player.getX(), player.getY(), null); // drawing Mario (player)
		
		// drawing the Images for the Score and GameTimer sprites and then drawing the Strings for score and remaining time.
		g2d.setColor(Color.WHITE); // setting font colour to white
		g2d.setFont(new Font("Courier", Font.BOLD, 20)); // setting the font of the text to a new fount using Courier, making it bold, and size 20
		g2d.drawString("x " + gameScore.getScore(), 60, 32);
		g2d.drawString(levelTimer.getRemainingTime() + "", 60, 93);
		g2d.drawImage(gameScore.getImage(), 5, 5, null);
		g2d.drawImage(levelTimer.getImage(), 5, 56, null);
		
	}
	
	/*
	 * Used to checkCollisions for player and Enemies
	 * Pre: none
	 * Post: void
	 */
	public void checkCollisions() {		
		// for-each loop that checks for bottom and top collision with the player for all the blocks
		for(Block currentBlock : allBlocks) {
			
			// if the player collided with the bottom of a block that is a question block, we add 100 to their score, change their type back to normal ("") and change its sprite to its alternate
			if(currentBlock.type.equals("question")) {
				if(player.bottomCollision(currentBlock)) {
					currentBlock.current = currentBlock.alternateSprite;
					currentBlock.type = "";
					gameScore.newScore(100);
				} 
			}
			
			// if Mario collides on either side or the top of a Flagpole, then call the resetStatus method and pass "win" since the player has won
			if(currentBlock.type.equals("end")) {
				if(player.leftCollision(currentBlock) || player.topCollision(currentBlock) || player.rightCollision(currentBlock)) {
					resetStatus("win");
				}
			}
		}
	
		// for-each loop that goes through all the enemies
		for(Enemy theEnemy : allEnemies) {
			// if the player has collision with the top of the enemy while falling
			if(player.topCollision(theEnemy)) {
				if(player.falling) {
					theEnemy.active = false; // when an enemy is set to inactive, it's x and y position will not be affected in the move method
					if(theEnemy.getLength() == 48) gameScore.newScore(50); // This will let us know if it is a Goomba (Goomba's have a height of 48 pixels), and beating a Goomba adds 50 to score
					else if(theEnemy.getLength() == 96) gameScore.newScore(100); // This will let us know if it is a Koopa (Goomba's have a height of 48 pixels), and beating a Goomba adds 50 to score
					
					// The x and y position of the enemy is set to -1000000 to move it to a position otuside of the map, so that the player won't see it anymore/defeat the same enemy twice
					theEnemy.setX(-1000000);
					theEnemy.setY(-1000000);
				}
			}
		}
		
		// if the player collides with the bottom, or either side of an enemy, then they have been hit and lose the game, so we call the resetStatus method and pass "lose" since the player has lost.
		if(player.bottomCollision(allEnemies, "") || player.leftCollision(allEnemies, "") || player.rightCollision(allEnemies, "")) {
			resetStatus("lose");
		}
	}
	
	/*
	 * AL (ActionListener) Class - private
	 * This class is a child of KeyAdapter and is used to pass keyPressed and keyReleased events into the player keyPressed and keyReleased methods.
	 */
	private class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e); // when key is pressed, this KeyEvent is passed to the player keyPressed method
		}
		
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e); // when key is released, this KeyEvent is passed to the player keyReleased method
		}

	}
	
}
