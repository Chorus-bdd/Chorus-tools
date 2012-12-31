package org.chorusbdd.chorus.tools.xml.beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.chorusbdd.chorus.results.ResultsSummary;
import org.chorusbdd.chorus.tools.xml.adapter.ResultSummaryAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecutionTokenBean {

	private String testSuiteName;
    private long executionStartTimestamp;
    private String executionStartTime;
    private ResultsSummary resultsSummary;
    private String executionHost;
	
	public ExecutionTokenBean() {}

	@XmlElement
	public String getTestSuiteName() {
		return testSuiteName;
	}

	@XmlJavaTypeAdapter(ResultSummaryAdapter.class)
	public ResultsSummary getResultsSummary() {
		return resultsSummary;
	}


	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public void setResultsSummary(ResultsSummary resultsSummary) {
		this.resultsSummary = resultsSummary;
	}

    @XmlAttribute
    public long getExecutionStartTimestamp() {
        return executionStartTimestamp;
    }

    public void setExecutionStartTimestamp(long executionStartTimestamp) {
        this.executionStartTimestamp = executionStartTimestamp;
    }

    @XmlAttribute
    public String getExecutionStartTime() {
        return executionStartTime;
    }

    public void setExecutionStartTime(String executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    @XmlAttribute
    public String getExecutionHost() {
        return executionHost;
    }

    public void setExecutionHost(String executionHost) {
        this.executionHost = executionHost;
    }
}
