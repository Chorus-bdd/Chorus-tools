package org.chorusbdd.chorus.tools.webagent.filter;

import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 13/01/13
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public class LastNItemsFilter implements TestSuiteFilter {

    private int count;
    private int maxItems;

    public LastNItemsFilter(int maxItems) {
        this.maxItems = maxItems;
    }

    @Override
    public void reset() {
        count = 0;
    }

    public boolean accept(WebAgentTestSuite suite) {
        return ++count <= maxItems;
    }
}
