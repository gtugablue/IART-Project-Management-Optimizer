package optimizer.gui;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import optimizer.Problem;
import optimizer.domain.Task;

public class GraphFrame {
	private Problem problem;
	private Graph graph;
	public GraphFrame(Problem problem) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		this.problem = problem;
		this.graph = new SingleGraph("Graph");
		for (Task t : problem.getTasks()) {
			graph.addNode(t.getName());
			for (Task precedence : t.getPrecedences())
				graph.addEdge(t.getName()+"-"+precedence.getName(), t.getName(), precedence.getName(), true);
		}
	}
	
	public void display() {
		graph.display();
	}
}
