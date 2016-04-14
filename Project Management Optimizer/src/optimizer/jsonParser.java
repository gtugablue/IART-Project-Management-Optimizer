package optimizer;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.*;

import optimizer.domain.Element;
import optimizer.domain.Skill;
import optimizer.domain.Task;

public class jsonParser {

	String filepath;
	ProjectManagementOptimizer otimizer;

	/**
	 * json parser constructor
	 * 
	 * @param filepath
	 * @param otimizer
	 */
	public jsonParser(String filepath, ProjectManagementOptimizer otimizer) {
		this.filepath = filepath;
		this.otimizer = otimizer;
	}

	/**
	 * Method to parse the input file
	 */
	public void parser() {

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(filepath));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray skillsArray = (JSONArray) jsonObject.get("skills");

			Iterator<String> skillsIterator = skillsArray.iterator();
			while (skillsIterator.hasNext()) {
				Skill oneSkill = new Skill(skillsIterator.next());
				otimizer.skills.add(oneSkill);
				System.out.println("Skill: " + oneSkill.getName());
			}

			JSONArray tasksArray = (JSONArray) jsonObject.get("tasks");

			for (int i = 0; i < tasksArray.size(); i++) {

				JSONObject oneTask = (JSONObject) tasksArray.get(i);

				String name = (String) oneTask.get("name");
				System.out.println(name);
				long skill = (long) oneTask.get("skill");
				System.out.println(skill);
				Skill oneSkill = new Skill(String.valueOf(skill));

				Task newTask = new Task(name, 0, oneSkill, null);
				otimizer.tasks.add(newTask);

			}

			JSONArray elementsArray = (JSONArray) jsonObject.get("elements");

			for (int k = 0; k < elementsArray.size(); k++) {

				JSONObject oneElement = (JSONObject) elementsArray.get(k);

				String element = (String) oneElement.get("name");
				System.out.println(element);

				Element el = new Element(element);

				JSONArray Elskills = (JSONArray) oneElement.get("skills");

				for (int j = 0; j < Elskills.size(); j++) {
					JSONArray oneSkill = (JSONArray) Elskills.get(j);

					long elementSkill = (long) oneSkill.get(0);
					System.out.println(elementSkill);
					Skill elSkill = new Skill(String.valueOf(elementSkill));
					double performance = (double) oneSkill.get(1);
					System.out.println(performance);

					el.addSkill(elSkill, (float) performance);

				}
				
				otimizer.elements.add(el);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
