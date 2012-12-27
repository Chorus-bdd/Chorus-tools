package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.TestSuiteFilter;
import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.chorusbdd.chorus.tools.xml.writer.TestSuite;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 16:31
 */
public abstract class AbstractSuiteListHandler extends AbstractWebAgentHandler {

    private WebAgentFeatureCache webAgentFeatureCache;
    private TestSuiteFilter testSuiteFilter;
    private String handledPath;

    public AbstractSuiteListHandler(WebAgentFeatureCache webAgentFeatureCache, TestSuiteFilter testSuiteFilter, String handledPath) {
        this.webAgentFeatureCache = webAgentFeatureCache;
        this.testSuiteFilter = testSuiteFilter;
        this.handledPath = handledPath;
    }

    @Override
    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TestSuite> suites = webAgentFeatureCache.getSuites(testSuiteFilter);
        doHandle(suites, target, baseRequest, request, response);
    }

    protected abstract void doHandle(List<TestSuite> suites, String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException;

    @Override
    protected boolean shouldHandle(String target) {
        return handledPath.equals(target);
    }
}
