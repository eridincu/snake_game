package snakesGame;

import game.Direction;
import game.GridGame;
import snakes.Food;
import snakes.Node;
import snakes.Snake;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that implements the game logic for Snakes.
 *
 */
public class SnakesGame extends GridGame {

    private List<Snake> snakess;
    private Node[][] nodesMap;
    private Food food;

    /**
     * Creates a new Nature Simulator game instance
     * @param gridWidth number of grid squares along the width
     * @param gridHeight number of grid squares along the height
     * @param gridSquareSize size of a grid square in pixels
     * @param frameRate frame rate (number of timer ticks per second)
     */
    public SnakesGame(int gridWidth, int gridHeight, int gridSquareSize, int frameRate) {
    	super(gridWidth, gridHeight, gridSquareSize, frameRate);
        snakess = new ArrayList<>();
        nodesMap = new Node[gridWidth][gridHeight];
        try {
        	addSnake(new Snake(4,1,Direction.RIGHT));
        	updateNodesMapWithoutRemovingTail(snakess.get(0));  
        } catch(Exception e) {
        	e.getMessage();
        	System.out.println("You entered illegal gridWidth and gridHeight. Therefore, default snake cannot be created.\n"
        			+ "You should at least enter gridWidth as 5 and gridHeight as 2.");
        }
        addFood();               
    }	
    @Override
    protected void timerTick() {
        // Determine and execute actions for all creatures
        ArrayList<Snake> snakeHeadsCopy = new ArrayList<>(snakess);
        //counts snakes that stay in that timer tick
        int countStay = 0;
        for (Snake snake : snakeHeadsCopy) {        	        	
        
        	//Create local Information
            LocalInformation localInfo = createLocalInformationForCreature(snake);
            // Choose action
            Action action = snake.chooseAction(localInfo);
            
            // Execute action
            if (action != null) {
            	switch(action.getType()) {
            	case STAY:
            		// STAY
                    snake.stay();
                    countStay++; 
                    break;
            	case MOVE:
            		 // MOVE
                    if (isDirectionFree(snake.getHeadX(), snake.getHeadY(), action.getDirection())) {
                    	//UPDATE MAP TO REMOVE THE TAIL'S POSITION BEFORE MOVEMENT FROM THE MAP SO THAT MAP CAN STAY REFRESHED                    	
                    	snake.move(action.getDirection());                    	                    	
                        updateNodesMap(snake,localInfo.getLastX(),localInfo.getLastY());
                    }
                    break;
            	case CONSUME:
            		if (isDirectionFree(snake.getHeadX(), snake.getHeadY(), action.getDirection())) {
                		// CONSUME    
            			removeFood();
                		snake.consume(action.getDirection());
                		
                   		// IF SNAKE CAN REPRODUCE
                		if(isReproduce(snake)) {
                			// UPDATE MAP TO REMOVE THE TAIL'S PREVIOUS POSITION FROM MAP SO THAT NEW SNAKE CAN SPAWN
                        	deleteRemainingNode(localInfo.getLastX(),localInfo.getLastY());                        	
                        	//REPRODUCE
                        	addSnake(snake.reproduce());
                           	//UPDATE MAP WITHOUT REMOVING TAIL
                        	updateNodesMapWithoutRemovingTail(snake);                        	
                        	//ADD NEW FOOD              		
                			addFood();                        	
                		}
                		// IF SHE CANT REPRODUCE
                		else {
                			updateNodesMapWithoutRemovingTail(snake);                			
                			//ADD NEW FOOD
                   			addFood();
                		}                		
                	}
            		break;
            	}                 
            }
            // IF ALL SNAKES ARE STAYING, END THE GAME.
          	if(countStay==snakeHeadsCopy.size()) {
          		stop();
          	}
        }
    }
	private boolean isReproduce(Snake snake) {
		return snake.getSnakesBody().size()>=8;
	}
    /**
     * adds a new food to the game, if food is ever spawned on a snake, tries again.
     */
    private void addFood() {    	
    	 
		do {
			food = new Food((int) (Math.random() * getGridWidth()),(int)( Math.random() * getGridHeight()));
		} while (!(nodesMap[food.getX()][food.getY()] == null));
        addDrawable(food);
        nodesMap[food.getX()][food.getY()] = food;
	} 
    private void removeFood() {    	
    	nodesMap[food.getX()][food.getY()] = null;
    	removeDrawable(food);
    }
    /**
     * adds a new snake to the game 
     * @param snake to be added
     * @return to see whether addition is a success
     */
    public boolean addSnake(Snake snake) {
    	if(snake!=null) {
    		
    		if(isPositionInsideGrid(snake.getHeadX(), snake.getHeadY())) {
    			if (nodesMap[snake.getHeadX()][snake.getHeadY()] == null) {
                  snakess.add(snake);
                  updateNodesMapWithoutRemovingTail(snake);//warning
                  addDrawable(snake);
                  return true;
              } else {
            	  System.out.println("You can't put a snake here.");
                  return false;
              }
    		} else {
    			System.out.println("You can't put a snake here");
    			return false;
    		}
    		
    	
    	}
    	else {
    		return false;
    	}
    }
    /**
     * Creates local information for snake
     * @param snake that local information is created for
     * @return the information snake needs
     */
    private LocalInformation createLocalInformationForCreature(Snake snake) {
        
    	int x = snake.getHeadX();
        int y = snake.getHeadY();
        //mapping for nodes around the snake
        HashMap<Direction, Node> nodesAround = setNodesAround(x, y);
        //mapping for food
        HashMap<Direction, Food> foodAround = setFoodAround(x, y);
        //sets free directions
        ArrayList<Direction> freeDirections = setFreeDirections(x, y, nodesAround);
        //checks if food is neighbor
        ArrayList<Direction> foodDirections = setFoodDirections(x, y, freeDirections);
        //if food is neighbor of the snake, changes the free direction list to food direction list
        if(!foodDirections.isEmpty()) {
        	freeDirections = foodDirections;
        }          
        int lastX = snake.getSnakesBody().getLast().getX();
    	int lastY = snake.getSnakesBody().getLast().getY();

        return new LocalInformation(getGridWidth(), getGridHeight(), nodesAround, foodAround, freeDirections, food, lastX, lastY);
    }
    /**
     * puts nodes around the snake's head into that hash map.
     * @param x position of snake's head
     * @param y position of snake's head
     * @return nodes around, empty if no nodes appear
     */
	private HashMap<Direction, Node> setNodesAround(int x, int y) {
		HashMap<Direction, Node> nodesAround = new HashMap<>();
        nodesAround.put(Direction.UP, getNodesAtPosition(x, y - 1));
        nodesAround.put(Direction.DOWN, getNodesAtPosition(x, y + 1));
        nodesAround.put(Direction.LEFT, getNodesAtPosition(x - 1, y));
        nodesAround.put(Direction.RIGHT, getNodesAtPosition(x + 1, y));
		return nodesAround;
	}
	/**
	 * puts the food in the map if it's around
	 * @param x position of the snake's head
	 * @param y position of the snake's head
	 * @return food around, empty if there's no food
	 */
	private HashMap<Direction, Food> setFoodAround(int x, int y) {
		HashMap<Direction, Food> foodAround = new HashMap<>();
        if(getFoodAtPosition(x,y-1)!=null) {
        	foodAround.put(Direction.UP, getFoodAtPosition(x, y - 1));
        }
        else if(getFoodAtPosition(x,y+1)!=null) {
        	foodAround.put(Direction.DOWN, getFoodAtPosition(x, y + 1));
        }
        else if(getFoodAtPosition(x-1,y)!=null) {
        	foodAround.put(Direction.LEFT, getFoodAtPosition(x - 1, y));
        }
        else if(getFoodAtPosition(x+1,y)!=null) {
        	foodAround.put(Direction.RIGHT, getFoodAtPosition(x + 1, y));
        }
		return foodAround;
	}
	/**
	 * using nodes around, returns a list that contains free directions
	 * @param x of the head's position
	 * @param y of the head's position
	 * @param nodesAround map that holds the nodes around
	 * @return available directions as a list
	 */
	private ArrayList<Direction> setFreeDirections(int x, int y, HashMap<Direction, Node> nodesAround) {
		ArrayList<Direction> freeDirections = new ArrayList<>();
        if (isDirectionFree(x,y,Direction.UP) && isPositionInsideGrid(x, y - 1)) {
            freeDirections.add(Direction.UP);
        }
        if (isDirectionFree(x,y,Direction.DOWN) && isPositionInsideGrid(x, y + 1)) {
            freeDirections.add(Direction.DOWN);
        }
        if (isDirectionFree(x,y,Direction.LEFT) && isPositionInsideGrid(x - 1, y)) {
            freeDirections.add(Direction.LEFT);
        }
        if (isDirectionFree(x,y,Direction.RIGHT) && isPositionInsideGrid(x + 1, y)) {
            freeDirections.add(Direction.RIGHT);
        }
		return freeDirections;
	}
	/**
	 * looks for a better direction which makes snake closer to the food. 
	 * written using (else if)s instead of (if)s to move snakes parallel so they conflict less with each other.
	 * @param x of snake's head
	 * @param y of snake's head
	 * @param freeDirections directions taken from available directions by looking around the head
	 * @return a list which contains the direction that makes snake closer to the food
	 */
	private ArrayList<Direction> setFoodDirections(int x, int y, ArrayList<Direction> freeDirections) {
		ArrayList<Direction> foodDirections = new ArrayList<>();
        	
		if(Math.abs(food.getX()-(x-1))<Math.abs(food.getX()-(x))&&freeDirections.contains(Direction.LEFT)) {    	
			foodDirections.add(Direction.LEFT);    					   		
		}
		if(Math.abs(food.getX()-(x+1))<Math.abs(food.getX()-(x))&&freeDirections.contains(Direction.RIGHT)) {
    		foodDirections.add(Direction.RIGHT);	
   		}
		if(Math.abs(food.getY()-(y-1))<Math.abs(food.getY()-(y))&&freeDirections.contains(Direction.UP)) {
   			foodDirections.add(Direction.UP);  			
    	}
		if(Math.abs(food.getY()-(y+1))<Math.abs(food.getY()-(y))&&freeDirections.contains(Direction.DOWN)) {
    		foodDirections.add(Direction.DOWN);  			
   		}
		return foodDirections;
	}
	//checks whether position is free in map
	private boolean isPositionFreeOrHaveFood(int x, int y) {
        return (isPositionInsideGrid(x, y) && getNodesAtPosition(x, y) == null) ||
        		(isPositionInsideGrid(x, y) && getNodesAtPosition(x, y) instanceof Food);
	}
	/**
	 * checks whether target direction is free in map
	 * @param x of the snake's head
	 * @param y of the snake's head
	 * @param direction that will be checked whether it's empty
	 * @return another method which returns a boolean by checking the position in that direction
	 */
    private boolean isDirectionFree(int x, int y, Direction direction) {
        if (direction == null) {
            return false;
        }
        int xTarget = x;
        int yTarget = y;
        if (direction == Direction.UP) {
            yTarget--;
        } else if (direction == Direction.DOWN) {
            yTarget++;
        } else if (direction == Direction.LEFT) {
            xTarget--;
        } else if (direction == Direction.RIGHT) {
            xTarget++;
        }
        return isPositionFreeOrHaveFood(xTarget, yTarget);
    }
    /**
     * prevents from going out of the map
     * @param x of position
     * @param y of position
     * @return true if position is inside the grid
     */
	private boolean isPositionInsideGrid(int x, int y) {
        return (x >= 0 && x < getGridWidth()) && (y >= 0 && y < getGridHeight());
    }
	/**
	 * Update node map when snake moves or reproduces
	 * @param snake that makes the action
	 * @param x of the tail's position before moving
	 * @param y of the tail's position before moving
	 */
    private void updateNodesMap(Snake snake, int x, int y) {
    	
    	LinkedList<Node> snakesBody = snake.getSnakesBody();
    	
    	for(Node node : snakesBody) {
    		int xOfNode = node.getX();
   			int yOfNode = node.getY();
   			if (isPositionInsideGrid(xOfNode,yOfNode)) {
   				nodesMap[xOfNode][yOfNode] = node;
    		}
    	}
    	//delete the position of the tail after she moves since tail follows its head
    	deleteRemainingNode(x,y);
    }
   /**
    * deletes the joint in given coordinates
    * @param x of the joint
    * @param y of the joint
    */
    private void deleteRemainingNode(int x, int y) {
    	nodesMap[x][y] = null;
    }
    /**
     * If snake consumes, her tail remains still so that's why I need that method. Also, at the very start
     * since food may be spawned on the tail if tail gets removed at the beginning, I prevent it by using
     * that method
     * @param snake that takes the action
     */
    private void updateNodesMapWithoutRemovingTail(Snake snake) {
    	
    	for(Node node :snake.getSnakesBody()) {
    		int xOfNode = node.getX();
   			int yOfNode = node.getY();
   			if (isPositionInsideGrid(xOfNode, yOfNode)) {
   				nodesMap[xOfNode][yOfNode] = node;
    		}
    	}
    }        	
    /**
     * checks a location if there's a food
     * @param x of the position wanted to be checked
     * @param y of the position wanted to be checked
     * @return the food on that position, if there's any
     */
    private Food getFoodAtPosition(int x, int y) {
    	if(!isPositionInsideGrid(x,y)) {
    		return null;
    	}
    	if(food.getX()==x&&food.getY()==y) {
    		return food;
    	}
    	else {
    		return null;
    	}
    }
   /**
    * checks a location if there is a joint
    * @param x of the position wanted to be checked
    * @param y of the position wanted to be checked
    * @return the node on that position, if there's any
    */
    private Node getNodesAtPosition(int x, int y) {
        if (!isPositionInsideGrid(x, y)) {
            return null;
        }
        return nodesMap[x][y];
    }

}
