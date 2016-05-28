package optimizer.domain;

import java.util.*;

public class Element {
	private String name;
	private Map<Skill, Float> skills;
	protected Map<Float,Task> assignementTimes= new HashMap<>();


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

	public boolean canBeAssigned(float start, float end){
		Set<Float> keys = assignementTimes.keySet();

		for(Float t_start : keys){
			float t_end = assignementTimes.get(t_start).getDuration()+start;
			if((start > t_start && start < t_end)
					|| (t_start > start && t_start < end)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifies if a client could be assigned to a specific project that starts and finishes at start time
	 * @param start
	 * @return
     */
	public boolean isFree(Float start){
		return canBeAssigned(start,start);
	}

	/**
	 * Verifies if a Element is free to be assigned to a task that starts and finishes at start time and if true assigns it
	 * @param start
	 * @param task
     * @return
     */
	public boolean assign(float start, Task task){
		if (!skills.containsKey(skills) || assignementTimes.containsValue(task))
			return false;
		if(isFree(start)){
			assignementTimes.put(start,task);
			task.assignElement(this);
			return true;
		}else{
			return false;
		}
	}

	public void removeFromTaskArray(Task... tasks){
		for(Task task : tasks){
			removeFromTask(task);
		}
	}

	public boolean removeFromTask(Task task){
		if(!assignementTimes.containsValue(task)){
			return false;
		}
		Set<Float> keys = assignementTimes.keySet();
		for (Float startTime : keys){
			Task t_task = assignementTimes.get(startTime);
			if(t_task.equals(task)) {
				assignementTimes.remove(startTime);
				task.removeAssignedElement(this);
				break;
			}
		}
		return true;
	}
}

