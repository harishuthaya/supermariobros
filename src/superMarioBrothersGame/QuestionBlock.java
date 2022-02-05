package superMarioBrothersGame;

import java.awt.Image;

import javax.swing.ImageIcon;

/*
 * QuestionBlock class
 * Child class of Block class. This is one type of Block.
 * This unique block has a main and alternate sprite. It contains points that can be attained when Mario collides with the bottom of it. 
 */
public class QuestionBlock extends Block {
	ImageIcon question, used; // ImageIcons for the main sprite and the sprite once points have been taken from the block
	Image questionBlock, usedBlock; // two Images, one for each of these sprites
	
	public QuestionBlock(int xPosition, int yPosition) {
		super(xPosition, yPosition); // calling the Block constructor with the x and y position of the Question Block.
		question = new ImageIcon("questionbrick.gif"); // getting the ImageIcon for the main sprite
		questionBlock = question.getImage(); // getting the Image from the ImaceIcon
		questionBlock = questionBlock.getScaledInstance(48, 48, Image.SCALE_DEFAULT); // scaling the block to 48 pixels by 48 pixels
		
		used = new ImageIcon("emptybrick.png"); // getting the ImageIcon for the alternate sprite (that will be switched to once points are taken from the Question Block). 
		usedBlock = used.getImage(); // getting the Image from the ImaceIcon
		usedBlock = usedBlock.getScaledInstance(48, 48, Image.SCALE_DEFAULT); // scaling the block to 48 pixels by 48 pixels
		
		// current is set to questionBlock as that is the starting sprite
		current = questionBlock;
		// once Mario collides with the bottom of the Block and gets the points, it will switch to usedBlock sprite
		alternateSprite = usedBlock;
		
		type = "question"; // since this is a special block, its type is set to "question"
	}
	
}
