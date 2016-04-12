package solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import optimizer.Problem;

public class Population implements Cloneable{
	
	private List<Chromosome> chromosomes;
	
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
	
	public List<Chromosome> getPopulation(){
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
	
	public void sort() {
		Collections.sort(this.chromosomes);
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
	
	public Population evolve(Problem problem) {
		Population p = (Population) this.clone();
		return p;
	}
	
	@Override
	public Object clone() {
		Population p = new Population();
		for (int i = 0; i < this.chromosomes.size(); i++) {
			p.chromosomes.add((Chromosome) this.chromosomes.get(i).clone());
		}
		return p;
	}
}
