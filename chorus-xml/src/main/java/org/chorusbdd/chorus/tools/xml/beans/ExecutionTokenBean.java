package org.chorusbdd.chorus.tools.xml.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.chorusbdd.chorus.results.ResultsSummary;
import org.chorusbdd.chorus.tools.xml.adapter.ResultSummaryAdapter;

public class ExecutionTokenBean {
	
	String testSuiteName;
	long executionStartTime;
	ResultsSummary resultsSummary; 
	
	public ExecutionTokenBean() {}

	@XmlElement
	public String getTestSuiteName() {
		return testSuiteName;
	}

	
//	public long getExecutionStartTime() {
//		return executionStartTime;
//	}

	@XmlJavaTypeAdapter(ResultSummaryAdapter.class)
	public ResultsSummary getResultsSummary() {
		return resultsSummary;
	}
	
//	public void setExecutionStartTime(long executionStartTime) {
//		this.executionStartTime = executionStartTime;
//	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}


	public void setResultsSummary(ResultsSummary resultsSummary) {
		this.resultsSummary = resultsSummary;
	};
	
	
}
