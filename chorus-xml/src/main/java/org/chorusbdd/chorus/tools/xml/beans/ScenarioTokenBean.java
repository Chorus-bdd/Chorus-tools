package org.chorusbdd.chorus.tools.xml.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.chorusbdd.chorus.core.interpreter.results.StepToken;
import org.chorusbdd.chorus.tools.xml.adapter.StepTokenAdapter;

public class ScenarioTokenBean {

    private String name;
    private List<StepToken> steps ;
    private String tags;

    public ScenarioTokenBean() {}

    @XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name="steps")
	@XmlJavaTypeAdapter(StepTokenAdapter.class)
	public List<StepToken> getStep() {
		return steps;
	}

	public void setSteps(List<StepToken> steps) {
		this.steps = steps;
	}

	@XmlAttribute()
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
    
    
}
