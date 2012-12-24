package org.chorusbdd.chorus.tools.xml.beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


import org.chorusbdd.chorus.results.StepEndState;

@XmlType(propOrder = {"type", "action", "endState", "message", "throwable"})
public class StepTokenBean {
	private String type;
    private String action;

    private StepEndState endState;
    private String message = "";
    private String throwable;
    
    public StepTokenBean() {}

    @XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	@XmlAttribute
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@XmlAttribute
	public StepEndState getEndState() {
		return endState;
	}

	public void setEndState(StepEndState endState) {
		this.endState = endState;
	}

	@XmlAttribute
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getThrowable() {
		return throwable;
	}

	public void setThrowable(String throwable) {
		this.throwable = throwable;
	};
    
    
}
