package solver.genetic_algorithm;

import optimizer.Problem;

public class Chromosome {
	private boolean[] genes;
	
	public Chromosome(int length) {
		this.genes = new boolean[length];
		randomizeGenes();
	}
	
	public Chromosome(boolean[] genes) {
		this.genes = genes;
	}
	
	public void flipGene(int index) {
		this.genes[index] = !this.genes[index];
	}
	
	public int evaluate(Problem problem) {
		// TODO
		return 0;
	}
	
	private void randomizeGenes() {
		// TODO
	}
}
