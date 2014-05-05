package alife;

import java.util.ArrayList;
import java.util.Random;

public class Pathogen {
	private String geneticCode;
	private static Random codeGen = new Random();
	private static Double mutationRate = .00001;
	private static Double crossoverRate = .1;
	public Pathogen(String gene){
		this.geneticCode = gene;
	}
	public Pathogen(){
		String code = "";
		for(int i =0; i<32;i++){
			double rand = Math.random();
			if(rand<.5) code+="1";
			else code+="0";
		}
		geneticCode = code;
	}
	public static Double getMutationRate(){
		return mutationRate;
	}
	public void setGeneticCode(String code){
		this.geneticCode = code;
	}
	public static Double getCrossoverRate(){
		return crossoverRate;
	}
	
	public double getFitness(ArrayList<Pathogen> list){
		int count = 0;
		for(Pathogen disease: list){
			if (this.geneticCode.equals(disease.geneticCode)){
				count++;
			}
		}
		return count/list.size();
	}

	
	
	public String getGeneticCode(){
		return this.geneticCode;
	}
	
	
}
