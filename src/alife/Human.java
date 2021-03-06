package alife;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;


public class Human extends Actor{
	private ArrayList<Pathogen> diseases;
	private boolean sick; 
	private int daysTillVisit = 24;
	private LinkedList<Location> hospitalDirectory = new  LinkedList<Location>();
	private Location closestDoctor;
	private boolean timeForAppointment = false;
	private boolean madeAppointment = false;
	private double infectionRate  = 1;
	private HashSet<String> immune;
	private int drugStrength =29;
	//private HashMap<Drug, Integer> treatments = new HashMap<Drug, Integer>();


	 public void turnRight(){
		 setDirection(this.getDirection()+Location.HALF_RIGHT);
	 }
	 //tells human its time for appointment
	 public void callForVisit(){
		 this.timeForAppointment = true;
	 }
	 
	 public boolean isTimeForAppointment(){
		 return this.timeForAppointment;
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
		 this.mutateDiseases();
		 //makes there there is a pause before the next doctor visit
		 if(this.daysTillVisit>0){
			 this.daysTillVisit--;
		 }
		 if(sick && !this.timeForAppointment){
			 this.infectNeighbors();
		 }
		 //makes an appointment to the nearest doctor
	      if(sick && this.daysTillVisit==0 && !this.madeAppointment ){
	    	  this.closestDoctor = this.findNearestDoctor();
	    	  ((Doctor)(this.getGrid().get(this.closestDoctor))).makeAppointment(this);
	    	  this.madeAppointment = true;
	      }
	      //goes to the nearest doctor
	      if(this.timeForAppointment){
	    	  this.askForTreatment();
	    	  int dir =this.getLocation().getDirectionToward(this.closestDoctor);
	    	  this.setDirection(dir);
	    	  if(this.canMove()){
	    		  this.move();
	    	  } 
	      }
	      //wanders in circles
	      /*else if(this.madeAppointment){
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
	      }*/
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
	       if(sick){
	    	  // diseases = evolvePathogen(Pathogen.getMutationRate(),Pathogen.getCrossoverRate());
	      }

	}
	
	public Human(){
		this.diseases = new ArrayList<Pathogen> ();
		this.sick = false;
		this.immune = new HashSet<String>();
	}
	public Human(LinkedList<Location> directory){
		this.diseases = new ArrayList<Pathogen> ();
		this.sick = false;
		this.hospitalDirectory = directory;
		this.immune = new HashSet<String>();
	}

	public boolean isSick(){
		return this.sick;
	}

	public void getSick(Pathogen disease){
		if(!immune.contains(disease.getGeneticCode())){
			diseases.add(disease);
			sick = true;
			this.setColor(Color.RED);
		}
		
	}
	
	public ArrayList<Actor> getNeighbors(){

		return this.getGrid().getNeighbors(this.getLocation());
	}
	//takes a drug from the doctor
	public int takeDrug(Drug treatment){
		//System.out.println("Try Drug");
		boolean effective = false; 
		this.madeAppointment = false;
		this.daysTillVisit =24;
		int effectiveCount  = 0;
		this.timeForAppointment = false;
		for(int i=0;i<this.diseases.size(); i++){
			String resultString = Doctor.xor(treatment.getGeneticCode(),diseases.get(i).getGeneticCode());
			//String resultString = Integer.toBinaryString(result);
			int count = 0; 
			System.out.println(resultString);
			for(char c: resultString.toCharArray()){
				if(c == '1'){
					count++;
				}
			}
			//System.out.println("count");
			//System.out.println(count);
			if(count>=this.drugStrength){
				//System.out.println("Drug Worked");
				immune.add(diseases.get(i).getGeneticCode());
				this.sick = false;
				this.setColor(Color.BLUE);
				diseases.remove(i);
				effective = true;
				effectiveCount++;
			}
		}
		if(this.diseases.size()==0){
			this.sick = false;
			this.setColor(Color.BLUE);
		}
		return effectiveCount;
	}
	
	public String printImmunity(){
		String result  = "";
		for(String code: immune){
			result+=code+"\n";
		}
		return result;
	}

	public ArrayList<Pathogen> getDiseases(){
		return this.diseases;
	}
	public int numDiseases(){
		return this.diseases.size();
	}
	public void infect(Human other){
		int index =(int) (Math.random()*(this.diseases.size()));
		other.getSick(new Pathogen(this.diseases.get(index).getGeneticCode()));
	}
	
	public String getDiseaseCodes(){
		HashSet<String> diseaseSet = new HashSet<String> ();
		for(Pathogen disease: this.diseases){
			diseaseSet.add(disease.getGeneticCode());
		}
		String result= "";
		for(String  disease: diseaseSet){
			result+= disease+"\n";
		}
		return result;
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
				 ((Doctor)doc).free();
			 }
		 }
	}
	/*removes duplicate Pathogens*/
	public ArrayList<Pathogen> consolodateDiseases(){
		ArrayList<Pathogen> list = new ArrayList<Pathogen>(diseases);
		int size = list.size();
		for(Pathogen bug: diseases){
			for(int x =0; x<size; x++){
				if (bug.getGeneticCode().equals(list.get(x).getGeneticCode())){
					list.remove(x);
					size--;
					break;
				}
			}
		}
		return list;
	}
	
	private void mutateDiseases(){
		LinkedList<Pathogen> crossOverPool = new LinkedList<Pathogen>();
		for(Pathogen disease: this.diseases){
			double rand = Math.random();
			if(rand<disease.getMutationRate()){
				disease.setGeneticCode(mutate(disease.getGeneticCode()));
			}
			rand = Math.random();
			if(rand<disease.getCrossoverRate()){
				crossOverPool.add(disease);
			}
		}
		this.crossover(crossOverPool);
	}
	private String mutate(String code){
		int rand = (int) (Math.random()*code.length());
		char[] codeArray = code.toCharArray();
		if(codeArray[rand]=='1') codeArray[rand]='0';
		else codeArray[rand]='1';
		String mutant = new String (codeArray);
		return mutant;
	}
	
	private void crossover(LinkedList<Pathogen> chroms){
		Collections.shuffle(chroms);
		if(chroms.size()%2==1) chroms.pop();
		while(!chroms.isEmpty()){
			char[] chrom1 = chroms.get(0).getGeneticCode().toCharArray();
			char[] chrom2 = chroms.get(1).getGeneticCode().toCharArray();
			int rand = (int) (Math.random()*chrom1.length);
			for(int i=0; i<rand; i++){
				char holder = chrom1[i];
				chrom1[i]=chrom2[i];
				chrom2[i] = holder;
			}
			chroms.get(0).setGeneticCode(new String(chrom1));
			chroms.get(1).setGeneticCode(new String(chrom2));
			chroms.pop();
			chroms.pop();
		}
	}
	/*
		public ArrayList<Pathogen> evolvePathogen(double mutationRate, double crossoverRate){
		//determine number of pathogens
		ArrayList<Pathogen> bugList = consolodateDiseases();
		int size = bugList.size();
		ArrayList<Pathogen> tempBugList = new ArrayList<Pathogen>();
		Random rand = new Random();
		//roulette wheel
		int last = 0;
		ArrayList<Integer> intList = new ArrayList<Integer>();
		int x,y,z;
		x = 0;*/
		/*each Pathogen has a number range corresponding to their fitness level,
		 * which is relative to the general population. */
		/*for(Pathogen d: bugList){
			intList.set(x, (int) (d.getFitness(diseases)*100 + last));
			last = intList.get(x);
			x++;
		}

		Pathogen first = null;
		Pathogen second = null;
		int random;
		int intListSize = intList.size();
		boolean check = false;
		for(x = 0; x<size; x++){
			random = rand.nextInt(intList.size());
			//if there is only one Pathogen, then don't crossover
			if(bugList.size()==1){
				tempBugList.add(mutate(bugList.get(x), mutationRate, rand));
				break;
			//if we don't reach the crossoverRate threshhold, don't crossover
			}else if(random<(crossoverRate*100)){
				for(y = 0; y<size; y++){
					if (random<=intList.get(y)){
						tempBugList.add(mutate(bugList.get(y),mutationRate, rand));
					}
				}*/
			/*generate a random number in the range 0-100, and find which range it 
			 * falls in. The Pathogen corresponding to that range gets selected as a parent*/
			/*}else{
				random = rand.nextInt(intListSize);
				//get first parent
				for(y = 0; y<size; y++){
					if (random<=intList.get(y)){
						first = bugList.get(y);
						break;
					}
				}
				//get second parent
				while(check == false){
					random = rand.nextInt(intListSize);
					for(z = 0; z<size; z++){
						if (random<=intList.get(z) && bugList.get(z)!=first){
							second = bugList.get(z);
							check = true;
							break;
						}
					}
				}
				//crossover point
				random = rand.nextInt(32)+1;
				String parent1 = first.getGeneticCode());
				String parent2 = Integer.toBinaryString(second.getGeneticCode());
				int length = parent1.length();
				while(length<32){
					parent1 = "0"+parent1;
				}
				length = parent2.length();
				while(length<32){
					parent2 = "0"+parent2;
				}
				String child = parent1.substring(0, random) + parent2.substring(random);
				//if desired, mutate here
				//tempBugList.add(new Pathogen(child,2));
			}	
		}
		//update the Pathogen list
		diseases = tempBugList;
		//reset treatment stats for all Pathogens. This isn't necessary
		// unless we add field data for pathogens so i removed it
		System.out.println("its xyan fault: "+tempBugList.size()  );
		return tempBugList;

	}
	private Pathogen mutate(Pathogen bug, double mutationRate, Random rand){
		int num = rand.nextInt(101);
		if(num<=mutationRate*100){
			String bugString = Integer.toBinaryString(bug.getGeneticCode());
			int length = bugString.length();
			while(length<32){
				bugString = "0"+bugString;
				length++;
			}
			num = rand.nextInt(32);
			//split string
			String first = bugString.substring(0,num);
			String second = bugString.substring(num+1);
			if (bugString.charAt(num)=='1'){
				bugString = first+'0'+second;
			}else{
				bugString = first+'1'+second;
			}
			//if desired, mutate here
			return new Pathogen(Integer.parseInt(bugString,2));
		}
		return bug;
	}*/

}
