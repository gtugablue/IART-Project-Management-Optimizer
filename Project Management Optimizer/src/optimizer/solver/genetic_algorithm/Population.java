package optimizer.solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import optimizer.Problem;

public class Population implements Cloneable{
	private int num;
	private List<Chromosome> chromosomes;
	private int totalFitness;
	Random random = new Random();
	
	/**
	 * Population Constructor
	 * Creates random chromosomes and adds them to the arraylist chromosomes
	 * @param populationSize
	 * @param chromosomeLength
	 */
	public Population(int populationSize, Problem problem){
		this.num = 0;
		chromosomes = new ArrayList<Chromosome>();
		for(int i = 0; i < populationSize; i++){
			Chromosome oneChromosome = new Chromosome(problem);
			chromosomes.add(oneChromosome);
		}
		this.evaluate(problem);
	}
	
	/**
	 * Population secondary constructor
	 * Creates an empty population
	 * @return
	 */
	public Population(){
		this.num = 0;
		chromosomes = new ArrayList<Chromosome>();
	}
	
	public List<Chromosome> getPopulation(){
		return chromosomes;
	}
	
	public int getSize(){
		return chromosomes.size();
	}
	
	public int getChromosomeSize() {
		if (chromosomes.size() == 0) return 0;
		return chromosomes.get(0).getSize();
	}
	
	/**
	 * Method to find the population's chromosome with the highest fitness
	 * @return Chromosome
	 */
	public Chromosome getFittest(){
		int maxFit = Integer.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < chromosomes.size(); i++){
			if(chromosomes.get(i).getFitness() < maxFit){
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
	
	public void evaluate(Problem problem) {
		for (int i = 0; i < this.chromosomes.size(); i++) {
			this.chromosomes.get(i).evaluate();
		}
		this.totalFitness = 0;
		for (int i = 0; i < this.chromosomes.size(); i++) {
			this.totalFitness += this.chromosomes.get(i).getFitness();
		}
		Collections.sort(this.chromosomes);
	}
	
	public void setChromosome(int index, Chromosome c) {
		this.chromosomes.set(index, c);
	}
	
	public Chromosome getChromosome(int index) {
		return this.chromosomes.get(index);
	}
	
	public int getTotalFitness() {
		return totalFitness;
	}
	
	public void showInfo() {
		System.out.print("#" + this.num + "\t Best fitness: " + getFittest().getFitness() + "\t Avg. fitness: " + getTotalFitness() / getSize() + " - ");
		for (int j = 0; j < getSize(); j++) {
			System.out.print(getChromosome(j).getFitness() + " ");
		}
		System.out.println();
	}
	
	@Override
	public Population clone() {
		Population p = new Population();
		p.totalFitness = this.totalFitness;
		for (int i = 0; i < this.chromosomes.size(); i++) {
			p.chromosomes.add(this.chromosomes.get(i).clone());
		}
		p.num = this.num;
		return p;
	}
	
	public void increaseNum() {
		this.num++;
	}
	
	public int num() {
		return this.num;
	}
}
