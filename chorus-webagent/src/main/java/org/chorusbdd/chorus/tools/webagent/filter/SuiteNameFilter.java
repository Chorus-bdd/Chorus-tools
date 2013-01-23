package org.chorusbdd.chorus.tools.webagent.filter;

import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: nick
 * Date: 23/01/13
 * Time: 09:22
 */
public class SuiteNameFilter implements TestSuiteFilter {

    private Set<String> suiteNames;
    private TestSuiteFilter wrappedFilter;

    public SuiteNameFilter(String[] suiteNames, TestSuiteFilter wrappedFilter) {
        this.suiteNames = new HashSet<String>(Arrays.asList(suiteNames));
        this.wrappedFilter = wrappedFilter;
    }


    public boolean accept(WebAgentTestSuite suite) {
        boolean accept = wrappedFilter.accept(suite);
        if ( accept ) {
            accept = suiteNames.contains(suite.getTestSuiteName());
        }
        return accept;
    }
}
