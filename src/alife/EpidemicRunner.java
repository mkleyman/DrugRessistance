package alife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;

public class EpidemicRunner {

static ArrayList<Human> humanList;
static ArrayList<Doctor> doctorList;
static ArrayList<Pathogen> pathogenList;
static ArrayList<Drug> drugList;
static Random rand = new Random();
	public static void main(String[] args)
    {
		ActorWorld world = new ActorWorld();
		spawnPathogens(5);
		LinkedList<Location> loc = spawnDoctors(2, world);
		spawnHumans(5,loc,world);
	    world.show();
    }
	
	public static void spawnPathogens(int numBugs){
		pathogenList = new ArrayList<Pathogen>();
		for(int x=0; x<numBugs; x++){
			pathogenList.set(x,new Pathogen());
		}
	}
	public static void spawnHumans(int numHumans, LinkedList<Location> hospList, ActorWorld world){
		humanList = new ArrayList<Human>();
		for(int x=0; x<numHumans; x++){
			Human dude = new Human(hospList);
			humanList.set(x, dude);
			world.add(world.getRandomEmptyLocation(), dude);
		}
	}
	public static LinkedList<Location> spawnDoctors(int numDoctors, ActorWorld world){
		LinkedList<Location> hospList= new LinkedList<Location>();
		doctorList = new ArrayList<Doctor>();
		for(int x = 0; x<numDoctors; x++){
			//there is no constructor, I'm not sure how this will work
			Doctor dudeMD = new Doctor();
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
			humanList.get(rand.nextInt(humanSize)).getSick(pathogenList.get(index%pathSize)); 
		}
	}
	
}
