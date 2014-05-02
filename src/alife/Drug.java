package alife;

public class Drug {
	private int geneticCode;
	private double numberTreated;
	private double numberCured;
	public Drug(short gene){
		this.geneticCode = gene;
		this.numberTreated = 0;
		this.numberCured = 0;
	}
	
	public int getGeneticCode(){
		return this.geneticCode;
	}
	
	public double getFitness(){
		return this.numberTreated*(this.numberCured/this.numberTreated);
	}
	
	public void update(boolean effective){
		this.numberTreated++;
		if(effective){
			this.numberCured++;
		}
	}
	

}
