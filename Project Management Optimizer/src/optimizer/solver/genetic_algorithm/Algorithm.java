package optimizer.solver.genetic_algorithm;

import java.util.ArrayList;
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
		selection(p);
		crossover(p);
		mutation(p);

		p.evaluate(problem);
		return p;
	}

	/**
	 * Select the genes to be crossed over
	 * @param p
	 */
	private void crossover(Population p) {

		int taskToCross = random.nextInt(p.getChromosomeSize() - 1) + 1;
		int chromosome1 = random.nextInt(p.getSize()-1) + 1;
		int chromosome2 = random.nextInt(p.getSize()-1) + 1;
		
		while(chromosome2 == chromosome1)
			chromosome2 = random.nextInt(p.getSize()-1) + 1;
		
		int member = random.nextInt(p.getChromosomeSize() + p.getChromosome(chromosome1).getNumBitsTask()) + p.getChromosome(chromosome1).getNumBitsTask();
		
		crossoverElement(p.getChromosome(chromosome1),p.getChromosome(chromosome2), taskToCross, member);

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
		int maxValue = population.getChromosome(0).getFitness();
		for (int i = 0; i < amountToSelect; i++) {
			double d = random.nextDouble() * (population.getSize() * maxValue - population.getTotalFitness());
			for (int j = 0; j < population.getSize(); j++) {
				d -= maxValue - population.getChromosome(j).getFitness();
				if (d <= 0) {
					selected[i] = population.getChromosome(j);
					break;
				}
			}
			if (selected[i] == null)
				selected[i] = population.getChromosome(population.getSize() - 1); // in
																					// case
																					// of
																					// rounding
																					// errors
		}
		return selected;
	}

	/**
	 * Crossover between a chromosome's member in a certain task
	 * 
	 * @param firstCh
	 * @param secondCh
	 * @param task
	 * @param member
	 * @return ArrayList<Chromosome>
	 */
	private ArrayList<Chromosome> crossoverElement(Chromosome firstCh, Chromosome secondCh, int task, int member) {

		ArrayList<Chromosome> chromosome = new ArrayList<Chromosome>();

		int nrMembers = problem.getElements().size();
		int block = firstCh.getNumBitsTask() * nrMembers;
		int blockIndex = block * task;
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
		classicMutation(population);
	}

	private void classicMutation(Population population) {
		int chromosomeSize = population.getChromosomeSize();
		int totalBits = (population.getSize() - this.elitism) * chromosomeSize;
		for (int i = 0; i < totalBits; i++) {
			float f = random.nextFloat();
			if (f < mutationRate) {
				population.getChromosome(i / chromosomeSize).flipGene(i % chromosomeSize);
			}
		}
	}

	private void swapMutation(Population population) {
		int bitsPerTask = population.getChromosome(0).getNumBitsTask() + problem.getElements().size();
		int numChromosomes = population.getSize() - this.elitism;
		for (int i = 0; i < numChromosomes; i++) {
			float f = random.nextFloat();
			if (f < mutationRate) {
				// TODO
				i--;
			}
		}
	}
}
