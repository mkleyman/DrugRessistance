package alife;

import java.util.ArrayList;
import java.util.Random;

public class Pathogen {
	private Integer geneticCode;
	private static Random codeGen = new Random();
	
	public Pathogen(int gene){
		this.geneticCode = gene;
	}
	public Pathogen(){
		geneticCode = codeGen.nextInt();
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

	
	
	public Integer getGeneticCode(){
		return this.geneticCode;
	}
	
	
}
