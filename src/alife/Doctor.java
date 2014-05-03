package alife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;

public class Doctor extends Actor{
	private int steps  = 0;
	private int sideLength = 6;
	private LinkedList<Human> appointments = new LinkedList<Human>();
	private ArrayList<Drug> drugPool;
	
	public boolean treat(Human patient, Drug treatment){
		boolean effective = patient.takeDrug(treatment);
		treatment.update(effective);
		return effective;
	}
	
	public void treat(Human patient){
		
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
