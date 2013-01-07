package org.chorusbdd.chorus.tools.xml.beans;

import org.chorusbdd.chorus.results.EndState;
import org.chorusbdd.chorus.results.StepToken;
import org.chorusbdd.chorus.tools.xml.adapter.StepTokenAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

public class ScenarioTokenBean {

    private String name;
    private List<StepToken> steps ;
    private String tags;
    private EndState endState;

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

    @XmlAttribute
    public EndState getEndState() {
        return endState;
    }

    public void setEndState(EndState endState) {
        this.endState = endState;
    }
}
