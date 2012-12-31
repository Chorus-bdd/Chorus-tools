package org.chorusbdd.chorus.tools.xml.beans;

import org.chorusbdd.chorus.results.EndState;

import javax.xml.bind.annotation.XmlAttribute;

public class ResultSummaryBean {

    private int featuresPassed = 0;
    private int featuresPending = 0;
    private int featuresFailed = 0;

    //stats
    private int scenariosPassed = 0;
    private int scenariosPending = 0;
    private int scenariosFailed = 0;
    private int unavailableHandlers = 0;

    private int stepsPassed = 0;
    private int stepsFailed = 0;
    private int stepsPending = 0;
    private int stepsUndefined = 0;
    private int stepsSkipped = 0;

    private long timeTaken = -1;
    private String timeTakenSeconds;

    private EndState endState;

    @XmlAttribute
	public int getFeaturesPassed() {
		return featuresPassed;
	}

	public void setFeaturesPassed(int featuresPassed) {
		this.featuresPassed = featuresPassed;
	}

	@XmlAttribute
	public int getFeaturesPending() {
		return featuresPending;
	}

	public void setFeaturesPending(int featuresPending) {
		this.featuresPending = featuresPending;
	}

	@XmlAttribute
	public int getFeaturesFailed() {
		return featuresFailed;
	}

	public void setFeaturesFailed(int featuresFailed) {
		this.featuresFailed = featuresFailed;
	}

	@XmlAttribute
	public int getScenariosPassed() {
		return scenariosPassed;
	}

	public void setScenariosPassed(int scenariosPassed) {
		this.scenariosPassed = scenariosPassed;
	}

	@XmlAttribute
	public int getScenariosPending() {
		return scenariosPending;
	}

	public void setScenariosPending(int scenariosPending) {
		this.scenariosPending = scenariosPending;
	}

	@XmlAttribute
	public int getScenariosFailed() {
		return scenariosFailed;
	}

	public void setScenariosFailed(int scenariosFailed) {
		this.scenariosFailed = scenariosFailed;
	}

	@XmlAttribute
	public int getUnavailableHandlers() {
		return unavailableHandlers;
	}

	public void setUnavailableHandlers(int unavailableHandlers) {
		this.unavailableHandlers = unavailableHandlers;
	}

	@XmlAttribute
	public int getStepsPassed() {
		return stepsPassed;
	}

	public void setStepsPassed(int stepsPassed) {
		this.stepsPassed = stepsPassed;
	}

	@XmlAttribute
	public int getStepsFailed() {
		return stepsFailed;
	}

	public void setStepsFailed(int stepsFailed) {
		this.stepsFailed = stepsFailed;
	}

	@XmlAttribute
	public int getStepsPending() {
		return stepsPending;
	}

	public void setStepsPending(int stepsPending) {
		this.stepsPending = stepsPending;
	}

	@XmlAttribute
	public int getStepsUndefined() {
		return stepsUndefined;
	}

	public void setStepsUndefined(int stepsUndefined) {
		this.stepsUndefined = stepsUndefined;
	}

	@XmlAttribute
	public int getStepsSkipped() {
		return stepsSkipped;
	}

	public void setStepsSkipped(int stepsSkipped) {
		this.stepsSkipped = stepsSkipped;
	}

    @XmlAttribute
    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    @XmlAttribute
    public String getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(String timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    @XmlAttribute
    public EndState getEndState() {
        return endState;
    }

    public void setEndState(EndState endState) {
        this.endState = endState;
    }
}
