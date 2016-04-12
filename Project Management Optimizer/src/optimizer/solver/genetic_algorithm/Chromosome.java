package optimizer.solver.genetic_algorithm;

import java.util.Random;

import optimizer.Problem;
import optimizer.Solution;

public class Chromosome extends Solution implements Comparable<Chromosome>, Cloneable {
	private boolean[] genes;
	private int fitness;
	private int numBitsTaskID;
	public Chromosome(Problem problem) {
		super(problem);
		this.numBitsTaskID = minNumBits(problem.getTasks().size());
		this.genes = new boolean[calculateChromosomeSize()];
		randomizeGenes();
	}

	public Chromosome(Problem problem, boolean[] genes) {
		super(problem);
		this.genes = genes;
	}

	private static int minNumBits(int size) {
		if (size <= 1) return size;
		return (int) Math.floor(Math.log(size - 1)/Math.log(2) + 1);
	}
	
	public int calculateChromosomeSize() {
		return problem.getTasks().size() * (this.numBitsTaskID + problem.getElements().size());
	}
	
	public boolean[] getGenes() {
		return genes;
	}

	public void flipGene(int index) {
		this.genes[index] = !this.genes[index];
	}

	@Override
	public int evaluate() {
		return super.evaluate();
	}

	private void randomizeGenes() {
		Random r = new Random();
		for (int i = 0; i < genes.length; i++) {
			genes[i] = r.nextBoolean();
		}
	}

	public int getFitness() {
		return this.fitness;
	}
	
	public int getSize() {
		return this.genes.length;
	}

	@Override
	public int compareTo(Chromosome c) {
		return this.fitness - c.fitness;
	}
	
	@Override
	public Object clone() {
		boolean[] newGenes = this.genes.clone();
		Chromosome c = new Chromosome(problem, newGenes);
		return c;
	}
}
