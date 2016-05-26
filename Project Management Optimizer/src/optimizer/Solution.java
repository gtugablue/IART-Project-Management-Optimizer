package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import optimizer.domain.Task;

public class Solution {
	protected Problem problem;
	protected List<Integer> taskOrder;
	protected HashMap<Task, Integer> taskStartTimes;
	protected HashMap<Task, Integer> taskCompletionTimes;
	protected int totalTime;
	public Solution(Problem problem) {
		this.problem = problem;
		this.taskOrder = new ArrayList<Integer>();
		this.taskStartTimes = new LinkedHashMap<Task, Integer>();
		this.taskCompletionTimes = new LinkedHashMap<Task, Integer>();
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
