package optimizer;

import java.util.Arrays;
import java.util.List;

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
		int[] taskCompletionTimes = new int[problem.getTasks().size()];
		Arrays.fill(taskCompletionTimes, Integer.MAX_VALUE);
		int[] elementReadyTimes = new int[problem.getElements().size()];
		Arrays.fill(elementReadyTimes, 0);
		
		return score;
	}
	
	private int elementToID(Element element) {
		for (int i = 0; i < problem.getElements().size(); i++) {
			if (problem.getElements().get(i).equals(element))
				return i;
		}
		return -1;
	}
}
