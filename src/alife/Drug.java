package alife;

import java.util.ArrayList;
import java.util.Random;

public class Drug {
	private Integer geneticCode;
	private double numberTreated;
	private double numberCured;
	public Drug(Integer gene){
		this.geneticCode = gene;
		this.numberTreated = 0;
		this.numberCured = 0;
	}
	
	public Integer getGeneticCode(){
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
	public ArrayList<Drug> evolveDrugs(ArrayList<Drug> drugList, double mutationRate, double crossoverRate){
		int size = drugList.size();
		ArrayList<Drug> tempDrugList = new ArrayList<Drug>();
		Random rand = new Random();
		//roulette wheel
		int last = 0;
		int[] intArr = new int[size];
		int x,y,z;
		x = 0;
		/*assign integer range 0<100 for each drug based on fitness*/
		for(Drug d: drugList){
			intArr[x] = (int) (d.getFitness()*100 + last);
			last = intArr[x];
		}
		
		Drug first = null;
		Drug second = null;
		int random;
		boolean check = false;
		for(x = 0; x<size; x++){
			random = rand.nextInt(101);
			//if there is only one drug, then don't crossover
			if(drugList.size()==1){
				tempDrugList.add(drugList.get(x));
			}else if(random<(crossoverRate*100)){
				for(y = 0; y<size; y++){
					if (random<=intArr[y]){
						tempDrugList.add(mutate(drugList.get(y),mutationRate, rand));
					}
				}
			/*generate a random number in the range 0-100, and find which range it 
			 * falls in. The drug corresponding to that range gets selected as a parent*/
			}else{
				random = rand.nextInt(101);
				//get first parent
				for(y = 0; y<size; y++){
					if (random<=intArr[y]){
						first = drugList.get(y);
						break;
					}
				}
				//get second parent
				while(check == false){
					random = rand.nextInt(101);
					for(z = 0; z<size; z++){
						if (random<=intArr[z] && drugList.get(z)!=first){
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
		return tempDrugList;
		
	}
	public Drug mutate(Drug drug, double mutationRate, Random rand){
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
