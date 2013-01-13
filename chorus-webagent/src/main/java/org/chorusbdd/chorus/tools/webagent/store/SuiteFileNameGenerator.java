package org.chorusbdd.chorus.tools.webagent.store;

import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 13/01/13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public interface SuiteFileNameGenerator {

    File getSuiteFile(File storeDirectory, WebAgentTestSuite suite);

    File[] getChildSuites(File storeDirectory);
}
