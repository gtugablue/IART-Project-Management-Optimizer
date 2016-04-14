package optimizer;

import java.util.ArrayList;

import optimizer.domain.Element;
import optimizer.domain.Skill;
import optimizer.domain.Task;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class ProjectManagementOptimizer {
	
	ArrayList<Skill> skills = new ArrayList<Skill>();
	ArrayList<Task> tasks = new ArrayList<Task>();
	ArrayList<Element> elements = new ArrayList<Element>();
	
	public ProjectManagementOptimizer(){}
	
	public static void main(String[] args) {
		
		ProjectManagementOptimizer project = new ProjectManagementOptimizer();
		
		jsonParser parser = new jsonParser(args[0], project);
		parser.parser();
		
		Problem problem = new Problem(project.tasks, project.elements, project.skills);
		Algorithm algorithm = new Algorithm(problem, 0.01, 0.4, 3);
		Population population = algorithm.randomStartingPopulation(50);
		population.evaluate(problem);
		
		for (int i = 0; i < 10; i++) {
			System.out.print("#" + (i + 1) + "\t Max fitness: " + population.getFittest().getFitness() + "\t Avg. fitness: " + population.getTotalFitness() / population.getSize() + " - ");
			for (int j = 0; j < population.getSize(); j++) {
				System.out.print(population.getChromosome(j).getFitness() + " ");
			}
			System.out.println();
			population = algorithm.evolve(population);
		}
		
	}
}
