package optimizer;
import java.util.List;

import optimizer.domain.Element;
import optimizer.domain.Skill;
import optimizer.domain.Task;

public class Problem {
	private List<Task> tasks;
	private List<Element> elements;
	private List<Skill> skills;
	
	public Problem(List<Task> tasks, List<Element> elements, List<Skill> skills) {
		this.tasks = tasks;
		this.elements = elements;
		this.skills = skills;
	}
	
	public List<Task> getTasks() {
		return this.tasks;
	}
}
