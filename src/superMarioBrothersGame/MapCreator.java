package superMarioBrothersGame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/*
 * MapCreator Class
 * Used to read an Image and create the map of Blocks and Enemies accordingly
 */
class MapCreator {
    
    /*
     * Method to create the Blocks for the level
     * Pre: a String of the gridmap file name to check. If there are multiple levels, this is convenient
     * Post: an ArrayList of Block objects that contains all the Blocks in the level.
     */
    public static ArrayList<Block> createMapBlocks(String mapPath) {
    	ArrayList<Block> mapBlocks = new ArrayList<Block>(); // instantiating this new ArrayList of Blocks
    	
    	File levelGrid = new File(mapPath); // instantiating a new File from this gridmap image
    	
    	BufferedImage mapImage = new BufferedImage(256, 32, BufferedImage.TYPE_INT_RGB); // instantiating a new buffered image with the dimensions of the gridmap
    	try { 
    	    mapImage = ImageIO.read(levelGrid); // reading the File of the gridmap into the mapImage BufferedImage
    	} 
    	catch (IOException e) { }

        int pixelMultiplier = 48; // since each Block is represented by a singular pixel on the gridmap, we multiply it's position by the pixel multiplier before passing the x and y values into the actual parameters of the Object's instantiation.

        // assigning different RGB colours for the different blocks
        int ordinaryBrick = new Color(0, 0, 255).getRGB();
        int surpriseBrick = new Color(255, 255, 0).getRGB();
        int groundBrick = new Color(255, 0, 0).getRGB();
        int end = new Color(160, 0, 160).getRGB();

        // for loop that cycles through each pixel of the bufferedImage
        for (int x = 0; x < mapImage.getWidth(); x++) {
            for (int y = 0; y < mapImage.getHeight(); y++) {
                int currentPixel = mapImage.getRGB(x, y); // getting the RGB colour value of the current pixel
                
                // multiplying the current x and y location by the pixel multiplier to scale correctly on the actual level.
                int xLocation = x*pixelMultiplier;
                int yLocation = y*pixelMultiplier;
                
                // instantiating different Blocks depending on the colour of the current pixel, creating the map
                if (currentPixel == ordinaryBrick) {
                    mapBlocks.add(new RegularBrick(xLocation, yLocation));
                }
                else if (currentPixel == surpriseBrick) {
                	mapBlocks.add(new QuestionBlock(xLocation, yLocation));
             
                }
                else if (currentPixel == groundBrick) {
                	mapBlocks.add(new GroundBlock(xLocation, yLocation));
                }
                else if(currentPixel == end){
                	mapBlocks.add(new Flagpole(xLocation+20, yLocation));
                }
            }
        }
        
        return mapBlocks; // returning this ArrayList of Blocks
    }
    
    /*
     * Method to create the Enemies for the level
     * Pre: a String of the gridmap file name to check. If there are multiple levels, this is convenient.
     * Post: an ArrayList of Enemy objects that contains all the Enemies in the level.
     */
    public static ArrayList<Enemy> createMapEnemies(String mapPath){
    	ArrayList<Enemy> mapEnemies = new ArrayList<Enemy>(); // instantiating this new ArrayList of Enemies.
    	
    	File levelGrid = new File(mapPath); // instantiating a new File from this gridmap image
    	
    	BufferedImage mapImage = new BufferedImage(256, 32, BufferedImage.TYPE_INT_RGB);// instantiating a new buffered image with the dimensions of the gridmap
    	try { 
    	    mapImage = ImageIO.read(levelGrid); // reading the File of the gridmap into the mapImage BufferedImage
    	} 
    	catch (IOException e) { }

        int pixelMultiplier = 48; // since each Enemy is represented by a singular pixel on the gridmap, we multiply it's position by the pixel multiplier before passing the x and y values into the actual parameters of the Object's instantiation.
    	
        // assigning different RGB colours for the different Enemies
    	int goomba = new Color(0, 255, 255).getRGB();
    	int koopa = new Color(255, 0, 255).getRGB();
    	
    	 for (int x = 0; x < mapImage.getWidth(); x++) {
             for (int y = 0; y < mapImage.getHeight(); y++) {
                 int currentPixel = mapImage.getRGB(x, y); // getting the RGB colour value of the current pixel
                 
                 // multiplying the current x and y location by the pixel multiplier to scale correctly on the actual level.
                 int xLocation = x*pixelMultiplier;
                 int yLocation = y*pixelMultiplier;

                 // instantiating different Enemies depending on the colour of the current pixel
                 if (currentPixel == goomba) {
                	 mapEnemies.add(new Goomba(xLocation, yLocation));
                 } 
                 
                 else if (currentPixel == koopa) {
                	 mapEnemies.add(new Koopa(xLocation, yLocation));
                 }
                

             }
         }
    	
    	return mapEnemies; // returning this ArrayList of Enemies
    }

}