package optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import optimizer.domain.Element;
import optimizer.domain.Skill;
import optimizer.domain.Task;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class ProjectManagementOptimizer {
	public static Random r = new Random(1);
	ArrayList<Skill> skills = new ArrayList<Skill>();
	ArrayList<Task> tasks = new ArrayList<Task>();
	ArrayList<Element> elements = new ArrayList<Element>();
	
	public ProjectManagementOptimizer(){}
	public static void main(String[] args) {		
		ProjectManagementOptimizer project = new ProjectManagementOptimizer();
		
		jsonParser parser = new jsonParser(args[0], project);
		parser.parser();
		
		project.skills = createSkills();
		project.tasks = createTasks(project.skills);
		project.elements = createElements();
		giveSkillsToElements(project.skills, project.elements);
		
		Problem problem = new Problem(project.tasks, project.elements, project.skills);
		Algorithm algorithm = new Algorithm(problem, 0.1, 0.02, 3);
		Population population = algorithm.randomStartingPopulation(50);
		population.evaluate(problem);
		
		for (int i = 0; i < 100000; i++) {
			System.out.print("#" + (i + 1) + "\t Best fitness: " + population.getFittest().getFitness() + "\t Avg. fitness: " + population.getTotalFitness() / population.getSize() + " - ");
			for (int j = 0; j < population.getSize(); j++) {
				System.out.print(population.getChromosome(j).getFitness() + " ");
			}
			System.out.println();
			population = algorithm.evolve(population);
		}
		
	}

	private static ArrayList<Skill> createSkills() {
		ArrayList<Skill> skills = new ArrayList<Skill>();
		/*skills.add(new Skill("Escrever relatorios"));
		skills.add(new Skill("Fazer algoritmos"));
		skills.add(new Skill("Derp"));
		skills.add(new Skill("Herp"));*/
		for (int i = 0; i < 10; i++) {
			skills.add(new Skill("random skill #" + i));
		}
		return skills;
	}

	private static ArrayList<Task> createTasks(List<Skill> skills) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		/*tasks.add(new Task("Fazer o relatorio", 70, skills.get(0), new ArrayList<Task>()));
		ArrayList<Task> precedences = new ArrayList<Task>();
		precedences.add(tasks.get(0));
		tasks.add(new Task("Resolver o problema com algoritmos geneticos", 80, skills.get(1), precedences));
		tasks.add(new Task("Resolver o problema com arrefecimento simulado", 90, skills.get(1), new ArrayList<Task>()));*/
		for (int i = 0; i < 100; i++) {
			ArrayList<Task> precedences = new ArrayList<Task>();
			if (tasks.size() > 3) {
				for (int j = 0; j < r.nextInt(3); j++) {
					int id = r.nextInt(tasks.size());
					if (!precedences.contains(tasks.get(id))) {
						precedences.add(tasks.get(id));
					}
				}
			}
			tasks.add(new Task("random task #" + i, r.nextInt(100) + 1, skills.get(r.nextInt(skills.size())), precedences));
		}
		return tasks;
	}

	private static ArrayList<Element> createElements() {
		ArrayList<Element> elements = new ArrayList<Element>();
		/*elements.add(new Element("Duarte Pinto"));
		elements.add(new Element("Filipa Ramos"));
		elements.add(new Element("Gustavo Silva"));*/
		for (int i = 0; i < 50; i++) {
			elements.add(new Element("random element #" + i));
		}
		return elements;
	}

	private static void giveSkillsToElements(List<Skill> skills, List<Element> elements) {
		/*elements.get(0).addSkill(skills.get(0), 0.5f);
		elements.get(0).addSkill(skills.get(1), 0.7f);

		elements.get(1).addSkill(skills.get(0), 1);

		elements.get(2).addSkill(skills.get(1), 1);*/
		for (int i = 0; i < skills.size(); i++) {
			for (int j = 0; j < elements.size(); j++) {
				elements.get(r.nextInt(skills.size())).addSkill(skills.get(i), (float)Math.random());
			}
		}
	}
}
