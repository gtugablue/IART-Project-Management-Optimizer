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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
