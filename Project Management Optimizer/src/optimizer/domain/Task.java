package optimizer.domain;

import java.util.ArrayList;
import java.util.List;

public class Task {
	private String name;
	private int duration;
	private List<Skill> skills;
	private List<Task> precedences;
	public Task(String name, int duration) {
		this.name = name;
		this.duration = duration;
		this.skills = new ArrayList<Skill>();
		this.precedences = new ArrayList<Task>();
	}
	
	public Task(String name, int duration, List<Skill> skills, List<Task> precedences) {
		this.name = name;
		this.duration = duration;
		this.skills = skills;
		this.precedences = precedences;
	}
	
	public void addSkill(Skill skill) {
		this.skills.add(skill);
	}
	
	public boolean hasSkill(Skill skill) {
		return this.skills.contains(skill);
	}
	
	public List<Skill> getSkills() {
		return this.skills;
	}
	
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	
	public List<Task> getPrecedencies() {
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
