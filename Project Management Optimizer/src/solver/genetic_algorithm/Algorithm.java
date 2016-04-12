package solver.genetic_algorithm;

import java.util.ArrayList;

import optimizer.Problem;

public class Algorithm {
	private Problem problem;
	private double mutationRate;
	private int elitism;
	private Population population;
	public Algorithm(Problem problem, int populationSize, double mutationRate, int elitism) {
		this.problem = problem;
		this.mutationRate = mutationRate;
		this.elitism = elitism;
		this.population = new Population(populationSize, (int) Math.floor(Math.log(problem.getTasks().size() - 1)/Math.log(2) + 1));
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
	 * @param oldPopulation
	 * @return Population
	 */
	private Population evolve(Population oldPopulation) {
		return oldPopulation.evolve(this.problem);
	}

	/**
	 * Crosover of two chromosomes
	 * 
	 * @param firstCh
	 * @param secondCh
	 * @param initialBit
	 * @param finalBit
	 * @return ArrayList<Chromosome>
	 */
	private ArrayList<Chromosome> crossover(Chromosome firstCh, Chromosome secondCh, int initialBit, int finalBit) {

		ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

		for (int i = initialBit; i < finalBit; i++) {
			firstCh.getGenes()[i] = secondCh.getGenes()[i];
			secondCh.getGenes()[i] = firstCh.getGenes()[i];
		}

		chromosomes.add(firstCh);
		chromosomes.add(secondCh);

		return chromosomes;

	}

	/**
	 * Mutate chromosome in the desired distance
	 * 
	 * @param initialCh
	 * @param initialBit
	 * @param finalBit
	 * @return Chromosome
	 */
	private Chromosome mutation(Chromosome initialCh, int mutationBit) {

		initialCh.flipGene(mutationBit + 1);

		return initialCh;

	}

}
