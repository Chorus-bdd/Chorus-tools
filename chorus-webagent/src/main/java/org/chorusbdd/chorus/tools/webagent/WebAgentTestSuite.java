package org.chorusbdd.chorus.tools.webagent;

import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.results.TestSuite;
import org.chorusbdd.chorus.tools.xml.util.FormattingUtils;

import java.util.List;

/**
 * User: nick
 * Date: 30/12/12
 * Time: 00:11
 */
public class WebAgentTestSuite extends TestSuite {

    private final String suiteTime;

    public WebAgentTestSuite(ExecutionToken executionToken, List<FeatureToken> features) {
        super(executionToken, features);
        this.suiteTime = FormattingUtils.getStartTimeFormatter().format(executionToken.getExecutionStartTime());
    }

    public String getFinalStatusAsString() {
        switch (getExecutionToken().getEndState()) {
            case PASSED: return "Passed";
            case PENDING: return "Pending";
            case FAILED: return "Failed";
            default:
                throw new UnsupportedOperationException("Unknown end state " + getExecutionToken().getEndState());
        }
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
    public String getSuiteId() {
        return getTestSuiteName() + "-" + getExecutionStartTime();
    }
}
