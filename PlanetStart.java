package stanford;

import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GRect;

public class PlanetStart extends GCompound{
	
	public PlanetStart(int theX1, int theY1, int theWidth, int theHeight){
		
		x1 = theX1;
		y1 = theY1;
		
		width = theWidth;
		height = theHeight;
		
		rect = new GRect(x1, y1, width, height);
		rect.setColor(new Color(150, 208, 242));
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
