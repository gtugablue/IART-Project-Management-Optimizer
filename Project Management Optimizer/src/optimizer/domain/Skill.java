package optimizer.domain;

import java.util.LinkedHashSet;
import java.util.Set;

public class Skill {
	private String name;
	private Set<Element> whoHas = new LinkedHashSet<Element>();
	public Skill(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addCapableElement(Element element) {
		whoHas.add(element);
	}
	
	public Set<Element> whoHas() {
		return whoHas;
	}
}
