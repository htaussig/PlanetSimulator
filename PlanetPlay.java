package stanford;


import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class PlanetPlay extends GraphicsProgram{         

	private static final int HEIGHT = 970;
	private static final int WIDTH = 1919;
	
	private static final int MINDIAMETER = 16;
	private static final int MAXDIAMETER = 60;
	private static final int NUMPLANETS = 4;  //a value here greater than 0 generates random planets
	
	public static final double MAXSPEED = .7;
	
	private static final int WALLKILL = 1; //decides whether default walls and border walls kill
	private static final boolean BORDERWALLS = true; //creates walls around the edges of the screen
	
	private static final boolean STORYMODE = false;
	private static final int NUMGOALSANDSTARTS = 2;
	
	private static final int TIMESDRAGGEDINTERVAL = 110;
	
	
	private static final double DRADIANS = (2 * Math.PI) / Planet.getNumPoints();
	
	//private static final int MAXSAVEPLANETS = 15; //max number of planets momentum conversion works for
	
	private Planet mousePlanet;
	private Planet hero;
	private PlanetGoal lastGoal;   //this might be unnecessary
	private PlanetStart lastStart;
	
	private double[][] lastXY = new double[TIMESDRAGGEDINTERVAL][2];
	private int timesDragged;
	private boolean mouseDown;
	private double x,y;
	private boolean waitingForNextPlanet = false;
	private boolean win = false;
	
	private int levelNumber = 0;
	private PlanetLevel currentLevel;
	
	public void run(){
		
		addMouseListeners();
		addKeyListeners();
		
		
		if(STORYMODE){
			
			initializeLevels();
			
			introduceLevel();
			
		}
		else{
			createPlanets();
			createBoundaries();
		}
		
		while(true){
			
			planetMovement();
			
			bouncePlanets();
			
			if(STORYMODE && !win){
				checkWin();
			}
			
			dragVelocity();
			
			pause(1);
		}
	}
	


	private void initializeLevels() {
		
		PlanetLevel level1 = new PlanetLevel(new PlanetStart(50, 750, 150, 150));
		
		level1.addGoal(new PlanetGoal(1670, 50, 200, 200));
		
		level1.addPlanet(new Planet(WIDTH / 2, HEIGHT / 2, 90, 0, 0, 8, false));
		
		level1.addWall(new PlanetWall(WIDTH / 2, 0, WIDTH / 2, HEIGHT / 3, 1));
		
		
		
		PlanetLevel level2 = new PlanetLevel(new PlanetStart(50, 750, 150, 150));
		
		level2.addGoal(new PlanetGoal(1670, 50, 200, 200));
		
		level2.addPlanet(new Planet(WIDTH / 2, HEIGHT / 2, 90, 0, 0, 8, false));
		
		level2.addWall(new PlanetWall(WIDTH / 2, 0, WIDTH / 2, HEIGHT / 2 - 43, 1));
		level2.addWall(new PlanetWall(WIDTH / 2, HEIGHT, WIDTH / 2, HEIGHT / 2 + 43, 1));
		
		
		
		PlanetLevel level3 = new PlanetLevel(new PlanetStart(50, 750, 150, 150));
	
		level3.addGoal(new PlanetGoal(1670, 50, 200, 200));
		
		level3.addPlanet(new Planet(WIDTH / 3, HEIGHT * 2 / 3, 90, 0, 0, 8, false));
		level3.addPlanet(new Planet(WIDTH * 2 / 3, HEIGHT / 3, 90, 0, 0, 8, false));
		
		level3.addWall(new PlanetWall(WIDTH / 3, HEIGHT, WIDTH / 3, HEIGHT * 2 / 3, 1));
		level3.addWall(new PlanetWall(WIDTH * 2 / 3, 0, WIDTH * 2 / 3, HEIGHT / 3, 1));
		
		
		PlanetLevel level4 = new PlanetLevel(new PlanetStart(50, 750, 150, 150));
		
		level4.addGoal(new PlanetGoal(1670, 50, 200, 200));
		
		level4.addPlanet(new Planet(WIDTH / 3, HEIGHT / 3, 90, 0, 0, 8, false));
		level4.addPlanet(new Planet(WIDTH * 2 / 3, HEIGHT * 2 / 3, 90, 0, 0, 8, false));
		
		level4.addWall(new PlanetWall(WIDTH / 3, HEIGHT, WIDTH / 3, HEIGHT / 3, 1));
		level4.addWall(new PlanetWall(WIDTH * 2 / 3, 0, WIDTH * 2 / 3, HEIGHT * 2 / 3, 1));
		
		
		PlanetLevel level5 = new PlanetLevel(new PlanetStart(50, (HEIGHT / 2) - 75, 150, 150));
		
		int diam = 60;
		
		for(int i = 0; i < 5; i++){
			
			int x = (int)((WIDTH - (((i / 2.0) - 1) * ((WIDTH - 100) / 2))) / 2);
			
			
			if(i % 2 == 0){
				
				int yDiff = (HEIGHT / 2) - 100;
				
				level5.addWall(new PlanetWall(x, 0, x, yDiff, 1));
				level5.addPlanet(new Planet(x, yDiff, diam, 0, 0, 6, false));
			}
			else{
				
				int yDiff = ((HEIGHT / 2) + 100);
				
				level5.addWall(new PlanetWall(x, HEIGHT, x, yDiff, 1));
				level5.addPlanet(new Planet(x, yDiff, diam, 0, 0, 6, false));
			}
		}
		
		level5.addGoal(new PlanetGoal(WIDTH - 200, 200, 150, HEIGHT - 400));
		
		
		
		PlanetLevel level6 = new PlanetLevel(new PlanetStart(50, (HEIGHT / 2) - 75, 150, 150));
		
		diam = 50;
		
		for(int i = 0; i < 5; i++){
			
			int x = (int)((WIDTH - (((i / 2.0) - 1) * ((WIDTH - 100) / 2))) / 2);
			
			if(i % 2 == 0){
				
				int yDiff = (HEIGHT / 2) - 100;
			
				if(i == 4){
					level6.addWall(new PlanetWall(x , yDiff, x, (HEIGHT / 2) + 36, 1));
				}
				
				level6.addWall(new PlanetWall(x, 0, x, yDiff, 1));
				level6.addPlanet(new Planet(x, yDiff, diam, 0, 0, 6, false));
			}
			else{

				int yDiff = ((HEIGHT / 2) + 100);
				
				level6.addWall(new PlanetWall(x, HEIGHT, x, yDiff, 1));
				level6.addPlanet(new Planet(x, yDiff, diam, 0, 0, 6, false));
			}
		}
		
		level6.addGoal(new PlanetGoal(WIDTH - 200, 200, 150, HEIGHT - 400));
		
		
		PlanetLevel level7 = new PlanetLevel(new PlanetStart(50, 750, 150, 150));
		
		level7.addGoal(new PlanetGoal(1670, 50, 200, 200));
		
		level7.addPlanet(new Planet(WIDTH / 4, HEIGHT / 2, 60, 0, 0, 3, false));
		level7.addPlanet(new Planet(WIDTH * 3 / 4, HEIGHT / 2, 60, 0, 0, 3, false));
		
		level7.addPlanet(new Planet(WIDTH / 2, HEIGHT / 5, 20, 0, 0, 9, false));
		level7.addPlanet(new Planet(WIDTH / 2, HEIGHT * 4 / 5, 20, 0, 0, 9, false));
		
		level7.addWall(new PlanetWall(WIDTH / 2, HEIGHT / 3, WIDTH / 2, HEIGHT * 2 / 3, 0));
		level7.addWall(new PlanetWall(WIDTH * 5 / 8, HEIGHT / 12, WIDTH * 5 / 8, HEIGHT / 3, 0));
		
	}

	private void introduceLevel() {   
		
		currentLevel = PlanetLevel.getLevel(levelNumber);
		
		createGoals();
		createStart();
		createPlanets();
		createBoundaries();
	}



	private void planetMovement() {
		
		for(int i = 0; i < Planet.getNumPlanets(); i++){
			
			for(int j = 0; j < Planet.getNumPlanets(); j++){
				
				if(j != i && Planet.planetsGet(j).isReady() && Planet.planetsGet(i).isReady() && Planet.planetsGet(i).getMoveable()){
					
					Planet.planetsGet(i).pull(Planet.planetsGet(j));
				}
			
			}
		}
			
			
			for(int i = 0; i < Planet.getNumPlanets(); i++){
				
				if(Planet.planetsGet(i).isReady() && Planet.planetsGet(i).getMoveable()){
					
					Planet.planetsGet(i).movePlanet();
					
			}
		}
	}
	


	@SuppressWarnings("unused")
	private void createPlanets(){  //remember all planet coordinates are doubled for some crazy reason
		
		if(NUMPLANETS > 0){

			double radius = MAXDIAMETER / 2.0;
			
			double minX = radius + 2 + (WIDTH / 3); //one so that the planets don't get destroyed immediately
			double minY = radius + 2 + (HEIGHT / 3);
			double maxX = WIDTH - radius - 2 - (WIDTH / 3); //subtracting and adding /3s to center planets
			double maxY = HEIGHT - radius - 2 - (HEIGHT / 3);
					
			
			for(int i = 0; i < NUMPLANETS; i++){
				introducePlanet(new Planet(rgen.nextDouble(minX, maxX), rgen.nextDouble(minY, maxY), rgen.nextInt(MINDIAMETER, MAXDIAMETER), 0, 0, rgen.nextInt(1,9)));
			}
			
		}
		
		if(!STORYMODE){
			//introducePlanet(WIDTH / 2, HEIGHT / 2, 90, 0, 0, 9, false);
			
			//introducePlanet(new Planet(125, 400, MINDIAMETER, 0, -.1, 9));
		}
		else{
			createHero();
			
			for(int i = 0; i < currentLevel.getNumPlanets(); i++){
				introducePlanet(currentLevel.getPlanet(i));
			}
		}
		
	}
	

	private void createHero() {   //probs shouldn't make this public
		
		double x = lastStart.getX1() + (lastStart.getWidth() / 2);
		double y = lastStart.getY1() + (lastStart.getHeight() / 2);
		
		Planet thePlanet = new Planet(x, y, MINDIAMETER, 0, 0, 3);
		add(thePlanet);
		thePlanet.setHero();
		hero = thePlanet;
		
		zAxis(thePlanet);
	}

	private void createBoundaries() {
		
		if(BORDERWALLS){
			createBorderWalls();
		}	
		
		if(STORYMODE){
			for(int i = 0; i < currentLevel.getNumWalls(); i++){
				introduceWall(currentLevel.getWall(i));
			}
			
		}
		else{
			//introduceWall(new PlanetWall(100, 100, 200, 200, 0));
			//introduceWall(new PlanetWall(100, 100, 0, 200, 0));
		}
		
		
	}

	private void createBorderWalls() {
		introduceWall(new PlanetWall(0, 0, 0, HEIGHT, WALLKILL));
		
		introduceWall(new PlanetWall(0, 0, WIDTH, 0, WALLKILL));
		
		introduceWall(new PlanetWall(WIDTH, 0, WIDTH, HEIGHT, WALLKILL));
		
		introduceWall(new PlanetWall(0, HEIGHT, WIDTH, HEIGHT, WALLKILL));
	}


	private void createGoals() {
		
		for(int i = 0; i < currentLevel.getNumGoals(); i++){
			introduceGoal(currentLevel.getGoal(i));
		}
		
		/*introduceGoal(1670, 50, 200, 200);
		setTopBound(lastGoal, 1);
		setRightBound(lastGoal, 1);*/
	}
	
	private void createStart(){
		
		introduceStart(currentLevel.getStart());
		
		//introduceStart(50, 750, 150, 150);
	}
	
	private void introduceWall(PlanetWall wall){
		add(wall);
		wall.sendToFront(); 
	}
	
	private void introducePlanet(Planet thePlanet){
		
		add(thePlanet);
		thePlanet.setReady(true);
		
		zAxis(thePlanet);
	}
	
	private void zAxis(Planet planet) {
		
		planet.sendToBack();
		
		if(STORYMODE){
			for(int i = 0; i < NUMGOALSANDSTARTS; i++){        //keeps the planets in front of the goals but behind everything else
				planet.sendForward();
			}
		}
	}

	private void introduceGoal(PlanetGoal goal){
		add(goal);
		goal.sendToBack();
		
		lastGoal = goal;
	}
	
	private void introduceStart(PlanetStart start){
		add(start);
		start.sendToBack();
		
		lastStart = start;
	}
	

	private void bouncePlanets() {
		
		for(int i = 0; i < Planet.getNumPlanets(); i++){
			
			Planet thePlanet = Planet.planetsGet(i);
			boolean isHero = thePlanet.isHero();
			
			if(getCollidingObjectWith(thePlanet) instanceof PlanetWall){
				
				PlanetWall collider = (PlanetWall) getCollidingObjectWith(thePlanet);

				thePlanet.bounce(collider);
				
				if(isHero && collider.getKill() >= 1){
					loseLife();
				}
			}
		}
	}
	
	private void loseLife() {
		
		System.out.println("You died! Try again");
		
		createHero();
	}
	
	private void resetHero(){
		
		hero.removeSelf();
		
		createHero();
		
	}

	private GObject getCollidingObjectWith(Planet planet) {  //add one on each coordinate so that it doesn't see itself with getElementAt()
		
		double start = (Math.PI / -2);
		
		double size = 0 + (planet.getDiameter() / 2);
		
		
		for(int i = 0; i < Planet.getNumPoints(); i++){
			
			GPoint point = new GPoint((size * Math.cos(start + (i * DRADIANS))) + (planet.getX() * 2), (size * Math.sin(start + (i * DRADIANS))) + (planet.getY() * 2));
			
			
			if(!(getElementAt(point) instanceof Planet) && getElementAt(point) != null){
				return getElementAt(point);
			}
		}
		
		return null;                     //need to make sure this looks for a  specific kind of Object, otherwise it will see it is in a goal and not think to bounce
	}
	


	private void checkWin() {
		
		double start = (Math.PI / -2);
		
		double size = 1 + (hero.getDiameter() / 2);
		
		
		for(int i = 0; i < Planet.getNumPoints(); i++){
			
			GPoint point = new GPoint((size * Math.cos(start + (i * DRADIANS))) + (hero.getX() * 2), (size * Math.sin(start + (i * DRADIANS))) + (hero.getY() * 2));
			
			
			if(!(getElementAt(point) instanceof PlanetGoal)){
				break;
			}
			else if(i == Planet.getNumPoints() - 1){
				win();
			}
		}
	}

	private void win(){
		hero.removeSelf();
		win = true;
		System.out.println("You win! :)");
		
		GLabel win = new GLabel("You did it! :)", WIDTH / 2, HEIGHT / 2);
		add(win);
		win.setFont("Helvetica-36");
		pause(2000);
		
		nextLevel();
		
	}
	
	private void skipLevel(){
		
		hero.removeSelf();
		win = true;
		System.out.println("cheater!");
		
		nextLevel();
		
	}

	private void nextLevel() {
		Planet.clearAll(currentLevel.getNumPlanets());
		removeAll();
		
		win = false;
		
		levelNumber++;
		
		if(PlanetLevel.getNumLevels() > levelNumber){
			
			GLabel level = new GLabel("Level " + (levelNumber + 1), WIDTH / 2, HEIGHT / 2);
			add(level);
			level.setFont("Helvetica-36");
			pause(1000);
			
			introduceLevel();
			
			remove(level);
		}
		else{
			endGame();
		}
	}




	private void endGame() {
		GLabel win = new GLabel("You beat all the levels!", WIDTH / 2, HEIGHT / 2);
		add(win);
		win.setFont("Helvetica-36");
	}



	public void setAllBounds(PlanetGoal goal, int killPower){
		setLeftBound(goal, killPower);
		setRightBound(goal, killPower);
		setTopBound(goal, killPower);
		setBottomBound(goal, killPower);
	}
	
	
	public void setLeftBound(PlanetGoal goal, int killPower) {
		int x1 = goal.getX1();
		int y1 = goal.getY1();
		int height = goal.getGoalHeight();
		
		introduceWall(new PlanetWall(x1, y1, x1, y1 + height, killPower));
	}
	
	public void setRightBound(PlanetGoal goal, int killPower) {
		int x1 = goal.getX1();
		int y1 = goal.getY1();
		int height = goal.getGoalHeight();
		int width = goal.getGoalWidth();
		
		introduceWall(new PlanetWall(x1 + width, y1, x1 + width, y1 + height, killPower));
	}
	
	public void setTopBound(PlanetGoal goal, int killPower) {
		int x1 = goal.getX1();
		int y1 = goal.getY1();
		int width = goal.getGoalWidth();
		
		introduceWall(new PlanetWall(x1, y1, x1 + width, y1, killPower));
	}
	
	public void setBottomBound(PlanetGoal goal, int killPower) {
		int x1 = goal.getX1();
		int y1 = goal.getY1();
		int height = goal.getGoalHeight();
		int width = goal.getGoalWidth();
		
		introduceWall(new PlanetWall(x1, y1 + height, x1 + width, y1 + height, killPower));
	}

	public void mousePressed(MouseEvent e) {
		
		x = e.getX();
		y = e.getY();
		
		timesDragged = 0;
		
		if(!waitingForNextPlanet){
			
			mouseDown = true;
			
			
			
			if(!STORYMODE){
			
				if(getElementAt(x,y) instanceof Planet){         //PLANET CHECK
					
					mousePlanet = (Planet) getElementAt(x,y);
					
					mousePlanet.setPosition(x,y);
				}
				else{
					
					mousePlanet = new Planet(x, y);
					add(mousePlanet);
					zAxis(mousePlanet);
				}
				
				mousePlanet.stopPlanet();
			}
			else if(getElementAt(x,y) instanceof Planet){
				
				mousePlanet = (Planet) getElementAt(x,y);
				
				if(mousePlanet.isHero() && isInStart(x, y)){
					mousePlanet.setPosition(x,y);
				}
				
			}
			
		}
		else if (getElementAt(x, y) instanceof Planet){
			Planet planetToOrbit = (Planet) getElementAt(x, y);
			mousePlanet.orbit(planetToOrbit);
			
		}
		
	}
	
	
	
	public void mouseDragged(MouseEvent e){   //this method always refers to the planet in mousePressed
		
		if(!waitingForNextPlanet){                     //mouse can be dragged in start zone while the hero is out of it to return it
			x = (double) e.getX();
			y = (double) e.getY();
			
			if(!STORYMODE || (mousePlanet.isHero() && isInStart(x, y))){
				mousePlanet.setPosition(x,y);
				mousePlanet.stopPlanet();
				mousePlanet.setReady(false);
			}
		}
		
	}
	

	private boolean isInStart(double x, double y) {
		double start = (Math.PI / -2);
		
		double size = 1 + (hero.getDiameter() / 2);
		
		
		for(int i = 0; i < Planet.getNumPoints(); i++){
			
			GPoint point = new GPoint((size * Math.cos(start + (i * DRADIANS))) + (x), (size * Math.sin(start + (i * DRADIANS))) + (y));
			
			
			
			if(!(getElementAt(point) instanceof PlanetStart || (getElementAt(point) instanceof Planet && ((Planet) getElementAt(point)).isHero()))){
				break;
			}
			else if(i == Planet.getNumPoints() - 1){
				return true;
			}
		}
		
		return false;
	}



	private void dragVelocity() {
		
		if(mouseDown && !waitingForNextPlanet){
			
			lastXY[timesDragged % TIMESDRAGGEDINTERVAL][0] = x;
			lastXY[timesDragged % TIMESDRAGGEDINTERVAL][1] = y;
			timesDragged++;
			
		}
		
		
	}
	
	public void mouseReleased(MouseEvent e){
		
		if(((!waitingForNextPlanet && mouseDown) && !STORYMODE) || (mouseDown && mousePlanet.isHero() && isInStart(hero.getX() * 2, hero.getY() * 2))){
			double time = TIMESDRAGGEDINTERVAL * 10 / 1000.0; //timesDraggedInterval, DELAY, number of milliseconds in a second   //should the times Dragged be considered 19 or 20?
			
			mouseDown = false;
			
			mousePlanet.setReady(true);
			
			double dx = 0;
			double dy = 0;
			
			if(timesDragged > TIMESDRAGGEDINTERVAL){
				dx = (((double) e.getX() - lastXY[(timesDragged + 1) % TIMESDRAGGEDINTERVAL][0]) / time) / 200; //divide by 100 to get velocity every .1 seconds like the program runs
				dy = (((double) e.getY() - lastXY[(timesDragged + 1) % TIMESDRAGGEDINTERVAL][1]) / time) / 200; 
			}
		
			if(!STORYMODE){
				if(dx > 1) dx = 1;
				if(dx < -1) dx = -1;
				if(dy > 1) dy = 1;
				if(dy < -1) dy = -1;
			}
			else{
				if(dx > MAXSPEED) dx = MAXSPEED;
				if(dx < -MAXSPEED) dx = -MAXSPEED;
				if(dy > MAXSPEED) dy = MAXSPEED;
				if(dy < -MAXSPEED) dy = -MAXSPEED;
			}
			
			
			
			mousePlanet.setVx(dx);
			mousePlanet.setVy(dy);
			
			
		}
		else{
			waitingForNextPlanet = false;
			mousePlanet.setReady(true);
			
		}
		
		
		
	}


	@SuppressWarnings("unused")
	public void keyPressed(KeyEvent e){
		
		int key = e.getKeyCode();
		
		if(key == 38){                   //up arrow for increase
			mousePlanet.increaseSize();
		}
		else if(key == 40){             //down arrow for decrease
			mousePlanet.decreaseSize();
		}
		else if(STORYMODE && key == 82){  //r to restart
			resetHero();
		}
		else if(STORYMODE && key == 83){  // s to skip level
			skipLevel();
		}
		else if(!STORYMODE && key == 32){   //space to stop
			if(mousePlanet.getMoveable()){
				mousePlanet.setMoveable(false);
			}
			else{
				mousePlanet.setMoveable(true);
			}
		}
		else if(!STORYMODE && key == 79){  //O for orbit
			mousePlanet.setMoveable(false);
			waitingForNextPlanet = true;
		}
		else if(key == 67){                     //c for conservation of momentum
			
			double momentumX = Planet.getTotalMomentumX();
			double momentumY = Planet.getTotalMomentumY();
				
			System.out.println("The total momentum of the system of " + Planet.getNumPlanets() + " planets is X: " + momentumX + " and y " +  momentumY);
			//I think there may be an extra planet in the planet list
		}
		else {                             //1-9 for density
			for(int i  = 49; i < 58; i++){
				if(e.getKeyCode() == i){
					if(!STORYMODE){
						mousePlanet.setMassConstant(i - 48);
						mousePlanet.changeColor(mousePlanet.getColor());
					}
					else{
						levelNumber = i - 48 - 1 - 1; //need to be one level lower, and levelNumber starts at 0
						nextLevel();
					}
				}
			}
		}
	}
	
	
	public static RandomGenerator rgen = RandomGenerator.getInstance();
	
}