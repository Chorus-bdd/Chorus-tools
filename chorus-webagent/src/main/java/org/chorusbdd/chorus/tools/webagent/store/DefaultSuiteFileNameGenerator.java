package org.chorusbdd.chorus.tools.webagent.store;

import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;
import org.chorusbdd.chorus.tools.webagent.util.WebAgentUtil;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 13/01/13
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSuiteFileNameGenerator implements SuiteFileNameGenerator {

    private static final String SUITE_FILE_SUFFIX = "-suite.xml";

    @Override
    public File getSuiteFile(File storeDirectory, WebAgentTestSuite suite) {
        return new File(storeDirectory, WebAgentUtil.urlEncode(suite.getSuiteId()) + SUITE_FILE_SUFFIX);
    }

    @Override
    public File[] getChildSuites(File storeDirectory) {
        return storeDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(SUITE_FILE_SUFFIX);
            }
        });
    }
}
