package optimizer.solver.genetic_algorithm;

public class Config {
	public int populationSize;
	public double mutationRate;
	public double crossoverRate;
	public int elitism;
	public Config() {}
	public Config(int populationSize, float crossoverRate, float mutationRate, int elitism) {
		this.populationSize = populationSize;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		this.elitism = elitism;
	}
}
