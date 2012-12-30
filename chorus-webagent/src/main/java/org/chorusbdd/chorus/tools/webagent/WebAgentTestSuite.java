package org.chorusbdd.chorus.tools.webagent;

import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.results.TestSuite;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * User: nick
 * Date: 30/12/12
 * Time: 00:11
 */
public class WebAgentTestSuite extends TestSuite {

    private static final ThreadLocal<SimpleDateFormat> startTimeFormatter = new ThreadLocal<SimpleDateFormat>() {
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd HH:mm:ss zzz");
        }
    };
    private final String suiteTime;

    public WebAgentTestSuite(ExecutionToken executionToken, List<FeatureToken> features) {
        super(executionToken, features);
        this.suiteTime = startTimeFormatter.get().format(executionToken.getExecutionStartTime());
    }

    public String getFinalStatusAsString() {
        return getExecutionToken().isPassed() ? "Passed" :
                getExecutionToken().isPending() ? "Pending" : "Failed";
    }
    public String getSuiteNameWithTime() {
        return getTestSuiteName() + " " + suiteTime;
    }

    public String getSuiteStartTime() {
        return suiteTime;
    }

    /**
     * @return Suite name with timestamp which together identify this TestSuite instance
     */
    public String getSuiteNameWithTimestamp() {
        return getTestSuiteName() + "-" + getExecutionStartTime();
    }
}
