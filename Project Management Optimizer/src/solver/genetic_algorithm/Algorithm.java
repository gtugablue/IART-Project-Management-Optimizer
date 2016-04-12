package solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.Random;

import optimizer.Problem;

public class Algorithm {
	private Problem problem;
	private double mutationRate;
	private int elitism;
	private static Random random = new Random();
	public Algorithm(Problem problem, int populationSize, double mutationRate, int elitism) {
		this.problem = problem;
		this.mutationRate = mutationRate;
		this.elitism = elitism;
		//this.population = new Population(populationSize, (int) Math.floor(Math.log(problem.getTasks().size() - 1)/Math.log(2) + 1));
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
		for (int i = this.elitism; i < population.getSize(); i++) {
			population.setChromosome(i, rouletteWheelSelection(population));
		}
	}
	
	private Chromosome rouletteWheelSelection(Population population) {
		double d = Algorithm.random.nextDouble() * population.getTotalFitness();	
		for(int i = 0; i < population.getSize(); i++) {		
			d -= population.getChromosome(i).getFitness();		
			if (d <= 0)
				return population.getChromosome(i);
		}
		return population.getChromosome(population.getSize() - 1); // in case of rounding errors
	}

	/**
	 * Crossover of two chromosomes
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
	
	private void mutation(Population population) {
		int chromosomeSize = population.getChromosomeSize();
		int totalBits = population.getSize() * chromosomeSize;
		for (int i = 0; i < totalBits; i++)
		{
			double d = random.nextDouble();
			if (d < mutationRate) {
				population.getChromosome(i / chromosomeSize).flipGene(i % chromosomeSize);
			}
		}
	}

}
