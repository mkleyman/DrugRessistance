package alife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import info.gridworld.actor.Actor;

public class Tracker extends Actor {
	
	private ArrayList<Drug> drugList = new ArrayList<Drug>();
	private Drug currentTreatment;
	private ArrayList<Human> humanList = new ArrayList<Human>();
	private ConcurrentHashMap<String,Integer> diseaseCountMap;
	private int stepCount = 0;
	private int cycleLength  = 20;
	
	private void createDrug(){
		int max = -1;
		String most = null;
		if(!this.diseaseCountMap.isEmpty()){
			for(String code: this.diseaseCountMap.keySet()){
				if(this.diseaseCountMap.get(code) > max){
					max = this.diseaseCountMap.get(code);
					most = code;
					
				}
			}
			String best = Doctor.xor(most,"11111111111111111111111111111111");
			this.currentTreatment.setGeneticCode(best);
			this.currentTreatment.resetFitness();
			this.diseaseCountMap.clear();
		}
	}
	public Tracker(ArrayList<Human> humans, Drug drug, ConcurrentHashMap<String,Integer> map ){
		//this.drugList = drugs;
		this.humanList = humans;
		this.currentTreatment = drug;
		this.diseaseCountMap = map;
	}
	public void act(){
		int sickCount  = 0;
		this.stepCount++;
		if (this.stepCount%this.cycleLength == 0){
	 		this.createDrug();
	 		//drugPool = Drug.evolveDrugs(drugPool, Drug.getMutationRate(), Drug.getCrossoverRate());
	 		
	 	}
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
		System.out.println(this.currentTreatment.getGeneticCode()+" : "+this.currentTreatment.getFitness());
	}
	
	

}
