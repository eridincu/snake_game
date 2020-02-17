package main;

import java.awt.EventQueue;

import game.Direction;
import snakes.Snake;
import snakesGame.SnakesGame;
import ui.ApplicationWindow;



/**
 * The main class of the project.
 */
public class Main {

    /**
     * Main entry point for the application.
     *
     * @param args application arguments
     */
    public static void main(String[]args) {
    	 EventQueue.invokeLater(() -> {
             try {
                 // Create game
                 // You can change the world width and height, size of each grid square in pixels or the game speed
            	 SnakesGame game = new SnakesGame(40, 40, 10, 30);
                 game.addSnake(new Snake(5,2,Direction.LEFT));
                 //Create application window that contains the game panel
                 ApplicationWindow window = new ApplicationWindow(game.getGamePanel());
                 window.getFrame().setVisible(true);

                 // Start game
                 game.start();

             } catch (Exception e) {
                 e.printStackTrace();
             }
         });	
    	
    }
}
