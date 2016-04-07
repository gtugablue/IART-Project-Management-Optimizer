package domain;

import java.util.ArrayList;
import java.util.List;

public class Task {
	private String name;
	private int duration;
	private List<Skill> skills;
	
	public Task(String name, int duration) {
		this.name = name;
		this.duration = duration;
		this.skills = new ArrayList<Skill>();
	}
	
	public Task(String name, int duration, List<Skill> skills) {
		this.name = name;
		this.duration = duration;
		this.skills = skills;
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
