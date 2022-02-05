package superMarioBrothersGame;

import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * The GameTimer class
 * This class is used to create and display the countdown timer for the game.
 */
public class GameTimer {
	// startTime will be the current internal time when the GameTimer is instantiated.
	double startTime, timeElapsed;
	
	int totalTime = 100000; // 100 seconds in milliseconds is the time allotted for our game.
	int remainingTime; // this variable will be the time remaining from the 100 seconds allotted for the game.
	
	Image timerSprite; // sprite for the timer icon that will be displayed beside the text displaying the remaining time. 
	
	public GameTimer() {
		startTime = System.currentTimeMillis(); // calculating the beginning time
		
		ImageIcon timerImage = new ImageIcon("timerimg.png"); // getting the image icon for the timer sprite
		timerSprite = timerImage.getImage(); // getting the image from the image icon
		timerSprite = timerSprite.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // scaling the sprite to 50 pixels by 50 pixels
	}
	
	/*
	 * Used to calculate and return the remaining time from the 100 second total time
	 * Pre: none
	 * Post: int (the remaining time)
	 */
	public int getRemainingTime() {
		timeElapsed = System.currentTimeMillis() - startTime;
		remainingTime = (int)((totalTime - timeElapsed) / 1000);
		if(remainingTime < 0) remainingTime = 0;
		return remainingTime;
	}
	
	/*
	 * Used to return the image for the timer sprite
	 * Pre: none
	 * Post: Image (the sprite for the timer)
	 */
	public Image getImage() {
		return timerSprite;
	}
	
}
