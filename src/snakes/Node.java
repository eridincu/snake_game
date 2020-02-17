package snakes;



public class Node {
	/**
	 * Creates parts of the snake and food.
	 */
	private int x;//x position of a node
	private int y;//y position of a node
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * getter for x
	 * @return the x coordinate of node
	 */
	public int getX() {
		return x;
	}
	/**
	 * setter for x
	 * @param x the x coordinate that will be set for node 
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * getter for y
	 * @return the y coordinate of node
	 */
	public int getY() {
		return y;
	}
	/**
	 * setter for y
	 * @param y the y coordinate that will be set for node
	 */
	public void setY(int y) {
		this.y = y;
	}
	
}