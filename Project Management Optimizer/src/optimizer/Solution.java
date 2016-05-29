package optimizer;

import java.util.*;

import optimizer.domain.Element;
import optimizer.domain.Task;

public class Solution {
	protected Problem problem;
	protected List<Integer> taskOrder = new ArrayList<>();
	protected HashMap<Task, Float> taskStartTimes = new LinkedHashMap<>();
	protected HashMap<Task, Float> taskCompletionTimes = new LinkedHashMap<>();
	protected Map<Task, Set<Element>> taskAssignedElements = new HashMap<>();
	protected float totalTime;
	public Solution(Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
	}

	public List<Integer> getTaskOrder() {
		return this.taskOrder;
	}
	
	public Float getTaskStartTime(Task t) {
		return taskStartTimes.get(t);
	}
	
	public Float getTaskCompletionTime(Task t) {
		return taskCompletionTimes.get(t);
	}
	
	public Set<Element> getTaskAssignedElements(Task t) {
		return taskAssignedElements.get(t);
	}
	
	public Float getTaskDuration(Task t) {
		Float start = taskStartTimes.get(t);
		if (start == null) return null;
		Float end = taskCompletionTimes.get(t);
		if (end == null) return null;
		return end - start;
	}

	protected void setTaskOrder(List<Integer> taskOrder) {
		this.taskOrder = taskOrder;
	}

	protected void setTaskStartTimes(HashMap<Task, Float> taskStartTimes) {
		this.taskStartTimes = taskStartTimes;
	}

	protected void setTaskCompletionTimes(HashMap<Task, Float> taskCompletionTimes) {
		this.taskCompletionTimes = taskCompletionTimes;
	}

	protected void setTotalTime(float totalTime) {
		this.totalTime = totalTime;
	}

	public int getTotalTime() {
		return Math.round(this.totalTime);
	}

	@Override
	public String toString() {
		return "Solution{" +
				"taskStartTimes=" + taskStartTimes +
				",\n taskCompletionTimes=" + taskCompletionTimes +
				'}';
	}
}
