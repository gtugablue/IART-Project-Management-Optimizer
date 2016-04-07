package optimizer.domain;

import java.util.HashMap;
import java.util.Map;

public class Element {
	private String name;
	private Map<Skill, Integer> skills;
	
	public Element(String name) {
		this.name = name;
		this.skills = new HashMap<Skill, Integer>();
	}
	
	public Element(String name, Map<Skill, Integer> skills) {
		this.name = name;
		this.skills = skills;
	}
	
	public void addSkill(Skill skill, int performance) {
		this.skills.put(skill, performance);
	}
	
	public int getSkillPerfomance(Skill skill) {
		Integer performance = this.skills.get(skill);
		if (performance == null) return -1;
		return performance;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
