package optimizer;

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
		return 0;
	}
}
