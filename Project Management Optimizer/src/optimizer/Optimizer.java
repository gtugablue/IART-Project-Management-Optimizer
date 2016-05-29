package optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import optimizer.domain.Element;
import optimizer.domain.Skill;
import optimizer.domain.Task;
import optimizer.gui.GUI;
import optimizer.gui.GraphPanel;
import optimizer.solver.genetic_algorithm.Algorithm;
import optimizer.solver.genetic_algorithm.Population;

public class Optimizer {
	public static Random r = new Random(1);
	
	public static Population createStartingPopulation(Algorithm algorithm) {
		return algorithm.randomStartingPopulation();
	}
	
	public static Population evolvePopulation(Algorithm algorithm, Population population) {
		return algorithm.evolve(population);
	}
	
	public static Problem loadProblemFromJSON(String file) {
		return jsonParser.parse(file);
	}
	
	public static Problem generateRandomProblem() {
		ArrayList<Skill> skills = createSkills();
		ArrayList<Task> tasks = createTasks(skills);
		ArrayList<Element> elements = createElements();
		giveSkillsToElements(skills, elements);
		
		Problem problem = new Problem(tasks, elements, skills);
		return problem;
	}
	
	private static ArrayList<Skill> createSkills() {
		ArrayList<Skill> skills = new ArrayList<Skill>();
		for (int i = 0; i < 8; i++) {
			skills.add(new Skill("random skill #" + i));
		}
		return skills;
	}

	private static ArrayList<Task> createTasks(List<Skill> skills) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < 25; i++) {
			ArrayList<Task> precedences = new ArrayList<Task>();
			if (tasks.size() > 3) {
				for (int j = 0; j < r.nextInt(3); j++) {
					int id = r.nextInt(tasks.size());
					if (!precedences.contains(tasks.get(id))) {
						precedences.add(tasks.get(id));
					}
				}
			}
			tasks.add(new Task("random task #" + i, r.nextInt(100) + 50, skills.get(r.nextInt(skills.size())), precedences));
		}
		return tasks;
	}

	private static ArrayList<Element> createElements() {
		ArrayList<Element> elements = new ArrayList<Element>();
		for (int i = 0; i < 10; i++) {
			elements.add(new Element("random element #" + i));
		}
		return elements;
	}

	private static void giveSkillsToElements(List<Skill> skills, List<Element> elements) {
		for (int i = 0; i < skills.size(); i++) {
			for (int j = 0; j < elements.size(); j++) {
				elements.get(r.nextInt(skills.size())).addSkill(skills.get(i), (float)Math.random());
			}
		}
	}
}
