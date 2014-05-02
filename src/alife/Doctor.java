package alife;

import java.util.LinkedList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;

public class Doctor extends Actor{
	private int steps  = 0;
	private int sideLength = 6;
	private LinkedList<Human> appointments = new LinkedList<Human>();
	
	public boolean treat(Human patient, Drug treatment){
		boolean effective = patient.takeDrug(treatment);
		treatment.update(effective);
		return effective;
	}
	
	 public void act()
	    {
		 	Human currentPatient = appointments.pop();
		 	currentPatient.callForVisit();
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
	 public void makeAppointment(Human patient){
		 this.appointments.add(patient);
	 }

}
