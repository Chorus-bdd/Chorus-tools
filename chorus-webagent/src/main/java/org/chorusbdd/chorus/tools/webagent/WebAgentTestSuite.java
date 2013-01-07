/**
 *  Copyright (C) 2000-2012 The Software Conservancy and Original Authors.
 *  All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to
 *  deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 *
 *  Nothing in this notice shall be deemed to grant any rights to trademarks,
 *  copyrights, patents, trade secrets or any other intellectual property of the
 *  licensor or any contributor except as expressly stated herein. No patent
 *  license is granted separate from the Software, for code that you delete from
 *  the Software, or for combinations of the Software with other software or
 *  hardware.
 */
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
