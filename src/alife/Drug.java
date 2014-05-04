package alife;

import java.util.ArrayList;
import java.util.Random;

public class Drug {
	private Integer geneticCode;
	//doctor must reset total cured each round after calling evolve on drug
	//static double numCuredThisRound;
	/*these total values refer to the grand total over time,
	 * they are not reset.*/
	private double totalCured;
	private double totalTreated;
	/*these values are reset after each evolution*/
	private double numberTreated;
	private double numberCured;
	private static double mutationRate = .05;
	private static double crossoverRate = .5;
	public Drug(Integer gene){
		this.geneticCode = gene;
		this.numberTreated = 0;
		this.numberCured = 0;
	}

	public Integer getGeneticCode(){
		return this.geneticCode;
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
		this.totalTreated++;
		if(effective){
			this.numberCured++;
			this.totalCured++;
		}
	}
	/*used only in evolveDrugs*/
	public void reset(){
		this.numberTreated = 0;
		this.numberCured = 0;
	}
	public static ArrayList<Drug> evolveDrugs(ArrayList<Drug> drugList, double mutationRate, double crossoverRate){
		int size = drugList.size();
		ArrayList<Drug> tempDrugList = new ArrayList<Drug>();
		Random rand = new Random();
		//roulette wheel
		int last = 0;
		ArrayList<Integer> intList = new ArrayList<Integer>();
		int x,y,z;
		x = 0;
		/*each drug has a number range corresponding to their fitness level,
		 * which is relative to the general population. */
		for(Drug d: drugList){
			intList.set(x, (int) (d.getFitness()*100 + last));
			last = intList.get(x);
			x++;
		}

		Drug first = null;
		Drug second = null;
		int random;
		int intListSize = intList.size();
		boolean check = false;
		for(x = 0; x<size; x++){
			random = rand.nextInt(intList.size());
			//if there is only one drug, then don't crossover
			if(drugList.size()==1){
				tempDrugList.add(mutate(drugList.get(x), mutationRate, rand));
				break;
			//if we don't reach the crossoverRate threshhold, don't crossover
			}else if(random<(crossoverRate*100)){
				for(y = 0; y<size; y++){
					if (random<=intList.get(y)){
						tempDrugList.add(mutate(drugList.get(y),mutationRate, rand));
					}
				}
			/*generate a random number in the range 0-100, and find which range it 
			 * falls in. The drug corresponding to that range gets selected as a parent*/
			}else{
				random = rand.nextInt(intListSize);
				//get first parent
				for(y = 0; y<size; y++){
					if (random<=intList.get(y)){
						first = drugList.get(y);
						break;
					}
				}
				//get second parent
				while(check == false){
					random = rand.nextInt(intListSize);
					for(z = 0; z<size; z++){
						if (random<=intList.get(z) && drugList.get(z)!=first){
							second = drugList.get(z);
							check = true;
							break;
						}
					}
				}
				//crossover point
				random = rand.nextInt(32)+1;
				String parent1 = Integer.toBinaryString(first.getGeneticCode());
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
				tempDrugList.add(new Drug(Integer.parseInt(child,2)));
			}	
		}
		//update the drug list
		EpidemicRunner.drugList = tempDrugList;
		//reset treatment stats for all drugs
		for(Drug drug: drugList){
			drug.reset();
		}
		return tempDrugList;

	}
	private static Drug mutate(Drug drug, double mutationRate, Random rand){
		int num = rand.nextInt(101);
		if(num<=mutationRate*100){
			String drugString = Integer.toBinaryString(drug.getGeneticCode());
			int length = drugString.length();
			while(length<32){
				drugString = "0"+drugString;
				length++;
			}
			num = rand.nextInt(32);
			//split string
			String first = drugString.substring(0,num);
			String second = drugString.substring(num+1);
			if (drugString.charAt(num)=='1'){
				drugString = first+'0'+second;
			}else{
				drugString = first+'1'+second;
			}
			//if desired, mutate here
			return new Drug(Integer.parseInt(drugString,2));
		}
		return drug;
	}

}
