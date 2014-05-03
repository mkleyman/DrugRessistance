package alife;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class Human extends Actor{
	private ArrayList<Pathogen> diseases;
	private boolean sick; 
	private int daysTillVisit = 24;
	private LinkedList<Location> hospitalDirectory = new  LinkedList<Location>();
	private Location closestDoctor;
	private boolean timeForAppointment = false;
	private boolean madeAppointment = false;
	private static double infectionRate  = .3;
	//private HashMap<Drug, Integer> treatments = new HashMap<Drug, Integer>();
	 
	
	 public void turnRight(){
		 setDirection(this.getDirection()+Location.HALF_RIGHT);
	 }
	 //tells human its time for appointment
	 public void callForVisit(){
		 this.timeForAppointment = true;
	 }
	 //moves the human forward
	 public void move(){
		 Grid<Actor> gr = getGrid();
		 if(gr==null){
			 return;
		 }
		 Location loc = getLocation();
		 Location next = loc.getAdjacentLocation(getDirection());
		 if(gr.isValid(next))
			 moveTo(next);
		 else
			 removeSelfFromGrid();
	 }
	 //checks if the human can move forward
	 public boolean canMove(){
		 Grid<Actor> gr = getGrid();
		 if(gr==null){
			 return false;
		 } 
		 Location loc = getLocation();
		 Location next = loc.getAdjacentLocation(getDirection());
		 if(!gr.isValid(next)){
			 return false;
		 }
		 Actor neighbor = gr.get(next);
		 return (neighbor == null) ;
	 }
	 
	 public void turnLeft(){
		 setDirection(this.getDirection()+Location.HALF_LEFT);
	 }
	 
	 public Location findNearestDoctor(){
	
		 Grid<Actor> gr = getGrid();
		 if(gr==null){
			 return null;
		 } 
		 Location loc = getLocation();
		 //Location next = loc.getAdjacentLocation(getDirection());
		 Location best = null;
		 double smallest = Double.MAX_VALUE;
		 //iterates through all hospitals
		 for(Location doc: this.hospitalDirectory){
			 double dist = distance (doc.getRow(), doc.getCol(), loc.getRow(), loc.getCol());
				 
			 if(dist<smallest){
				 best = doc;
				 smallest = dist;
			 }
		 }
		 return best;
		 
	 }
	 private double distance(int x1, int y1, int x2, int y2){
		 return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(x1-x2, 2));
	 }
	 public void infectNeighbors(){
		 Grid<Actor> gr = getGrid();
		 if(gr==null){
			 return;
		 } 
		 Location loc = getLocation();
		 for(Actor victim: gr.getNeighbors(loc)){
			 if(victim instanceof Human){
				 Double random = Math.random();
				 if(random<this.infectionRate){
					 this.infect(((Human)victim));
				 }
			 }
		 }
	 }
	 public void act(){
		 //makes there there is a pause before the next doctor visit
		 if(this.daysTillVisit>0){
			 this.daysTillVisit--;
		 }
		 if(sick && !this.timeForAppointment){
			 this.infectNeighbors();
		 }
		 //makes an appointment to the nearest doctor
	      if(sick && this.daysTillVisit==0 && !this.madeAppointment){
	    	  this.closestDoctor = this.findNearestDoctor();
	    	  ((Doctor)(this.getGrid().get(this.closestDoctor))).makeAppointment(this);
	    	  this.madeAppointment = true;
	      }
	      //goes to the nearest doctor
	      if(this.timeForAppointment){
	    	  int dir =this.getLocation().getDirectionToward(this.closestDoctor);
	    	  this.setDirection(dir);
	    	  if(this.canMove()){
	    		  this.move();
	    	  }
	    	  this.askForTreatment();
	      }
	      //wanders in circles
	      else if(this.madeAppointment){
	    	  if(this.canMove()){
	    		  double rand=Math.random();
	    		  if (rand<.4){
	    			  this.turnLeft();
	    		  }
	    		  else if(rand>.6){
	    			  this.turnRight();
	    		  }
	    		  else this.move();
	    		  
	    	  }
	    	  else{
	    		  this.turnRight();
	    	  }
	      }
	      //wanders mostly in straight lines
	      else{
	    	  if(this.canMove()){
	    		  double rand=Math.random();
	    		  if (rand<.1){
	    			  this.turnLeft();
	    		  }
	    		  else if(rand>.9){
	    			  this.turnRight();
	    		  }
	    		  else this.move();
	    		  
	    	  }
	    	  else{
	    		  this.turnRight();
	    	  }
	    	  
	    	  
	      }
		 	
	}
	
	public Human(){
		this.diseases = new ArrayList<Pathogen> ();
		this.sick = false;
	}
	public Human(LinkedList<Location> directory){
		this.diseases = new ArrayList<Pathogen> ();
		this.sick = false;
		this.hospitalDirectory = directory;
	}
	
	
	public void getSick(Pathogen disease){
		diseases.add(disease);
		sick = true;
	}
	//takes a drug from the doctor
	public boolean takeDrug(Drug treatment){
		boolean effective = false; 
		this.madeAppointment = false;
		this.daysTillVisit =24;
		this.timeForAppointment = false;
		for(int i=0;i<this.diseases.size(); i++){
			int result = treatment.getGeneticCode()^diseases.get(i).getGeneticCode();
			String resultString = Integer.toBinaryString(result);
			int count = 0; 
			for(char c: resultString.toCharArray()){
				if(c == '1'){
					count++;
				}
			}
			if(count>=21){
				diseases.remove(i);
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
	
	public void askForTreatment(){
		 Grid<Actor> gr = getGrid();
		 if(gr==null){
			 return;
		 } 
		 Location loc = getLocation();
		 for(Actor doc: gr.getNeighbors(loc)){
			 if(doc instanceof Doctor){
				 ((Doctor)doc).treat(this);
			 }
		 }
	}
	
	

}
