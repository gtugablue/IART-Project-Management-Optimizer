package optimizer.domain;

import scala.xml.Elem;

import java.util.*;
import java.lang.Math.*;

public class Task {
	private String name;
	private int duration;
	private Skill skill;
	private List<Task> precedences = new ArrayList<Task>();
	private List<Task> successors = new ArrayList<Task>();
    private Integer position = null; //Needed for SA

	public Task(String name, int duration) {
		this.name = name;
		this.duration = duration;
		this.skill = null;
	}
	
	public Task(String name, int weight, Skill skill, List<Task> precedences) {
		this.name = name;
		this.duration = weight;
		this.skill = skill;
		this.precedences = precedences;
		for (Task t : precedences) {
			t.successors.add(this);
		}
    }
	
	public Task(String name, int weight, Skill skill){
		this.name = name;
		this.duration = weight;
		this.skill = skill;
	}
	
	public void addPrecedence(Task task){
		precedences.add(task);
        task.addSuccessor(this);
	}
	
	public Skill getSkill() {
		return this.skill;
	}
	
	public List<Task> getPrecedences() {
		return this.precedences;
	}
	
	public void setPrecedences(List<Task> precedences) {
		this.precedences = precedences;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

    public List<Task> getSuccessors() {
        return successors;
    }

    public void setSuccessors(List<Task> successors) {
        this.successors = successors;
    }

    public void addSuccessor(Task task){
        this.successors.add(task);
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Task))return false;

        Task task = (Task) obj;

        return this.getName().equals(task.getName())
                && this.getDuration() == task.getDuration()
                && this.getSkill().equals(task.getSkill())
                ;
    }

    public int getLastPrecedence(){
        int best = -1;
        for(Task task : precedences){
            if(task.getPosition() >= best)
                best = task.getPosition();
        }
        return best;
    }

    public int getFirstSuccessor(){
        int best = Integer.MAX_VALUE;
        for(Task task : successors){
            if(task.getPosition() == null)
                continue;
            if(task.getPosition() <= best)
                best = task.getPosition();
        }
        return best;
    }

    /**
     * Retorna a posição do sucessor que aparece em primeiro na lista. Se o sucessor
     * @param size
     * @return
     */
    public int getFirstSuccessor(int size){
        int bestSize = getFirstSuccessor();
        if(bestSize >= 9999999)
            return size;
        else
            return bestSize;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                '}';
    }
}
