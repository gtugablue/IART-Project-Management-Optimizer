package optimizer;

import java.util.ArrayList;

import optimizer.domain.Element;
import optimizer.domain.Skill;
import optimizer.domain.Task;
import solver.genetic_algorithm.Algorithm;
import solver.genetic_algorithm.Population;

public class ProjectManagementOptimizer {
	public static void main(String[] args) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		ArrayList<Element> elements = new ArrayList<Element>();
		ArrayList<Skill> skills = new ArrayList<Skill>();
		Problem problem = new Problem(tasks, elements, skills);
		Algorithm algorithm = new Algorithm(problem, 50, 0.0001, 3);
		Population population = new Population(50, 30);
		population.evaluate(problem);
		for (int i = 0; i < 1000; i++) {
			System.out.print("Max fitness: " + population.getFittest().getFitness() + "\t Total fitness: " + population.getTotalFitness() + " - ");
			for (int j = 0; j < population.getSize(); j++) {
				System.out.print(population.getChromosome(j).getFitness() + " ");
			}
			System.out.println();
			population = algorithm.evolve(population);
		}
	}
}