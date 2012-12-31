package org.chorusbdd.chorus.tools.xml.beans;


import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.chorusbdd.chorus.results.EndState;
import org.chorusbdd.chorus.results.ScenarioToken;
import org.chorusbdd.chorus.tools.xml.adapter.ScenarioTokenAdapter;

public class FeatureTokenBean {

	private String name;
	private String usesHandlers;
	private String configurationName;
    private String description;
    private List<ScenarioToken> scenarios ;
    private EndState endState;

    public FeatureTokenBean(){}

	
	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getUsesHandlers() {
		return usesHandlers;
	}

	public void setUsesHandlers(String usesHandlers) {
		this.usesHandlers = usesHandlers;
	}

    @XmlAttribute
	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElementWrapper(name="scenarios")
	@XmlJavaTypeAdapter(ScenarioTokenAdapter.class)
	public List<ScenarioToken> getScenario() {
		return scenarios;
	}

	public void setScenarios(List<ScenarioToken> scenarios) {
		this.scenarios = scenarios;
	}

    @XmlAttribute
    public EndState getEndState() {
        return endState;
    }

    public void setEndState(EndState endState) {
        this.endState = endState;
    }
	
}
