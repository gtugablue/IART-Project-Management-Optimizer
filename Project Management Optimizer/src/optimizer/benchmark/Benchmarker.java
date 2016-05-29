package optimizer.benchmark;

public class Benchmarker {
	public static void main(String[] args) {
		/*for (int populationSize = 15; populationSize <= 220; populationSize += 1) {
			Benchmark b = new GeneticAlgorithmBenchmark(100, populationSize, 0.3f, 0.015f, populationSize / 15);
			Score score = measureScore(b, 50, 3);
			//System.out.println("Population size: " + populationSize + "; Fitness: " + score.score + "; Time: " + score.time + "s");
			System.out.println(populationSize + "\t" + score.score + "\t" + score.time);
		}*/
		/*for (float crossoverRate = 0.2f; crossoverRate <= 1; crossoverRate += 0.025) {
			Benchmark b = new GeneticAlgorithmBenchmark(100, 50, crossoverRate, 0.015f, 5);
			Score score = measureScore(b, 50);
			//System.out.println("Crossover rate: " + crossoverRate + "; Fitness: " + score.score + "; Time: " + score.time + "s");
			System.out.println(crossoverRate + "\t" + score.score);
		}*/
		/*for (float mutationRate = 0.001f; mutationRate <= 0.01; mutationRate += 0.001) {
			Benchmark b = new GeneticAlgorithmBenchmark(1000, 50, 0.3f, mutationRate, 5);
			Score score = measureScore(b, 10);
			System.out.println("Mutation rate: " + mutationRate + "; Fitness: " + score.score + "; Time: " + score.time + "s");
		}*/
		/*for (int elitism = 0; elitism <= 25; elitism += 1) {
			Benchmark b = new GeneticAlgorithmBenchmark(100, 155, 0.3f, 0.015f, elitism);
			Score score = measureScore(b, 25, Double.MAX_VALUE);
			//System.out.println("Population size: " + populationSize + "; Fitness: " + score.score + "; Time: " + score.time + "s");
			System.out.println(elitism + "\t" + score.score + "\t" + score.time);
		}*/

		/*for (double coolingRate = 0.5; coolingRate < 1; coolingRate += 0.02) {
			Benchmark b = new SimulatedAnnealingBenchmark(5000, coolingRate, 100);
			Score score = measureScore(b, 1, Double.MAX_VALUE);
			//System.out.println("Population size: " + populationSize + "; Fitness: " + score.score + "; Time: " + score.time + "s");
			System.out.println(coolingRate + "\t" + score.score + "\t" + score.time);
		}*/
		/*for (double initialTemperature = 50; initialTemperature <= 1000; initialTemperature *= 1.02) {
			Benchmark b = new SimulatedAnnealingBenchmark(10000, 0.9994, 100);
			Score score = measureScore(b, 3, Double.MAX_VALUE);
			System.out.println(initialTemperature + "\t" + score.score + "\t" + score.time);
		}*/
		
		/*Benchmark b = new SimulatedAnnealingBenchmark(15000, 0.9997, 100);
		Score score = measureScore(b, 20, Double.MAX_VALUE);
		System.out.println(score.score + "\t" + score.time);
		*/
		Benchmark b = new GeneticAlgorithmBenchmark(4000, 155, 0.65f, 0.015f, 15);
		Score score = measureScore(b, 20, Double.MAX_VALUE);
		System.out.println(score.score + "\t" + score.time);
	}

	public static Score measureScore(Benchmark benchmark, int n, double maxTime) {
		long startTime = System.currentTimeMillis();
		long lMaxTime = (long)(maxTime * 1000);
		double score = 0;
		int i;
		for (i = 0; i < n; i++) {
			benchmark.run();
			if (System.currentTimeMillis() - startTime > lMaxTime)
				break;
			score += benchmark.getScore();
		}
		long endTime = System.currentTimeMillis();
		score = score / i;
		Score returnScore = new Score();
		returnScore.time = ((endTime - startTime) / n) * 0.001;
		returnScore.score = score;
		return returnScore;
	}
}
