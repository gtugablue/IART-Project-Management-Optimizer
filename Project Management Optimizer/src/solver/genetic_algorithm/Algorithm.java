package solver.genetic_algorithm;

import java.util.ArrayList;

public class Algorithm {

	private double mutationRate;
	private boolean elitism;

	public Algorithm(double mutationRate, boolean elitism) {
		this.mutationRate = mutationRate;
		this.elitism = elitism;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public boolean getElitism() {
		return elitism;
	}

	/**
	 * Create a new population with
	 * 
	 * @param oldPopulation
	 * @return Population
	 */
	private Population evolution(Population oldPopulation) {

		Chromosome fittest;

		if (elitism)
			fittest = oldPopulation.getFittest();
		else
			fittest = oldPopulation.getRandom();

		Population newPopulation = new Population();

		newPopulation.populate(fittest);

		return newPopulation;

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
