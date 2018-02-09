package stanford;

import java.util.ArrayList;

public class PlanetLevel {

	public PlanetLevel(PlanetStart theStart){
		//A PlanetLevel is really just data, so we don't need to create anything, it is created in the main program
		
		start = theStart;   //but every level needs a start
		
		levels.add(this);
	}
	
	public void addPlanet(Planet planet){
		startingPlanets.add(planet);
	}
	

	public void addWall(PlanetWall wall){
		startingWalls.add(wall);
	}
	
	public void addGoal(PlanetGoal goal){
		startingGoals.add(goal);
	}
	
	public void setStart(PlanetStart theStart){
		start = theStart;
	}
	
	
	public Planet getPlanet(int i){
		return startingPlanets.get(i);
	}
	
	public PlanetWall getWall(int i){
		return startingWalls.get(i);
	}
	
	public PlanetGoal getGoal(int i){
		return startingGoals.get(i);
	}
	
	public PlanetStart getStart(){
		return start;
	}
	
	
	public int getNumPlanets(){
		return startingPlanets.size();
	}
	
	public int getNumWalls(){
		return startingWalls.size();
	}
	
	public int getNumGoals(){
		return startingGoals.size();
	}
	
	
	public static PlanetLevel getLevel(int i){
		return levels.get(i);
	}
	
	public static int getNumLevels(){
		return levels.size();
	}
	

	
	private ArrayList<Planet> startingPlanets = new ArrayList<Planet>();
	private ArrayList<PlanetWall> startingWalls = new ArrayList<PlanetWall>();
	private ArrayList<PlanetGoal> startingGoals = new ArrayList<PlanetGoal>();
	
	private static ArrayList<PlanetLevel> levels = new ArrayList<PlanetLevel>();
	
	private PlanetStart start;
	
	
	
}
