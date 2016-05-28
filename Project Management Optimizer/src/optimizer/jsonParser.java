package optimizer;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import optimizer.domain.Element;
import optimizer.domain.Skill;
import optimizer.domain.Task;

public class jsonParser {

	/**
	 * Method to parse the input file
	 * @param filePath input file path
	 */
	public static Problem parse(String filePath) {

		JSONParser parser = new JSONParser();
		List<Task> tasks = new ArrayList<Task>();
		List<Skill> skills = new ArrayList<Skill>();
		List<Element> elements = new ArrayList<Element>();

		try {

			Object obj = parser.parse(new FileReader(filePath));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray skillsArray = (JSONArray) jsonObject.get("skills");

			for (int l = 0; l < skillsArray.size(); l++) {
				String skillName = (String) skillsArray.get(l);
				Skill oneSkill = new Skill(skillName);
				skills.add(oneSkill);
			}

			JSONArray tasksArray = (JSONArray) jsonObject.get("tasks");
			ArrayList<ArrayList<Integer>> list = new ArrayList<>();

			for (int i = 0; i < tasksArray.size(); i++) {

				JSONObject oneTask = (JSONObject) tasksArray.get(i);

				ArrayList<Integer> prec = new ArrayList<Integer>();

				String name = (String) oneTask.get("name");
				long duration = (long) oneTask.get("duration");
				long skill = (long) oneTask.get("skill");
				Skill theSkill = skills.get((int) skill);

				JSONArray precedences = (JSONArray) oneTask.get("precedences");

				for (int t = 0; t < precedences.size(); t++) {
					long index = (long) precedences.get(t);
					prec.add((int) index);
				}

				Task newTask = new Task(name, (int) duration, theSkill);
				tasks.add(newTask);

				list.add(prec);

			}

			for (int h = 0; h < list.size(); h++) {
				for (int b = 0; b < list.get(h).size(); b++) {
					tasks.get(h).addPrecedence(tasks.get(list.get(h).get(b)));
				}
			}

			JSONArray elementsArray = (JSONArray) jsonObject.get("elements");

			for (int k = 0; k < elementsArray.size(); k++) {

				JSONObject oneElement = (JSONObject) elementsArray.get(k);

				String element = (String) oneElement.get("name");

				Element el = new Element(element);

				JSONArray Elskills = (JSONArray) oneElement.get("skills");

				for (int j = 0; j < Elskills.size(); j++) {
					JSONArray oneSkill = (JSONArray) Elskills.get(j);

					long elementSkill = (long) oneSkill.get(0);
					Skill elSkill = skills.get((int) elementSkill);
					double performance = (double) oneSkill.get(1);

					el.addSkill(elSkill, (float) performance);

				}

				elements.add(el);
			}

		} catch (Exception e) {
			return null;
		}
		return new Problem(tasks, elements, skills);
	}
}
