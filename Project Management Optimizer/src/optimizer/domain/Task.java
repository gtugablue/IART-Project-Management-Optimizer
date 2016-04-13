package optimizer.domain;

import java.util.ArrayList;
import java.util.List;

public class Task {
	private String name;
	private int duration;
	private Skill skill;
	private List<Task> precedences;
	public Task(String name, int duration) {
		this.name = name;
		this.duration = duration;
		this.skill = null;
		this.precedences = new ArrayList<Task>();
	}
	
	public Task(String name, int duration, Skill skill, List<Task> precedences) {
		this.name = name;
		this.duration = duration;
		this.skill = skill;
		this.precedences = precedences;
	}
	
	public Skill getSkill() {
		return this.skill;
	}
	
	public List<Task> getPrecedences() {
		return this.precedences;
	}
	
	public void setPrecedencies(List<Task> precedences) {
		this.precedences = precedences;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
