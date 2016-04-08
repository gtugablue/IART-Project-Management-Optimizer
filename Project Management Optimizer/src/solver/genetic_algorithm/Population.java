package solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.Random;

public class Population {
	
	private ArrayList<Chromosome> chromosomes;
	
	/**
	 * Population Constructor
	 * Creates random chromosomes and adds them to the arraylist chromosomes
	 * @param populationSize
	 * @param chromosomeLength
	 */
	public Population(int populationSize, int chromosomeLength){
		
		chromosomes = new ArrayList<Chromosome>();
		
		for(int i = 0; i < populationSize; i++){
			
			Chromosome oneChromosome = new Chromosome(chromosomeLength);
			chromosomes.add(oneChromosome);
			
		}
		
	}
	
	/**
	 * Population secondary constructor
	 * Creates an empty population
	 * @return
	 */
	public Population(){
		chromosomes = new ArrayList<Chromosome>();
	}
	
	public ArrayList<Chromosome> getPopulation(){
		return chromosomes;
	}
	
	public int getPopulationSize(){
		return chromosomes.size();
	}
	
	/**
	 * Method to find the population's chromosome with the highest fitness
	 * @return Chromosome
	 */
	public Chromosome getFittest(){
		
		int maxFit = 0;
		int index = 0;
		
		for(int i = 0; i < chromosomes.size(); i++){
			if(chromosomes.get(i).getFitness() > maxFit){
				maxFit = chromosomes.get(i).getFitness();
				index = i;
			}
		}
		
		return chromosomes.get(index);
		
	}
	
	public void populate(Chromosome chromosome){
		chromosomes.add(chromosome);
	}
	
	public Chromosome getRandom(){
			
		int random = generateRandom();
		
		return chromosomes.get(random);

	}
	
	public int generateRandom(){
		Random rand = new Random();
		
		return rand.nextInt(chromosomes.size() + 1);
	}
	

}
