package optimizer.benchmark;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Config;
import optimizer.solver.genetic_algorithm.Population;

public class GeneticAlgorithmBenchmark extends Benchmark {
	private int maxGenerations;
	private int populationSize;
	private float crossoverRate;
	private float mutationRate;
	private int elitism;
	public GeneticAlgorithmBenchmark(int maxGenerations, int populationSize, float crossoverRate, float mutationRate, int elitism) {
		this.maxGenerations = maxGenerations;
		this.populationSize = populationSize;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		this.elitism = elitism;
	}
	@Override
	public void run() {
		Problem problem = Optimizer.generateRandomProblem();
		Config config = new Config(populationSize, crossoverRate, mutationRate, elitism);
		Algorithm algorithm = new Algorithm(problem, config);
		Population p = algorithm.randomStartingPopulation();
		//this.score = p.getFittest().getFitness();
		this.score = p.getFittest().getTotalTime();
		for (int i = 0; i < maxGenerations; i++) {
			p = algorithm.evolve(p);
			//this.score = p.getFittest().getFitness();
			this.score = p.getFittest().getTotalTime();
		}
	}
}
