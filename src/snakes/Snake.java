package snakes;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;

import game.Direction;
import game.Drawable;
import snakesGame.Action;
import snakesGame.Action.Type;
import snakesGame.LocalInformation;
import ui.GridPanel;



public class Snake implements Drawable{
	/**
	 * Creates the snake with its nodes. Includes how the actions function and chooseAction methods
	 */
	
	//represents snake with its body parts(nodes)
	private LinkedList<Node> snake;
	/**
	 * randomly giving one of the seven different colors to the draw method to build a snake's body(not it's head, they are always
	 * orange.
	 */
	private Color[] colors = {Color.BLUE, Color.green, Color.magenta, Color.RED, Color.pink, Color.yellow,Color.ORANGE};
	Random generator = new Random();
	private final int randomNum = generator.nextInt(7);
	
	/**
	 * Constructor for one time use only at the beginning of the game.
	 * @param x coordinate of head
	 * @param y coordinate of head
	 */
	public Snake(int x, int y, Direction direction) {
		snake = new LinkedList<>();		
		switch(direction) {
		case RIGHT: 
			for(int i = 0; i<4;i++) {
				snake.add(new Node(x-i,y));
			}//snake is initially created
			break;
		case LEFT:
			for(int i = 0; i<4;i++) {
				snake.add(new Node(x+i,y));
			}//snake is initially created
			break;
		case UP:
			for(int i = 0; i<4;i++) {
				snake.add(new Node(x,y+i));
			}//snake is initially created
			break;
		case DOWN:
			for(int i = 0; i<4;i++) {
				snake.add(new Node(x,y-i));
			}//snake is initially created
		}
	
		
		
	}
	/**
	 * Constructor for reproducing. Since reproduced snake's location depends on its parent snake, only creates new linkedlist
	 * for new snake.
	 */
	public Snake() {;
		snake = new LinkedList<>();
	}	
	/**
	 * Chooses a valid action for the snake by taking information about around snake's head and returns it
	 * @param localInfo gives the snake the information it needs like free directions and where is food.
	 * @return the appropriate action 
	 */
	public Action chooseAction(LocalInformation localInfo) {
		
		if(!localInfo.getFreeDirections().isEmpty()&&!localInfo.getFoodAround().values().isEmpty()) {//checks food map if there's any food
			
			return new Action(Type.CONSUME, localInfo.getFoodAround().keySet().iterator().next());//takes the direction of the food from the keys of the food map
		}
		else if(!localInfo.getFreeDirections().isEmpty()) {//checks if there's a free direction to move
			
			return new Action(Type.MOVE , LocalInformation.getRandomDirection(localInfo.getFreeDirections()));//move	
		}
		else  {
			
			return new Action(Type.STAY);//no option left, so stay
		}		
	}
	/**
	 * Move method. To move, snake removes its tail node and add it up to its head.
	 * @param direction which direction she will move
	 */
	public void move(Direction direction) {
		
		//coordinates for the head of the snake before moving, to identify where to add the new node to move
		int headX = snake.getFirst().getX();
		int headY = snake.getFirst().getY();
		//moves according to the direction that has been chosen 
		switch(direction) {
		//removes and takes the last node and add it to the head
		case UP:
			snake.addFirst(snake.removeLast());			
			setHeadY(headY-1);
			setHeadX(headX);
			break;
		case DOWN:
			snake.addFirst(snake.removeLast());
			setHeadY(headY+1);
			setHeadX(headX);
			break;
		case LEFT:
			snake.addFirst(snake.removeLast());
			setHeadY(headY);
			setHeadX(headX-1);
			break;
		case RIGHT:
			snake.addFirst(snake.removeLast());
			setHeadY(headY);
			setHeadX(headX+1);
			break;
		}
	}
	/**
	 * The method to add a new node to the snake if it eats a food.
	 * @param direction where to move, also where the food is according to snake's head.
	 */
	public void consume(Direction direction) {
		//adds a new node as a head to the snake to where the food is.
		switch(direction) {
		case UP:
			snake.addFirst(new Node(getHeadX(),getHeadY()-1));
			break;
		case DOWN:
			snake.addFirst(new Node(getHeadX(),getHeadY()+1));
			break;
		case LEFT:
			snake.addFirst(new Node(getHeadX()-1,getHeadY()));
			break;
		case RIGHT:
			snake.addFirst(new Node(getHeadX()+1,getHeadY()));
			break;
		}
	}
	/**
	 * Stay. Snake does nothing
	 */
	public void stay() {
		/**
		 * DO NOTHING
		 */
	}
	/**
	 * creates a new snake by taking parent's snake's last 4 nodes, tail node becomes the head and rest follows it.
	 * @return the new snake that will be produced
	 */
	public Snake reproduce() {
		Snake newSnake = new Snake();
		
		for(int i = 0;i<4;i++) {
			newSnake.getSnakesBody().add(snake.removeLast());
		}
		return newSnake;
		
	}
	/**
	 * method to return linkedlist representing the snake's body.
	 * @return the snake's body.
	 */
	public LinkedList<Node> getSnakesBody() {
		return snake;
	}
	/**
	 * getter for the head node's x coordinate
	 * @return x coordinate of the head
	 */
	public int getHeadX() {
		return snake.getFirst().getX();
	}
	/**
	 * setter for the head node's x coordinate
	 * @param x coordinate wanted to be set
	 */
	public void setHeadX(int x) {
		snake.getFirst().setX(x);
	}
	/**
	 * getter for the head node's y coordinate
	 * @return y coordinate of the head
	 */
	public int getHeadY() {
		return snake.getFirst().getY();
	}
	/**
	 * setter for the head node's y coordinate
	 * @param y coordinate wanted to be set
	 */
	public void setHeadY(int y) {
		snake.getFirst().setY(y);
	}
	
	@Override
	public void draw(GridPanel panel) {
		//drawer method
		panel.drawSquare(getHeadX(),getHeadY(), Color.GRAY);//draws the head in dark gray color
		for(int i = 1; i < snake.size();i++) {
			panel.drawSquare(snake.get(i).getX(), snake.get(i).getY(), colors[randomNum]);
			//takes the randomNum from the start of the class and takes the color which has the index of the randomNum in the array created at the beginning.			
		}
	}
	
	
}
