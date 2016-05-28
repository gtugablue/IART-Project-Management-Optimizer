package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import optimizer.domain.Task;

public class Solution {
	protected Problem problem;
	protected List<Integer> taskOrder = new ArrayList<>();
	protected HashMap<Task, Float> taskStartTimes = new LinkedHashMap<>();
	protected HashMap<Task, Float> taskCompletionTimes = new LinkedHashMap<>();
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
}
