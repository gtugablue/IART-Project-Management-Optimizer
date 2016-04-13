package optimizer.solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import optimizer.Problem;
import optimizer.Solution;
import optimizer.domain.Element;
import optimizer.domain.Task;

public class Chromosome extends Solution implements Comparable<Chromosome>, Cloneable {
	private boolean[] genes;
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
		taskOrder = new ArrayList<Integer>();
		taskElements = new ArrayList<List<Integer>>();
		List<Task> tasks = problem.getTasks();
		int bitsPerTask = this.numBitsTaskID + problem.getElements().size();
		for (int i = 0; i < tasks.size(); i++) {
			int offset = i * bitsPerTask;
			boolean[] bTaskID = Arrays.copyOfRange(this.genes, offset, offset + this.numBitsTaskID);
			int taskID = booleanArrToInt(bTaskID);
			if (taskID >= problem.getTasks().size()) continue; // Ignore, because ID is invalid
			if (taskOrder.contains(taskID)) continue; // Ignore, because the task is already in the list
			taskOrder.add(taskID);
			taskElements.add(readElements(offset));
		}
		for (int i = 0; i < tasks.size(); i++) {
			if (!taskOrder.contains(i)) {
				taskOrder.add(i);
				taskElements.add(readElements(i * bitsPerTask));
			}
		}
		return super.evaluate();
	}
	
	private List<Integer> readElements(int offset) {
		ArrayList<Integer> elements = new ArrayList<Integer>();
		for (int j = 0; j < problem.getElements().size(); j++) {
			if (this.genes[offset + this.numBitsTaskID + j])
				elements.add(j);
		}
		return elements;
	}

	private int booleanArrToInt(boolean[] booleans) {
		int n = 0;
		for (boolean b : booleans)
			n = (n << 1) | (b ? 1 : 0);
		return n;
	}

	private void randomizeGenes() {
		Random r = new Random();
		for (int i = 0; i < genes.length; i++) {
			genes[i] = r.nextBoolean();
		}
	}

	public int getFitness() {
		return this.score;
	}

	public int getSize() {
		return this.genes.length;
	}

	@Override
	public int compareTo(Chromosome c) {
		return this.score - c.score;
	}

	@Override
	public Object clone() {
		boolean[] newGenes = this.genes.clone();
		Chromosome c = new Chromosome(problem, newGenes);
		return c;
	}
}
