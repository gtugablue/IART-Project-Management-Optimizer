package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import optimizer.domain.Task;

public class Solution {
	protected Problem problem;
	protected List<Integer> taskOrder;
	protected HashMap<Task, Float> taskStartTimes;
	protected HashMap<Task, Float> taskCompletionTimes;
	protected float totalTime;
	public Solution(Problem problem) {
		this.problem = problem;
		this.taskOrder = new ArrayList<Integer>();
		this.taskStartTimes = new LinkedHashMap<Task, Float>();
		this.taskCompletionTimes = new LinkedHashMap<Task, Float>();
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
	
	public float getTotalTime() {
		return this.totalTime;
	}
}
