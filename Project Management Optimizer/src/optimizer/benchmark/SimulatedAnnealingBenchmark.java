package optimizer.benchmark;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.solver.simulated_annealing.Algorithm;

public class SimulatedAnnealingBenchmark extends Benchmark {
	private int numCicles;
	private double coolingRate;
	private double initialTemperature;
	public SimulatedAnnealingBenchmark(int numCicles, double coolingRate, double initialTemperature) {
		this.numCicles = numCicles;
		this.coolingRate = coolingRate;
		this.initialTemperature = initialTemperature;
	}
	@Override
	public void run() {
		Problem problem = Optimizer.generateRandomProblem();
		Algorithm algorithm = new Algorithm(problem, this.coolingRate, this.initialTemperature, this.numCicles);
		algorithm.run();
		this.score = (long) algorithm.getBestAll();
	}
}
