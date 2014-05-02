package alife;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.grid.Location;

import java.util.ArrayList;
import java.util.HashMap;


public class Human extends Actor{
	private ArrayList<Pathogen> diseases;
	private boolean sick; 
	//private HashMap<Drug, Integer> treatments = new HashMap<Drug, Integer>();
	
	 public void act()
	    {
	       /* if ( canMove())
	        {
	            move();
	            
	        }
	        else
	        {
	            turn();
	           
	        }*/
	    }
	
	public Human(){
		this.diseases = new ArrayList<Pathogen> ();
		this.sick = false;
	}
	public void getSick(Pathogen disease){
		diseases.add(disease);
		sick = true;
	}
	
	public boolean takeDrug(Drug treatment){
		boolean effective = false; 
		for(int i=0;i<this.diseases.size(); i++){
			if((treatment.getGeneticCode()^diseases.get(i).getGeneticCode()) ==0){
				effective = true;
			}
		}
		if(this.diseases.size()==0){
			this.sick = false;
		}
		return effective;
	}
	public void infect(Human other){
		int index =(int) (Math.random()*(this.diseases.size()));
		other.getSick(this.diseases.get(index));
	}
	
	

}
