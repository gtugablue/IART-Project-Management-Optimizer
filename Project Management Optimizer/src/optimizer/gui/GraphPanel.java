package optimizer.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import optimizer.Problem;
import optimizer.Solution;
import optimizer.domain.Skill;
import optimizer.domain.Task;

public class GraphPanel implements ViewerListener {
	private Problem problem;
	private Graph graph;
	private Viewer viewer;
	private HashMap<Skill, Color> skillColors;
	public GraphPanel(Problem problem) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		this.problem = problem;
		this.graph = new MultiGraph("Graph");
		for (Task t : problem.getTasks()) {
			Node n = graph.addNode(t.getName());
			n.addAttribute("label", t.getName());
			n.addAttribute("skill", t.getSkill().getName());
			for (Task precedence : t.getPrecedences())
				graph.addEdge(t.getName()+"-"+precedence.getName(), t.getName(), precedence.getName(), true);
		}
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		viewer.addDefaultView(false);
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		String styleSheet = ""
				+ "node {shape: box; size-mode:dyn-size;text-mode: hidden;z-index: 0;}"
				+ "edge {shape: line;fill-color: #222;arrow-size: 10px, 3px;}";
		graph.setAttribute("ui.stylesheet", styleSheet);
		List<Color> colors = generateColors(problem.getSkills().size());
		skillColors = new HashMap<Skill, Color>();
		for (int i = 0; i < colors.size(); i++)
			skillColors.put(problem.getSkills().get(i), colors.get(i));
	}

	public void display() {
		graph.display(false);
	}
	
	public ViewPanel getView() {
		return viewer.getDefaultView();
	}

	public void update(Solution solution) {
		int numTasks = problem.getTasks().size();
		List<Task> tasks = problem.getTasks();
		for (int i = 0; i < numTasks; i++) {
			Node n = this.graph.getNode(i);
			Task t = problem.getTasks().get(i);
			int duration = solution.getTaskDuration(t);
			n.setAttribute("xy", solution.getTaskStartTime(t) + duration / 2, i);
			Color c = skillColors.get(t.getSkill());
			n.setAttribute("ui.style", "size: " + duration + "gu, 1gu; fill-color: rgb(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ");");
		}
		ViewPanel view = viewer.getDefaultView();
		if (view != null) {
			//view.resizeFrame(800, 600);
			//view.getCamera().setViewCenter(solution.getTotalTime() / 2, 0, 0);
			//view.getCamera().setViewPercent(1);
			//view.getCamera().setGraphViewport(-solution.getTotalTime() / 2, 0, problem.scoreLimit(), 5 * numTasks);
		}
	}
	
	public List<Color> generateColors(int n) {
		List<Color> colors = new LinkedList<Color>();
		for(int i = 0; i < 360; i += 360 / n) {
		    Color color = Color.getHSBColor(i, (float)(90 + Math.random() * 10), (float)(50 + Math.random() * 10));
		    colors.add(color);
		}
		return colors;
	}

	@Override
	public void buttonPushed(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonReleased(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewClosed(String id) {
		// TODO Auto-generated method stub
		
	}
}