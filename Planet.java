package stanford;


import java.awt.Color;
import java.util.ArrayList;

import acm.graphics.GCompound;
import acm.graphics.GLine;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.util.RandomGenerator;

public class Planet extends GCompound{
	
	public static final double G = .00015; //Gravitational constant G converted to pixels
	public static final double minRadius = 25; //the minimum radius that can be calculated to avoid ridiculous speeds
	private static final int numPoints = 8;  //number of points being collisionChecked for on each planet
	private static final Color heroColor = new Color(95, 0, 239);
	
	public Planet(double theX, double theY, double theDiameter, double theVx, double theVy, int theMassConstant, boolean moveAble) {
		massConstant = theMassConstant;
		
		canMove = moveAble;
		isReady = moveAble;
		
		diameter = theDiameter;
	
		mass = diameter * diameter * massConstant;
		
		x = theX / 2;   //this x coordinate needs to be halfed for some reason
		y = theY / 2;		//and the y
		
		vx = theVx;
		vy = theVy;
		
		
		int value = (9 - (massConstant)) * 255 / 9;
		color = new Color(130, value, value);
		
		
		circle = new GOval((x) - (diameter / 2), (y) - (diameter / 2), diameter, diameter);   //I straight up have no idea why I have to divide x and y by two
		circle.setFilled(true);
		circle.setColor(color);
		add(circle);
		
		planets.add(Planet.getNumPlanets(), this);
		
		numPlanets++;
	}
	
	public Planet(double theX, double theY, double theDiameter, double theVx, double theVy, int theMassConstant)
	{
		massConstant = theMassConstant;
		
		canMove = true;
		
		diameter = theDiameter;
	
		mass = diameter * diameter * massConstant;
		
		x = theX / 2;   //this x coordinate needs to be halfed for some reason
		y = theY / 2;		//and the y
		
		vx = theVx;
		vy = theVy;
		
		
		int value = (9 - (massConstant)) * 255 / 9;
		color = new Color(130, value, value);
		
		
		circle = new GOval((x) - (diameter / 2), (y) - (diameter / 2), diameter, diameter);   //I straight up have no idea why I have to divide x and y by two
		circle.setFilled(true);
		circle.setColor(color);
		add(circle);
		
		planets.add(Planet.getNumPlanets(), this);
		
		numPlanets++;
	}
	
	public Planet(double theX, double theY, double theDiameter, double theVx, double theVy)
	{
		
		canMove = true;
		
		diameter = theDiameter;
	
		mass = diameter * diameter * massConstant;
		
		x = theX / 2;
		y = theY / 2;
		
		vx = theVx;
		vy = theVy;
		
		int value = (9 - (massConstant)) * 255 / 9;
		color = new Color(130, value, value);
		
		circle = new GOval((x) - (diameter / 2), (y) - (diameter / 2), diameter, diameter);   //I straight up have no idea why I have to divide x and y by two
		circle.setFilled(true);
		circle.setColor(color);
		add(circle);
		
		planets.add(Planet.getNumPlanets(), this);
		
		numPlanets++;
	}
	
	public Planet(double theX, double theY) {
		
		canMove = true;
		
		massConstant = getMassConstant();
		
		diameter = 16;
		
		mass = diameter * diameter * massConstant;
		
		x = theX / 2;
		y = theY / 2;
		
		vx = 0;
		vy = 0;
		
		int value = (9 - (massConstant)) * 255 / 9;
		color = new Color(130, value, value);
		
		circle = new GOval((x) - (diameter / 2), (y) - (diameter / 2), diameter, diameter);   //I straight up have no idea why I have to divide x and y by two
		circle.setFilled(true);
		circle.setColor(color);
		add(circle);
		
		planets.add(Planet.getNumPlanets(), this);
		
		numPlanets++;
	}


	

	public void pull(Planet otherPlanet){
		
		double force = getForce(otherPlanet);
		double direction = getDirection(otherPlanet); 
		
		
		double dVx = (force * Math.cos(direction)) / mass;
		double dVy = (force * Math.sin(direction)) / mass;
		
		vx += dVx;
		vy += dVy;
		
		
		/*double dOtherX = (force * Math.cos(direction)) / otherPlanet.getMass();
		double dOtherY = (force * Math.sin(direction)) / otherPlanet.getMass();
		
		otherPlanet.addV()
		
		otherPlanet.circle.move(-vx, -vy);*/
	}
	
	private double getForce(Planet otherPlanet) {
		
		double radiusBetween = getRadiusBetween(otherPlanet);
		
		if(radiusBetween < minRadius) radiusBetween = minRadius; //stops ridiculous speeds without implementing bouncing yet
		
		return (G * mass * otherPlanet.getMass()) / (Math.pow(radiusBetween, 2));
	}
	
	private double getDirection(Planet otherPlanet) {
		
		double dX = (otherPlanet.getX() - getX());	
		double dY = (otherPlanet.getY() - getY());
		
		double direction = Math.atan(dY / dX);
		
		if(dX < 0) direction += Math.PI; //this switches the direction correctly to account for the range of arc tan
		
		return direction;
	}
	

	private double getRadiusBetween(Planet otherPlanet) {
		
		double dX = (otherPlanet.getX() - getX());	
		double dY = (otherPlanet.getY() - getY());
		
		return Math.sqrt((dX * dX) + (dY * dY));
	}

	public void orbit(Planet planetToOrbit) {   //won't work if radius is smaller than minradius
		
		if(this != planetToOrbit){
			
			double newDirection = (Math.PI / 2) + getDirection(planetToOrbit);
			double radius = getRadiusBetween(planetToOrbit);
			
			double vt = Math.sqrt((G * planetToOrbit.getMass() / radius));
			
			canMove = true;
			
			vx = vt * Math.cos(newDirection);
			vy = vt * Math.sin(newDirection);
		}
	}
	

	public void bounce(PlanetWall line){
		
		int killPower = line.getKill();
		
		if(killPower == 0){
			
			setNewVelocity(line);
		}
		else if(killPower == 1){
			
			if(isHero){
				removeSelf();
			}
			else{
				setNewVelocity(line);
			}
			
		}
		else if(killPower == 2){
			removeSelf();
			
			if(!isHero) {
				System.out.println("There are " + planets.size() + " planets left!");
			}	
			
		}
		
	}
	
	private void setNewVelocity(PlanetWall line) {      //something with the direction at which it hits the wall messes this up
		
		double slope = line.getSlope();
		
		if(slope == 0.0001){
			vx = -vx;
		}
		else if(slope == 0){
			vy = -vy;
		}
		else{
			double rotation = (Math.PI / 2) - Math.atan(slope);
			
			System.out.println("before: " + getVDirection());
			
			
			double direction = getVDirection() + rotation; 
			System.out.println("after: " + direction);
			
			
			double velocity = getVelocity();
			
			vx = velocity * Math.cos(direction);
			
			vy = velocity * Math.sin(direction);
			
		}
	}

	public void removeSelf(){
		remove(circle);
		planets.remove(this);
		numPlanets--;
	}
	
	public void movePlanet(){
			circle.move(vx, vy);
	}
	
	public static Planet planetsGet(int i){
		return planets.get(i);
	}
	
	public void changeColor(Color color) {
		circle.setColor(color);
	}
	
	public double getDiameter() {
		return diameter;
	}
	
	public Color getColor(){
		int value = (9 - (massConstant)) * 255 / 9;
		return new Color(130, value, value);
	}
	
	
	public double getMass()
	{
		return mass;
	}
	
	
	public void setMassConstant(int theMass){
		massConstant = theMass;
		mass = diameter * diameter * massConstant;
	}
	
	public int getMassConstant(){
		return massConstant;
	}
	
	public double getX(){
		return circle.getX() + (diameter / 2);
	}
	
	public double getY(){
		return circle.getY() + (diameter / 2);
	}
	

	public static double getTotalMomentumX() {
		double momentumX = 0;
		
		for(int i = 0; i < numPlanets; i++){
			momentumX += (planets.get(i).getMass() * planets.get(i).getVx());
		}
		
		return momentumX;
	}
	
	public static double getTotalMomentumY() {
		double momentumY = 0;
		
		for(int i = 0; i < numPlanets; i++){
			momentumY += (planets.get(i).getMass() * planets.get(i).getVy());
		}
		
		return momentumY;
	}
	
	public boolean isHero(){
		return isHero;
	}
	
	public void setHero(){
		isHero = true;
		color = heroColor;
		circle.setColor(color);
	}
	
	public void setVx(double theV){
		vx = theV;
	}
	
	public void setVy(double theV){
		vy = theV;
	}
	
	public void stopPlanet(){
		vx = 0;
		vy = 0;
	}
	
	public void setPosition(double x, double y){
		circle.setLocation((x / 2) - (diameter / 2), (y / 2) - (diameter / 2));
	}
	
	
	public double getVx(){
		return vx;
	}
	
	public double getVy(){
		return vy;
	}
	
	public double getVelocity(){
		return Math.sqrt((vx * vx) + (vy * vy));
	}
	
	public double getVDirection(){
		
		if(vx == 0){
			if(vy > 0){
				return Math.PI / 2;
			}
			else if(vy < 0){
				return -Math.PI / 2;
			}
			else{
				return 0;
			}
		}
		
		double direction = Math.atan(vy / vx);
		
		if(vx < 0) direction += Math.PI; //this switches the direction correctly to account for the range of arc tan
		
		return direction;
	}
	
	public static int getNumPlanets(){
		return planets.size();
	}
	
	public void increaseSize(){
		diameter++;
		mass = diameter * diameter * massConstant;
		circle.setSize(diameter, diameter);
		circle.move(-.5, -.5);
	}
	
	public void decreaseSize(){
		if(diameter > 1){
			diameter--;
			mass = diameter * diameter * massConstant;
			circle.setSize(diameter, diameter);
			circle.move(.5, .5);
		}
		
	}
	
	public boolean isReady() {
		
		return isReady;
	}
	
	public boolean getMoveable(){
		return canMove;
	}
	
	public void setMoveable(boolean trueOrFalse){
		canMove = trueOrFalse;
	}
	
	public void setReady(boolean trueOrFalse){
		
		isReady = trueOrFalse;
	}
	
	public static int getNumPoints(){
		return numPoints;
	}
	
	public static void clearAll(int numPlanets) {
		for(int i = 0; i < numPlanets; i++){
			planets.remove(0);
		}
	
	}
	
	
	public String toString(){
		return "The mass of this guy is " + mass;
	}
	
	
	
	
	private static int numPlanets = 0;
	
	private double diameter;
	private double mass;
	private double vx;
	private double vy;
	private double x;
	private double y;
	private GOval circle;
	private boolean isReady = false;
	private int massConstant = 1;
	private Color color;
	private boolean canMove;
	private boolean isHero = false;

	
	public static ArrayList<Planet> planets = new ArrayList<Planet>();

	

	
	

	


	


	


	

	
}
