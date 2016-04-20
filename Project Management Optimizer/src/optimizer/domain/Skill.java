package optimizer.domain;

import java.util.LinkedHashSet;
import java.util.Set;

public class Skill {
	private String name;
	public Skill(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
