package optimizer;

import java.util.Arrays;
import java.util.List;

import optimizer.domain.Element;
import optimizer.domain.Task;

public class Solution {
	protected Problem problem;
	protected List<Task> taskOrder;
	protected List<List<Element>> taskElements;
	protected int score;
	public Solution(Problem problem) {
		this.problem = problem;
	}

	public int evaluate() {
		int[] taskCompletionTimes = new int[problem.getTasks().size()];
		Arrays.fill(taskCompletionTimes, Integer.MAX_VALUE);
		int[] elementReadyTimes = new int[problem.getElements().size()];
		Arrays.fill(elementReadyTimes, 0);
		// TODO
		//System.out.println(taskElements.get(0).size());
		if (taskOrder.size() > 0 && taskElements.get(0).size() > 0)
			this.score = calculateTaskStartTime(taskOrder.get(0), taskElements.get(0), taskCompletionTimes, elementReadyTimes);
		else
			this.score = 1;
		return score;
	}
	
	private int calculateTaskStartTime(Task task, List<Element> elements, int[] taskCompletionTimes, int[] elementReadyTimes) {
		List<Task> precedences = task.getPrecedences();
		int maxPrecedenceTime = 0;
		for (int i = 0; i < problem.getTasks().size(); i++) {
			Task t = problem.getTasks().get(i);
			if (precedences.contains(t)) {
				if (taskCompletionTimes[i] > maxPrecedenceTime)
					maxPrecedenceTime = taskCompletionTimes[i];
			}
		}
		int minElementReadyTime = Integer.MAX_VALUE;
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getSkillPerfomance(task.getSkill()) <= 0)
				continue;
			int elementID = elementToID(elements.get(i));
			if (elementReadyTimes[elementID] < minElementReadyTime) {
				minElementReadyTime = elementReadyTimes[elementID];
			}
		}
		return Math.max(maxPrecedenceTime, minElementReadyTime);
	}
	
	private int elementToID(Element element) {
		for (int i = 0; i < problem.getElements().size(); i++) {
			if (problem.getElements().get(i).equals(element))
				return i;
		}
		return -1;
	}
}
