package org.chorusbdd.chorus.tools.webagent.store;

import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 13/01/13
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class NullSuiteStore implements SuiteStore {

    public List<WebAgentTestSuite> loadTestSuites() {
        return Collections.EMPTY_LIST;
    }

    public void addSuiteToStore(WebAgentTestSuite suite) {
    }

    public void removeSuiteFromStore(WebAgentTestSuite suite) {
    }

    public String toString() {
        return "NullSuiteStore";
    }
}
