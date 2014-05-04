package alife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;

public class Doctor extends Actor{

	private LinkedList<Human> appointments = new LinkedList<Human>();
	private ArrayList<Drug> drugPool;
	int patientsTreated = 0;
	boolean free;

	public boolean isFree(){
		return this.free;
	}
	
	public void free(){
		this.free = true;
	}
	public Doctor(ArrayList<Drug> drugPool){
		this.drugPool = drugPool;
		//this.free = true;
	}
	public boolean treat(Human patient, Drug treatment){
		boolean effective = patient.takeDrug(treatment);
		treatment.update(effective);
		return effective;
	}
	/*will use one of the treatment methods below in here*/
	public void treat(Human patient){
		treatAll(patient);
		this.free = true;
	}
	//treats patients with all the drugs
	public void treatAll(Human patient){
		for (Drug drug: drugPool){
			treat(patient, drug);
		}
	}
	//trests patients with a random drug
	public void treatBlind(Human patient){
		int index =(int) (Math.random()*(this.drugPool.size()));
		this.treat(patient, this.drugPool.get(index));
	}
	//treats patients with the overallbest drug
	public void treatBest(Human patient){
		HashMap<Drug,Integer> countMap = new HashMap<Drug,Integer>();
		for (Pathogen disease: patient.getDiseases()){
			Drug best = null;
			int most = -1;
			for (Drug drug: drugPool){
				int result = drug.getGeneticCode()^disease.getGeneticCode();
				String resultString = Integer.toBinaryString(result);
				int count = 0; 
				for(char c: resultString.toCharArray()){
					if(c == '1'){
						count++;
					}
				}
				if(count>most){
					best = drug;
					most = count;
				}
			}

			if(countMap.containsKey(best)){
				countMap.put(best,countMap.get(best)+1);
			}
			else{
				countMap.put(best,0);
			}
		}
		int largest = -1;
		Drug best = null;
		for(Drug drug:countMap.keySet()){
			if(countMap.get(drug)>largest){
				best =drug;
			}
		}
		this.treat(patient, best);
	}

	//treats patients with the best drug for each disease
	public void treatBestAll(Human patient){
		for (Pathogen disease: patient.getDiseases()){
			Drug best = null;
			int most = -1;
			for (Drug drug: drugPool){
				int result = drug.getGeneticCode()^disease.getGeneticCode();
				String resultString = Integer.toBinaryString(result);
				int count = 0; 
				for(char c: resultString.toCharArray()){
					if(c == '1'){
						count++;
					}
				}
				if(count>most){
					best = drug;
					most = count;
				}
			}
			this.treat(patient, best);
		}
	}




/*number to be treated before updating will have to be changed in here*/
	 public void act()
	    {
		 	//this is the variable controlling # to be treated
		 	int cycleSize = 20;
		 	if (appointments.size()>0){
			 	if(free){
			 		Human currentPatient = appointments.pop();
				 	currentPatient.callForVisit();
				 	this.free = false;
			 	
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
			 	patientsTreated++;
			 	if (patientsTreated%cycleSize == 0){
			 		drugPool = Drug.evolveDrugs(drugPool, Drug.getMutationRate(), Drug.getCrossoverRate());
			 		
			 	}
		 	}
		 }
	 }
	 public void makeAppointment(Human patient){
		 this.appointments.add(patient);
	 }

}
