package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import optimizer.domain.Task;

public class Solution {
	protected Problem problem;
	protected List<Integer> taskOrder = new ArrayList<>();
	protected HashMap<Task, Integer> taskStartTimes = new LinkedHashMap<>();
	protected HashMap<Task, Integer> taskCompletionTimes = new LinkedHashMap<>();
	protected int totalTime;
	public Solution(Problem problem) {
		this.problem = problem;
	}
	
	public List<Integer> getTaskOrder() {
		return this.taskOrder;
	}
	
	public Integer getTaskStartTime(Task t) {
		return taskStartTimes.get(t);
	}
	
	public Integer getTaskCompletionTime(Task t) {
		return taskCompletionTimes.get(t);
	}
	
	public Integer getTaskDuration(Task t) {
		Integer start = taskStartTimes.get(t);
		if (start == null) return null;
		Integer end = taskCompletionTimes.get(t);
		if (end == null) return null;
		return end - start;
	}
	
	public int getTotalTime() {
		return this.totalTime;
	}
}
