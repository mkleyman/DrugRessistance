package alife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import info.gridworld.actor.Actor;

public class Tracker extends Actor {
	
	private ArrayList<Drug> drugList = new ArrayList<Drug>();
	private ArrayList<Human> humanList = new ArrayList<Human>();
	
	public Tracker(ArrayList<Drug> drugs,ArrayList<Human> humans ){
		this.drugList = drugs;
		this.humanList = humans;
	}
	public void act(){
		int sickCount  = 0;
		HashMap<String, Integer> diseaseCounter = new HashMap<String, Integer>();
		for(Human person: humanList){
			if(person.isSick()){
				sickCount++;
				for(Pathogen disease: person.getDiseases()){
					if(diseaseCounter.containsKey((disease.getGeneticCode()))){
						diseaseCounter.put((disease.getGeneticCode()), diseaseCounter.get((disease.getGeneticCode()))+1);
						//diseaseCounter.put(Integer.toBinaryString(disease.getGeneticCode()), 1);
					}
					else{
						diseaseCounter.put((disease.getGeneticCode()), 1);
					}
				}
			}
		}
		System.out.println(sickCount+" out of "+humanList.size()+" are currently sick" );
		System.out.println("Current frequency of diseases:");
		for(String code : diseaseCounter.keySet()){
			System.out.println(code+" : "+diseaseCounter.get(code));
		}
		System.out.println("Current fitness of treatments:");
		for(Drug drug : this.drugList){
			System.out.println(drug.getGeneticCode()+" : "+drug.getFitness());
		}
	}
	
	

}
