package optimizer.solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
		p.increaseNum();
		return p;
	}

	/**
	 * Select the genes to be crossed over
	 * If the gene to flip is equal in both chromosomes then another gene is fliped
	 * @param p
	 */
	private void crossover(Population p) {

		int taskToCross = random.nextInt(problem.getTasks().size() - 1) + 1;
		int chromosome1 = random.nextInt(p.getSize()-1) + 1;
		int chromosome2 = random.nextInt(p.getSize()-1) + 1;
		
		while(chromosome2 == chromosome1)
			chromosome2 = random.nextInt(p.getSize()-1) + 1;
		
		int member = random.nextInt(problem.getElements().size());
		
		int bit = (problem.getElements().size() + p.getChromosome(chromosome1).getNumBitsTaskID())*(taskToCross - 1) 
				+ (p.getChromosome(chromosome1).getNumBitsTaskID() - 1) + member + 1;
		
		int nrMembers = problem.getElements().size();
		int counter = 0;
		
		while(p.getChromosome(chromosome1).getGenes()[bit] == p.getChromosome(chromosome2).getGenes()[bit] && counter < nrMembers){
			member = random.nextInt(problem.getElements().size());
			counter++;
		}
		
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
		int block = firstCh.getNumBitsTaskID() + nrMembers;
		int blockIndex = block * (task-1);
		int geneToFlip = blockIndex + (firstCh.getNumBitsTaskID() - 1) + member + 1;
		
		boolean gene;
		gene = firstCh.getGenes()[geneToFlip];
		firstCh.getGenes()[geneToFlip] = secondCh.getGenes()[geneToFlip];
		secondCh.getGenes()[geneToFlip] = gene;

		chromosome.add(firstCh);
		chromosome.add(secondCh);

		return chromosome;

	}

	private void mutation(Population population) {
		if (random.nextBoolean())
			classicMutation(population);
		else
			swapMutation(population);
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
		int numChromosomes = population.getSize() - this.elitism;
		for (int i = 0; i < numChromosomes; i++) {
			Chromosome c = population.getChromosome(i);
			for (int j = 0; j < problem.getTasks().size(); j++) {
				float f = random.nextFloat();
				if (f < mutationRate) {
					// Temporarily store the first task
					boolean[] temp = Arrays.copyOfRange(c.getGenes(), j * c.getNumBitsTaskBlock(), (j + 1) * c.getNumBitsTaskBlock());
					
					int swapWith = random.nextInt(problem.getTasks().size());
					
					// Copy the second task to the first one
					System.arraycopy(c.getGenes(), swapWith * c.getNumBitsTaskBlock(), c.getGenes(), j * c.getNumBitsTaskBlock(), c.getNumBitsTaskBlock());
					
					// Copy the temporarily stored task back to the second one
					System.arraycopy(temp, 0, c.getGenes(), swapWith * c.getNumBitsTaskBlock(), c.getNumBitsTaskBlock());
				}
			}
		}
	}
}
