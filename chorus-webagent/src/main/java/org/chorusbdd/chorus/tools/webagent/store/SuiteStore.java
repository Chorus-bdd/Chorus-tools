package org.chorusbdd.chorus.tools.webagent.store;

import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 13/01/13
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public interface SuiteStore {

    List<WebAgentTestSuite> loadTestSuites();

    void addSuiteToStore(WebAgentTestSuite suite);

    void removeSuiteFromStore(WebAgentTestSuite suite);

}
