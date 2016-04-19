package optimizer.solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import optimizer.Problem;
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
		evaluate();
	}

	public Chromosome(Problem problem, boolean[] genes) {
		super(problem);
		this.numBitsTaskID = minNumBits(problem.getTasks().size());
		this.genes = genes;
		evaluate();
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

		// Add missing tasks ordered by ID
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
		int fullOffset = offset + this.numBitsTaskID;
		int limit = fullOffset + problem.getElements().size();
		for (int j = fullOffset; j < limit; j++) {
			if (this.genes[j])
				elements.add(j - fullOffset);
		}
		return elements;
	}

	private int booleanArrToInt(boolean[] booleans) {
		int n = 0;
		for (boolean b : booleans)
			n = (n << 1) | (b ? 1 : 0);
		return n;
	}
	
	private boolean[] intToBooleanArr(int i, int numBits) {
		boolean[] b = new boolean[numBits];
		for (int j = b.length - 1; j >= 0; j--) {
	        b[j] = (j & (1 << j)) != 0;
	    }
		return b;
	}

	private void randomizeGenes() {
		Random r = new Random();
		List<Integer> topoSort = randomTopoSort();
		int taskSize = this.numBitsTaskID + problem.getElements().size();
		for (int i = 0; i < topoSort.size(); i++) {
			int n = topoSort.get(i);
			boolean[] taskID = intToBooleanArr(n, this.numBitsTaskID);
			for (int j = 0; j < this.numBitsTaskID; j++) {
				genes[i * taskSize + j] = taskID[j];
			}
			for (int j = this.numBitsTaskID; j < taskSize; j++) {
				genes[i * taskSize + j] = r.nextBoolean();
			}
		}
	}

	private List<Integer> randomTopoSort() {
		LinkedList<Integer> result = new LinkedList<Integer>();
		List<Task> tasks = problem.getTasks();

		LinkedList<Integer> sources = new LinkedList<Integer>(); 
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getPrecedences().size() == 0) {
				sources.add(i);
			}
		}
		int[] inDegree = new int[tasks.size()];
		for (int i = 0; i < tasks.size(); i++) {
			inDegree[i] = tasks.get(i).getPrecedences().size();
		}
		while( !sources.isEmpty() ) {
			Collections.shuffle(sources);
			int n = sources.poll();
			result.add(n);

			for(int i = 0; i < tasks.size(); i++) {
				if (!tasks.get(i).getPrecedences().contains(tasks.get(n)))
					continue;
				inDegree[i]--;
				if(inDegree[i] == 0) sources.add(i);
			}
		}

		return result;
	}

	public int getFitness() {
		return super.score;
	}

	public int getSize() {
		return this.genes.length;
	}

	@Override
	public int compareTo(Chromosome c) {
		return c.score - this.score;
	}

	public Chromosome(Chromosome c) {
		super(c);
		boolean[] newGenes = c.genes.clone();
		problem = c.problem;
		genes = newGenes;
	}

	@Override
	public Chromosome clone() {
		return new Chromosome(this);
	}
}
