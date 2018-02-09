package stanford;

import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GRect;

public class PlanetGoal extends GCompound{
	
	public PlanetGoal(int theX1, int theY1, int theWidth, int theHeight){
		
		x1 = theX1;
		y1 = theY1;
		
		width = theWidth;
		height = theHeight;
		
		rect = new GRect(x1, y1, width, height);
		rect.setColor(new Color(172, 234, 171));
		rect.setFilled(true);
		add(rect);
		
	}
	
	public int getX1(){
		return x1;
	}
	
	public int getY1(){
		return y1;
	}
	
	public int getGoalWidth(){
		return width;
	}
	
	public int getGoalHeight(){
		return height;
	}

	public GRect getRect(){
		return rect;
	}

	private int x1;
	private int y1;
	private int width;
	private int height;
	private GRect rect;
	
}
