package optimizer;

import java.util.ArrayList;

import optimizer.domain.Element;
import optimizer.domain.Skill;
import optimizer.domain.Task;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class ProjectManagementOptimizer {
	public static void main(String[] args) {
		ArrayList<Skill> skills = new ArrayList<Skill>();
		skills.add(new Skill("Escrever relatorios"));
		skills.add(new Skill("Fazer algoritmos"));
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task("Fazer o relatorio", 70, skills.get(0), new ArrayList<Task>()));
		tasks.add(new Task("Resolver o problema com algoritmos geneticos", 80, skills.get(1), new ArrayList<Task>()));
		tasks.add(new Task("Resolver o problema com arrefecimento simulado", 90, skills.get(1), new ArrayList<Task>()));
		
		ArrayList<Element> elements = new ArrayList<Element>();
		elements.add(new Element("Duarte Pinto"));
		elements.add(new Element("Filipa Ramos"));
		elements.add(new Element("Gustavo Silva"));
		
		elements.get(0).addSkill(skills.get(0), 0.5f);
		elements.get(0).addSkill(skills.get(1), 0.7f);
		
		elements.get(1).addSkill(skills.get(0), 1);
		
		elements.get(2).addSkill(skills.get(1), 1);
		
		Problem problem = new Problem(tasks, elements, skills);
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
