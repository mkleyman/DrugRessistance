package alife;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;

public class Doctor extends Actor{
	private int steps  = 0;
	private int sideLength = 6;
	
	public boolean treat(Human patient, Drug treatment){
		boolean effective = patient.takeDrug(treatment);
		treatment.update(effective);
		return effective;
	}
	
	 public void act()
	    {
	        /*if (steps < sideLength && canMove())
	        {
	            move();
	            steps++;
	        }
	        else
	        {
	            turn();
	            turn();
	            steps = 0;
	        }*/
	    }

}
