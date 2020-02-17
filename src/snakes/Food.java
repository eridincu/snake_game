package snakes;

import java.awt.Color;

import game.Drawable;
import ui.GridPanel;

public class Food extends Node implements Drawable{

	/**
	 * 
	 * @param x food's x coordinate
	 * @param y food's y coordinate
	 * 
	 */	
	public Food(int x, int y) {
		super(x,y);
	}
	//drawer method
	@Override
	public void draw(GridPanel panel) {
		panel.drawSquare(getX(), getY(), Color.BLACK);
	}
	
}
