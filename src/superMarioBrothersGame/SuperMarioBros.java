package superMarioBrothersGame;
import javax.swing.*;

/*
 * IMPORTANT: HOW TO PLAY GAME
 * "A" KEY TO MOVE LEFT
 * "W" KEY TO MOVE RIGHT
 * HOLD SHIFT KEY WHILE MOVING TO SPRINT/RUN FASTER
 * SPACE BAR TO JUMP
 */

/*
 * SuperMarioBros Class
 * This is the main class that runs the game.
 * Since this game is based on Object-Oriented programming, the "main" class and main method for this game are very short.
 * It creates a few objects, and in those objects (such as Board), there is a chain effect and other objects are created, forming the game. 
 */
public class SuperMarioBros {
	JFrame frame; // declaring a new JFrame that is going to be used to display the game.
	Board gameBoard; // declaring a Board object (this is the second most important object that will create everything that is added to the frame).
	
	public SuperMarioBros() {
		frame = new JFrame("Super Mario Bros."); // titling the frame "Super Mario Bros."
		gameBoard = new Board(); // instantiating the gameBoard object as a new Board.
		frame.add(gameBoard); // adding gameBoard into the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // setting the default close operation as exit on close. This means that the program will close when the X button is clicked on the window.
		frame.setSize(1280, 720); // setting the size of the frame to 1280 pixels by 720 pixels.
		frame.setResizable(false); // setting setResizable to false, so that the frame cannot be resized from its set size of 1280 x 720
		frame.setVisible(true); // seting the JFrame to visible so that it actually displays. 
	}
	
	public static void main(String[] args) {
		SuperMarioBros game = new SuperMarioBros(); // instantiating a new SuperMarioBros object, which starts the game.  
	}

}
