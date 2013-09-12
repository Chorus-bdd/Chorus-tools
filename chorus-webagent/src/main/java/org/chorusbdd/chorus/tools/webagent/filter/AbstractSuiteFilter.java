package org.chorusbdd.chorus.tools.webagent.filter;

import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;

/**
 * Created with IntelliJ IDEA.
 * User: GA2EBBU
 * Date: 23/01/13
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSuiteFilter implements TestSuiteFilter {

    protected TestSuiteFilter wrappedFilter;

    public AbstractSuiteFilter(TestSuiteFilter wrappedFilter) {
        this.wrappedFilter = wrappedFilter;
    }

    public final boolean accept(WebAgentTestSuite suite) {
        boolean accept = wrappedFilter.accept(suite);
        if ( accept ) {
            accept = applyFilter(suite);
        }
        return accept;
    }

    protected abstract boolean applyFilter(WebAgentTestSuite suite);

}
