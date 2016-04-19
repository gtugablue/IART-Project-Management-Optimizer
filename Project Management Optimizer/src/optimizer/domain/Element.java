package optimizer.domain;

import java.util.*;

public class Element {
	private String name;
	private Map<Skill, Float> skills;
	protected Map<Integer,Task> assignementTimes= new HashMap<>();


	public Element(String name) {
		this.name = name;
		this.skills = new HashMap<Skill, Float>();
	}
	
	public Element(String name, Map<Skill, Float> skills) {
		this.name = name;
		this.skills = skills;
	}
	
	public void addSkill(Skill skill, float performance) {
		this.skills.put(skill, performance);
	}
	
	public float getSkillPerfomance(Skill skill) {
		Float performance = this.skills.get(skill);
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

	public boolean canAssign(int start, int end){
		Set<Integer> keys = assignementTimes.keySet();

		for(Integer key : keys){
			int t_end = assignementTimes.get(key).getWeight()+start;

			if((start > key && start < t_end)
					|| (key > start && key < end)){
				return false;
			}
		}
		return true;
	}

	public boolean assign(int start, Task task){
		int end = task.getWeight() + start;
		if(canAssign(start, end)){
			assignementTimes.put(start,task);
			return true;
		}else{
			return false;
		}

	}
}

