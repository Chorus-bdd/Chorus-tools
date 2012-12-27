package org.chorusbdd.chorus.tools.webagent;

import org.chorusbdd.chorus.tools.xml.writer.TestSuite;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 16:33
 */
public interface TestSuiteFilter {

    public boolean accept(TestSuite suite);

    public static TestSuiteFilter ALL_SUITES = new TestSuiteFilter() {
        @Override
        public boolean accept(TestSuite suite) {
            return true;
        }
    };

}
