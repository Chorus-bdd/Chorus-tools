package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: nick
 * Date: 29/12/12
 * Time: 09:31
 */
public class TestSuiteHandler extends AbstractWebAgentHandler {

    private WebAgentFeatureCache cache;

    public TestSuiteHandler(WebAgentFeatureCache cache) {
        this.cache = cache;
    }

    @Override
    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    protected boolean shouldHandle(String target) {
        return target.endsWith("testSuite.xml");
    }
}
