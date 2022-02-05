package superMarioBrothersGame;

import java.awt.Image;
import javax.swing.*;

/*
 * Score class
 * Involves everything for the score of the game: score sprite, score, method to increase score, etc.
 */
public class Score {
	int totalScore; // represents the total score of the game
	int changeScore; // represents how much the score is going to be increased by when the newScore method is called
	JLabel displayScore; // a JLabel that will be used to display the score as text in the corner of the screen.
	
	Image coinSprite; // a sprite of a Mario Bros. coin that represents score.
	
	public Score() {
		totalScore = 0; // initialize the totalScore to start at 0.
		
		ImageIcon coinImage = new ImageIcon("coin.png"); // Creating an ImageIcon to get the coin sprite png file
		coinSprite = coinImage.getImage(); // getting the Image from the ImageIcon
		coinSprite = coinSprite.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // scaling the sprite to 50 pixels by 50 pixels.
	}
	
	/*
	 * This method is used to increment the score when an enemy has been defeaten, coins have been taken from a question block, or the end has been reached and points are being added for time remaining
	 * Pre: int for the number to increase the score by
	 * Post: void
	 */
	public void newScore(int changeScore) {
		totalScore += changeScore;
	}
	
	/*
	 * Get method for the total score. Used to set the JLabel to the latest/updated score.
	 * Pre: none
	 * Post: int that is the total score.
	 */
	public int getScore() {
		return totalScore;
	}
	
	/*
	 * Get method for the coin sprite, displayed beside the score
	 * Pre: none
	 * Post: Image (this coin sprite)
	 */
	public Image getImage() {
		return coinSprite;
	}

}
