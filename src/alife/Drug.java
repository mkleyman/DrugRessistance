package alife;

import java.util.ArrayList;
import java.util.Random;

public class Drug {
	private String geneticCode;
	//doctor must reset total cured each round after calling evolve on drug
	//static double numCuredThisRound;
	/*these total values refer to the grand total over time,
	 * they are not reset.*/
	private double totalCured;
	private double totalTreated;
	/*these values are reset after each evolution*/
	private double numberTreated;
	private double numberCured;
	private static double mutationRate = .55;
	private static double crossoverRate = .5;
	public Drug(String gene){
		this.geneticCode = gene;
		this.numberTreated = 0;
		this.numberCured = 0;
	}
	
	public void setGeneticCode(String code){
		this.geneticCode = code;
	}
	public String getGeneticCode(){
		return this.geneticCode;
	}
	
	public void resetFitness(){
		this.numberTreated = 0;
		this.numberCured = 0;
	}
	/*This is just equal to numberCured. I believe the intent was to have
	 * this represent what percentage of all successful treatments were
	 * the result of this drug, but I don't think that will work the way
	 * that things are currently set up. I think the methods I wrote are 
	 * the only ones to use this though, so we're okay - x*/
	public double getFitness(){
	//	return this.numberTreated*(this.numberCured/this.numberTreated);
		return (this.numberCured/this.numberTreated);
	}
	public static Double getMutationRate(){
		return mutationRate;
	}
	public static Double getCrossoverRate(){
		return crossoverRate;
	}
	public void update(boolean effective){
		this.numberTreated++;
		//this.totalTreated++;
		if(effective){
			this.numberCured++;
			//this.totalCured++;
		}
	}
}
	


