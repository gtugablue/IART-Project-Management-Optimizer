package optimizer.solver.genetic_algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import optimizer.Problem;
import optimizer.domain.Element;
import optimizer.domain.Task;

public class Solution implements Cloneable {
	protected Problem problem;
	protected List<Integer> taskOrder;
	protected List<List<Integer>> taskElements;
	protected int score;

	public Solution(Problem problem) {
		this.problem = problem;
	}

	public int evaluate() {
		HashMap<Task, Integer> taskCompletionTimes = new LinkedHashMap<Task, Integer>();
		HashMap<Element, Integer> elementReadyTimes = new LinkedHashMap<Element, Integer>();
		createTimes(taskCompletionTimes, elementReadyTimes);
		int currTime = 0;
		
		while (!allTasksDone(taskCompletionTimes)) {
			for (int i = 0; i < taskOrder.size(); i++) {
				Task task = problem.getTasks().get(i);
				if (!taskCompletionTimes.get(task).equals(Integer.MAX_VALUE)) continue; // Task already done, skip it
				int taskStartTime = calculateTaskStartTime(task, taskCompletionTimes, elementReadyTimes);
				if (taskStartTime == Integer.MAX_VALUE) continue; // Precedences not ready, try a different task
				currTime = taskStartTime;
				allocateElementsToTask(task, taskElements.get(i), currTime, taskCompletionTimes, elementReadyTimes);
				break;
			}
		}

		score = findMaxTaskCompletionTime(taskCompletionTimes);
		return score;
	}

	private int findMaxTaskCompletionTime(HashMap<Task, Integer> taskCompletionTimes) {
		int max = 0;
		Iterator<Entry<Task, Integer>> it = taskCompletionTimes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Task, Integer> pair = (Map.Entry<Task, Integer>)it.next();
			int value = pair.getValue();
			if (value > max)
				max = value;
		}
		return max;
	}

	private void allocateElementsToTask(Task task, List<Integer> taskElements, int currTime, HashMap<Task, Integer> taskCompletionTimes, HashMap<Element, Integer> elementReadyTimes) {
		float totalPerformance = 0;
		ArrayList<Element> assignedElements = new ArrayList<Element>();
		for (int id : taskElements) {
			if (currTime < elementReadyTimes.get(problem.getElements().get(id)))
				break; // Element not ready, don't assign to task
			float performance = problem.getElements().get(id).getSkillPerfomance(task.getSkill());
			if (performance <= 0)
				break; // Element hasn't got the skill to do the task, don't assign
			totalPerformance += performance;
			assignedElements.add(problem.getElements().get(id));
		}
		if (totalPerformance <= 0) { // Can happen if the algorithm doesn't generate an element with the skill to realize the task. If so, find any available element to be assigned to the task.
			float performance = findFreeElementID(task, elementReadyTimes).getSkillPerfomance(task.getSkill());
			totalPerformance += performance;
		}
		int duration = (int)(totalPerformance * task.getDuration());
		duration = (int)(task.getDuration() / totalPerformance);
		int endTime = currTime + duration;
		taskCompletionTimes.put(task, endTime);
		for (Element element : assignedElements) {
			elementReadyTimes.put(element, endTime);
		}
	}

	private Element findFreeElementID(Task task, HashMap<Element, Integer> elementReadyTimes) {
		Iterator<Entry<Element, Integer>> it = elementReadyTimes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Element, Integer> pair = (Map.Entry<Element, Integer>)it.next();
			if (pair.getKey().getSkillPerfomance(task.getSkill()) > 0)
				return pair.getKey();
		}
		return null;
	}

	private int calculateTaskStartTime(Task task, HashMap<Task, Integer> taskCompletionTimes, HashMap<Element, Integer> elementReadyTimes) {
		int precedencesReadyTime = 0;
		if (task.getPrecedences().size() >= 1)
		{
			for (Task precedence : task.getPrecedences()) {
				int time = taskCompletionTimes.get(precedence);
				if (time > precedencesReadyTime)
					precedencesReadyTime = time;
			}
		}
		if (precedencesReadyTime == Integer.MAX_VALUE)
			return precedencesReadyTime; // If the task's precedences haven't been met, there's not need to check if there are elements available

		int elementReadyTime = Integer.MAX_VALUE;
		for (Entry<Element, Integer> entry : elementReadyTimes.entrySet()) {
			if (entry.getKey().getSkillPerfomance(task.getSkill()) > 0) {
				int time = entry.getValue();
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
			if (currTime < taskCompletionTimes.get(precedence)) {
				return false;
			}
		}
		return true;
	}

	public Solution(Solution s) {
		problem = s.problem;
		taskOrder = new ArrayList<Integer>();
		taskElements = new ArrayList<List<Integer>>();
		for (int i = 0; i < s.taskOrder.size(); i++) {
			taskOrder.add(s.taskOrder.get(i));
			List<Integer> l = new ArrayList<Integer>();
			for (int j = 0; j < s.taskElements.get(i).size(); j++) {
				l.add(s.taskElements.get(i).get(j));
			}
			taskElements.add(l);
		}
		score = s.score;
	}

	@Override
	public Solution clone() {
		return new Solution(this);
	}
}
