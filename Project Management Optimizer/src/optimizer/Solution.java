package optimizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import optimizer.domain.Element;
import optimizer.domain.Task;

public class Solution {
	protected Problem problem;
	protected List<Integer> taskOrder;
	protected List<List<Integer>> taskElements;
	protected int score;
	public Solution(Problem problem) {
		this.problem = problem;
	}

	public int evaluate() {
		HashMap<Task, Integer> taskCompletionTimes = new HashMap<Task, Integer>();
		HashMap<Element, Integer> elementReadyTimes = new HashMap<Element, Integer>();
		createTimes(taskCompletionTimes, elementReadyTimes);
		int currTime = 0;

		while (!allTasksDone(taskCompletionTimes)) {
			for (int i = 0; i < taskOrder.size(); i++) {
				Task task = problem.getTasks().get(i);
				if (taskCompletionTimes.get(task).equals(Integer.MAX_VALUE)) continue; // Task already done, skip it
				if (!precedencesReady(task, currTime, taskCompletionTimes)) continue; // Precedences not ready, try a different task
			}
		}

		return score;
	}

	private boolean allTasksDone(HashMap<Task, Integer> taskCompletionTimes) {
		Iterator it = taskCompletionTimes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			if (pair.getValue().equals(Integer.MAX_VALUE))
				return false;
		}
		return true;
	}

	private void createTimes(HashMap<Task, Integer> taskCompletionTimes, HashMap<Element, Integer> elementReadyTimes) {
		for (Task task : problem.getTasks()) {
			taskCompletionTimes.put(task, Integer.MAX_VALUE);
		}
		for (Element element : problem.getElements()) {
			elementReadyTimes.put(element, 0);
		}
	}

	private boolean precedencesReady(Task task, int currTime, HashMap<Task, Integer> taskCompletionTimes) {
		List<Task> precedences = task.getPrecedences();
		for (Task precedence : precedences) {
			if (currTime < taskCompletionTimes.get(precedence))
				return false;
		}
		return true;
	}
}
