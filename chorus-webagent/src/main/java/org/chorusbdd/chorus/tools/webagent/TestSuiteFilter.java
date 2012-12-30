package org.chorusbdd.chorus.tools.webagent;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 16:33
 */
public interface TestSuiteFilter {

    public boolean accept(WebAgentTestSuite suite);

    public static TestSuiteFilter ALL_SUITES = new TestSuiteFilter() {
        @Override
        public boolean accept(WebAgentTestSuite suite) {
            return true;
        }
    };

}
