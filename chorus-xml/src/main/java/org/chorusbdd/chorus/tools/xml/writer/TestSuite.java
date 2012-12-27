package org.chorusbdd.chorus.tools.xml.writer;

import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.tools.xml.adapter.ExecutionTokenAdapter;
import org.chorusbdd.chorus.tools.xml.adapter.FeatureTokenAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Nick
 * Date: 17/10/12
 * Time: 17:53
 *
 * A wrapper around the tokens which represent the state of a test suite
 * either in progress or completed
 */
@XmlRootElement
public class TestSuite {

    private static final ThreadLocal<SimpleDateFormat> startTimeFormatter = new ThreadLocal<SimpleDateFormat>() {
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd HH:mm:ss zzz");
        }
    };

	private ExecutionToken executionToken;
	private List<FeatureToken> featureTokens;
    private String suiteTime;

    public TestSuite() {}
    
    public TestSuite(ExecutionToken executionToken, List<FeatureToken> featureTokens) {
        this.executionToken = executionToken;
        this.featureTokens = featureTokens;
        this.suiteTime = startTimeFormatter.get().format(executionToken.getExecutionStartTime());
    }

    @XmlJavaTypeAdapter(ExecutionTokenAdapter.class)
    public ExecutionToken getExecution() {
        return executionToken;
    }

    @XmlElementWrapper(name="features")
    @XmlJavaTypeAdapter(FeatureTokenAdapter.class)
    public List<FeatureToken> getFeature() {
        return featureTokens;
    }

    @XmlAttribute
    public String getSuiteName() {
        return executionToken.getTestSuiteName();
    }

    public String toString() {
    	return executionToken!=null? executionToken.getTestSuiteName(): super.toString();
    }

    public String getSuiteNameWithTime() {
        return getSuiteName() + " " + suiteTime;
    }

    public String getSuiteStartTime() {
        return suiteTime;
    }

    /**
     * @return Suite name with human readable start time
     */
    public long getExecutionStartTime() {
        return executionToken.getExecutionStartTime();
    }

    /**
     * @return Suite name with timestamp which together identify this TestSuite instance
     */
    public String getSuiteNameWithTimestamp() {
        return getSuiteName() + "-" + getExecutionStartTime();
    }

    public String getFinalStatusAsString() {
        return executionToken.isPassed() ? "Passed" :
               executionToken.isPending() ? "Pending" : "Failed";
    }
}
