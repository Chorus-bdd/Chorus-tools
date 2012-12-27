package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.TestSuiteFilter;
import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.chorusbdd.chorus.tools.xml.writer.TestSuite;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 16:58
 *
 * Presents a filtered list of test suites from a web agent cache as a xml document representation with a stylesheet for html conversion
 */
public class HtmlSuiteListHandler extends AbstractSuiteListHandler {

    public HtmlSuiteListHandler(WebAgentFeatureCache webAgentFeatureCache, TestSuiteFilter testSuiteFilter, String handledPath) {
        super(webAgentFeatureCache, testSuiteFilter, handledPath);
    }

    @Override
    protected void doHandle(List<TestSuite> suites, String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
