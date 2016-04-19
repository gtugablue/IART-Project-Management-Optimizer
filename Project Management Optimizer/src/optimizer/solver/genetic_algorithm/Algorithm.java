package optimizer.solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import optimizer.Problem;

public class Algorithm {
	private Problem problem;
	private double mutationRate;
	private double crossoverRate;
	private int elitism;
	private static Random random = new Random();
	
	public Algorithm(Problem problem, double mutationRate, double crossoverRate, int elitism) {
		this.problem = problem;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitism = elitism;
	}

	public Population randomStartingPopulation(int size) {
		return new Population(size, problem);
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public int getElitism() {
		return elitism;
	}

	/**
	 * Create the next generation of the population
	 * 
	 * @return Population
	 */
	public Population evolve(Population population) {
		Population p = (Population) population.clone();
		p.evaluate(problem);

		selection(p);
		// TODO Crossover
		mutation(p);

		return p;
	}

	private void selection(Population population) {
		Chromosome[] selected = rouletteWheelSelection(population);
		for (int i = 0; i < selected.length; i++) {
			population.setChromosome(i, selected[i].clone());
		}
	}

	private Chromosome[] rouletteWheelSelection(Population population) {
		int amountToSelect = population.getSize() - this.elitism;
		Chromosome[] selected = new Chromosome[amountToSelect];
		for (int i = 0; i < amountToSelect; i++) {
			double d = random.nextDouble() * population.getTotalFitness();	
			for(int j = 0; j < population.getSize(); j++) {		
				d -= population.getChromosome(j).getFitness();		
				if (d <= 0)
				{
					selected[i] = population.getChromosome(j);
					break;
				}
			}
			if (selected[i] == null)
				selected[i] = population.getChromosome(population.getSize() - 1); // in case of rounding errors
		}
		return selected;
	}
	
	/**
	 * Crossover between a chromosome's member in a certain task
	 * @param firstCh
	 * @param secondCh
	 * @param task
	 * @param member
	 * @return ArrayList<Chromosome>
	 */
	private ArrayList<Chromosome> crossoverElement(Chromosome firstCh, Chromosome secondCh, int task, int member){
		
		ArrayList<Chromosome> chromosome = new ArrayList<Chromosome>();
		
		int nrMembers = problem.getElements().size();
		int block = firstCh.getNumBitsTask()*nrMembers;
		int blockIndex = block*task;
		int geneToFlip = blockIndex + firstCh.getNumBitsTask() + member;
		
		boolean gene;
		gene = firstCh.getGenes()[geneToFlip];
		firstCh.getGenes()[geneToFlip] = secondCh.getGenes()[geneToFlip];
		secondCh.getGenes()[geneToFlip] = gene;
		
		chromosome.add(firstCh);
		chromosome.add(secondCh);
		
		return chromosome;
		
	}

	private void mutation(Population population) {
		int chromosomeSize = population.getChromosomeSize();
		int totalBits = (population.getSize() - this.elitism) * chromosomeSize;
		for (int i = 0; i < totalBits; i++)
		{
			float f = random.nextFloat();
			if (f < mutationRate) {
				population.getChromosome(i / chromosomeSize).flipGene(i % chromosomeSize);
			}
		}
	}
}
