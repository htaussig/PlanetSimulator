package stanford;

import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GLine;

public class PlanetWall extends GCompound{
	
	
	
	public PlanetWall(int theX1, int theY1, int theX2, int theY2, int theKillPower){
		
		X1 = theX1;
		X2 = theX2;
		Y1 = theY1;
		Y2 = theY2;
		killPower = theKillPower;
		
		line = new GLine(X1, Y1, X2, Y2);
		line.setColor(getWallColor());
		add(line);
		
		/*if(getSlope() == .0001){
			line = new GLine(X1 + 1, Y1, X2 + 1, Y2);
			line.setColor(getWallColor());
			add(line);
			
		}
		else{
			for(int i = 0; i < 2; i++){
				line = new GLine(X1, Y1 + i, X2, Y2 + i);
				line.setColor(getWallColor());
				add(line);
			}
			
		}*/
		
	}
	
	public double getSlope(){
		
		if(X2 - X1 != 0){
			return (Y2 - Y1) / (X2 - X1);
		}
		
		return 0.0001;
		
	}
	
	private Color getWallColor() {
		if(killPower == 1){
			return new Color(8, 0, 255);
		}
		else if(killPower == 2){
			return new Color(229, 18, 18);
		}
		return Color.BLACK;
	}
	
	public int getKill(){
		return killPower;
	}
	
	
	public String toString(){
		return "This is a wall at X1: " + X1 + ", Y1: " + Y1 + ", X2: " + X2 + ", Y2: " + Y2;
	}
	
	
	
	private int X1, X2;
	private int Y1, Y2;
	private int killPower;
	private GLine line;
	
}
