package snakesGame;

import game.Direction;
import snakes.Food;
import snakes.Node;

import java.util.HashMap;
import java.util.List;


/**
 * Class representing the information a snake has about its surroundings.
 * Automatically created and passed by the game to each snake at each timer tick.
 */
public class LocalInformation {

    private int gridWidth;
    private int gridHeight;
    private Food food;
    private HashMap<Direction, Food> foodAround;
    private HashMap<Direction, Node> nodesAround;
    private List<Direction> freeDirections;
    private int lastX;
    private int lastY;   
	/**
     * Constructs the local information for a Snake
     * @param gridWidth width of the grid world
     * @param gridHeight height of the grid world
     * @param nodes mapping of directions to neighbor nodes
	 * @param foodAround mapping of directions to neighbor food
     * @param freeDirections list of free directions
     * @param food the food positioned in the current timertick 
	 * @param lastY 
	 * @param lastX 
     */
    public LocalInformation(int gridWidth, int gridHeight, HashMap<Direction, Node> nodesAround, HashMap<Direction, Food> foodAround, List<Direction> freeDirections, Food food, int lastX, int lastY) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.nodesAround = nodesAround;
        this.freeDirections = freeDirections;
        this.food = food;
        this.foodAround = foodAround;
        this.lastX = lastX;
        this.lastY = lastY;
    }
    /**
     * 
     * @return the hash map for the food, empty if there's no food 
     */
   	public HashMap<Direction, Food> getFoodAround() {
		return foodAround;
	}
   	/**
   	 * getter for food if it's ever needed
   	 * @return the food 
   	 */
	public Food getFood() {
		return this.food;
	}
	public int getLastX() {
		return this.lastX;
	}
	public int getLastY() {
		return this.lastY;
	}
	/**
     * Getter for the width of the grid world.
     * Can be used to assess the boundaries of the world.
     * @return number of grid squares along the width
     */
    public int getGridWidth() {
        return gridWidth;
    }
    /**
     * Getter for the height of the grid world.
     * Can be used to assess the boundaries of the world.
     * @return number of grid squares along the height
     */
    public int getGridHeight() {
        return gridHeight;
    }
    /**
     * Returns the neighbor node one square up
     * @return node or null if no node exists
     */
    public Node getNodeUp() {
        return nodesAround.get(Direction.UP);
    }
    /**
     * Returns the neighbor node one square down
     * @return node or null if no node exists
     */
    public Node getNodeDown() {
        return nodesAround.get(Direction.DOWN);
    }
    /**
     * Returns the neighbor node one square left
     * @return node or null if no node exists
     */
    public Node getNodeLeft() {
        return nodesAround.get(Direction.LEFT);
    }
    /**
     * Returns the neighbor node one square right
     * @return node or null if no node exists
     */
    public Node getNodeRight() {
        return nodesAround.get(Direction.RIGHT);
    }    
    /**
     * Returns the list of free directions around the current position.
     * The list does not contain directions out of bounds or containing a node.
     * Can be used to determine the directions available to move.
     * @return directions or null if no directions exists
     */
    public List<Direction> getFreeDirections() {
        return freeDirections;
    }   
    /**
     * Utility function to get a randomly selected direction among multiple directions.
     * The selection is uniform random: All directions in the list have an equal chance to be chosen.
     * @param possibleDirections list of possible directions
     * @return direction randomly selected from the list of possible directions
     */
    public static Direction getRandomDirection(List<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return null;
        }
        int randomIndex = (int)(Math.random() * possibleDirections.size());
        return possibleDirections.get(randomIndex);
    }
}
