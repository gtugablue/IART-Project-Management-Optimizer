package optimizer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
				int taskStartTime = calculateTaskStartTime(task, taskCompletionTimes, elementReadyTimes);
				currTime = taskStartTime;
				break;
			}
		}
		return score;
	}
	
	private int calculateTaskStartTime(Task task, HashMap<Task, Integer> taskCompletionTimes, HashMap<Element, Integer> elementReadyTimes) {
		int precedencesReadyTime = 0;
		if (task.getPrecedences().size() >= 1)
		{
			precedencesReadyTime = Integer.MAX_VALUE;
			for (Task precedence : task.getPrecedences()) {
				int time = taskCompletionTimes.get(precedence);
				if (time > precedencesReadyTime)
					precedencesReadyTime = time;
			}
		}
		if (precedencesReadyTime == Integer.MAX_VALUE)
			return precedencesReadyTime; // If the task's precedences haven't been met, there's not need to check if there are elements available
		
		int elementReadyTime = Integer.MAX_VALUE;
		Iterator<Entry<Element, Integer>> it = elementReadyTimes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Element, Integer> pair = (Map.Entry<Element, Integer>)it.next();
			if (pair.getKey().getSkillPerfomance(task.getSkill()) > 0) {
				int time = pair.getValue();
				elementReadyTime = Math.min(elementReadyTime, time);
			}
		}
		
		return Math.max(precedencesReadyTime, elementReadyTime);
	}

	private boolean allTasksDone(HashMap<Task, Integer> taskCompletionTimes) {
		Iterator<Entry<Task, Integer>> it = taskCompletionTimes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Task, Integer> pair = (Map.Entry<Task, Integer>)it.next();
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
