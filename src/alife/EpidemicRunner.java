package alife;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

public class EpidemicRunner {

static ArrayList<Human> humanList;
static ArrayList<Doctor> doctorList;
static ArrayList<Pathogen> pathogenList;
static ArrayList<Drug> drugList = new ArrayList<Drug>();
static ConcurrentHashMap<String,Integer> diseaseCountMap;
static Drug treatment = new Drug("11111111111111111111111111111111");

static Random rand = new Random();
	public static void main(String[] args)
    {
		diseaseCountMap = new ConcurrentHashMap<String,Integer>();
		ActorWorld world = new ActorWorld(new BoundedGrid(50,50));
		spawnPathogens(1);
		//spawnDrugs(1);
		
		LinkedList<Location> loc = spawnDoctors(10, world);
		spawnHumans(50,loc,world);
		infectRandom(2);
		Tracker watcher = new Tracker( humanList, treatment, diseaseCountMap);
		watcher.setColor(Color.BLACK);
		world.add(world.getRandomEmptyLocation(), watcher);
		/*while(true){
			for(Human person: humanList){
				person.act();
				for(Doctor md: doctorList){
					md.act();
				}
			}
			watcher.act();
		}*/
		
	    world.show();
    }
	public static void spawnDrugs(int numDrugs){
		drugList = new ArrayList<Drug>();
		for(int x = 0; x<numDrugs; x++){
			drugList.add(new Drug(Integer.toBinaryString(rand.nextInt())));
		}
	}
	public static void spawnPathogens(int numBugs){
		pathogenList = new ArrayList<Pathogen>();
		for(int x=0; x<numBugs; x++){
			pathogenList.add(x,new Pathogen());
		}
	}
	public static void spawnHumans(int numHumans, LinkedList<Location> hospList, ActorWorld world){
		humanList = new ArrayList<Human>();
		for(int x=0; x<numHumans; x++){
			Human dude = new Human(hospList);
			humanList.add(x, dude);
			world.add(world.getRandomEmptyLocation(), dude);
		}
		
	}
	public static LinkedList<Location> spawnDoctors(int numDoctors, ActorWorld world){
		LinkedList<Location> hospList= new LinkedList<Location>();
		doctorList = new ArrayList<Doctor>();
		for(int x = 0; x<numDoctors; x++){
			//there is no constructor, I'm not sure how this will work
			//Drug d = new Drug("11111111111111111111111111111111");
			//drugList.add(d);
			Doctor dudeMD = new Doctor(treatment, diseaseCountMap);
			dudeMD.setColor(Color.YELLOW);
			doctorList.add(dudeMD);
			Location loc = world.getRandomEmptyLocation();
			hospList.add(loc);
			world.add(loc, dudeMD);
		}
		return hospList;
	}
	
	/*randomly infects "x" number of humans
	 * used to create the initial infected
	 * population*/
	public static void infectRandom(int x){
		//get the size of the pathogenList
		Pathogen disease = new Pathogen();
		int pathSize = 0;
		for(Pathogen bug: pathogenList){
			pathSize++;
		}
		//get the size of the humanList
		int humanSize = 0;
		for(Human human:humanList){
			humanSize++;
		}
		//pathogens are distributed uniformly, humans receiving them are random
		for(int index = 0; index<x; index++){
			int r = rand.nextInt(humanSize);
			humanList.get(r).getSick(pathogenList.get(index%pathSize));
			humanList.get(r).setColor(Color.RED);
		}
	}
	
}
