/**
 *  Copyright (C) 2000-2012 The Software Conservancy and Original Authors.
 *  All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to
 *  deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 *
 *  Nothing in this notice shall be deemed to grant any rights to trademarks,
 *  copyrights, patents, trade secrets or any other intellectual property of the
 *  licensor or any contributor except as expressly stated herein. No patent
 *  license is granted separate from the Software, for code that you delete from
 *  the Software, or for combinations of the Software with other software or
 *  hardware.
 */
package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.filter.SuiteEndStateFilter;
import org.chorusbdd.chorus.tools.webagent.filter.SuiteNameFilter;
import org.chorusbdd.chorus.tools.webagent.filter.TestSuiteFilter;
import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;
import org.chorusbdd.chorus.tools.webagent.util.WebAgentUtil;
import org.chorusbdd.chorus.util.NetworkUtils;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;
import java.util.List;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 16:31
 */
public abstract class AbstractSuiteListHandler extends XmlStreamingHandler {

    private WebAgentFeatureCache cache;
    private TestSuiteFilter testSuiteFilter;
    private String handledPath;
    private String pathSuffix;
    private int localPort;
    private final String handledPathWithSuffix;

    public AbstractSuiteListHandler(WebAgentFeatureCache webAgentFeatureCache, TestSuiteFilter testSuiteFilter, String handledPath, String pathSuffix, int localPort) {
        this.cache = webAgentFeatureCache;
        this.testSuiteFilter = testSuiteFilter;
        this.handledPath = handledPath;
        this.pathSuffix = pathSuffix;
        this.localPort = localPort;
        this.handledPathWithSuffix = handledPath + pathSuffix;
    }

    @Override
    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter writer) throws Exception {
        TestSuiteFilter filter = getSuiteFilter(testSuiteFilter, target, request);
        List<WebAgentTestSuite> suites = cache.getSuites(filter);
        doHandle(suites, target, baseRequest, request, response, writer);
    }

    /**
     * A subclass may decorate, amend or otherwise change the filter rules
     * @return The test suite filter to use to generate the suite list
     */
    protected TestSuiteFilter getSuiteFilter(TestSuiteFilter testSuiteFilter, String target, HttpServletRequest request) {

        //if parameter set, add the filter rule to the rule chain
        if ( request.getParameter(SuiteNameFilter.SUITE_NAME_HTTP_PARAMETER) != null ) {
            String[] suiteNames = request.getParameterValues(SuiteNameFilter.SUITE_NAME_HTTP_PARAMETER);
            testSuiteFilter = new SuiteNameFilter(suiteNames, testSuiteFilter);
        }

        //if parameter set, add the filter rule to the rule chain
        if ( request.getParameter(SuiteEndStateFilter.SUITE_END_STATE_HTTP_PARAMETER) != null) {
            String[] suiteEndStates = request.getParameterValues(SuiteEndStateFilter.SUITE_END_STATE_HTTP_PARAMETER);
            //add the filter rule to the rule chain
            testSuiteFilter = new SuiteEndStateFilter(suiteEndStates, testSuiteFilter);
        }
        return testSuiteFilter;
    }

    protected abstract void doHandle(List<WebAgentTestSuite> suites, String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter writer) throws Exception;

    @Override
    protected boolean shouldHandle(String target) {
        return handledPathWithSuffix.equals(target);
    }

    public String getHandledPath() {
        return handledPath;
    }

    public WebAgentFeatureCache getCache() {
        return cache;
    }

    public int getLocalPort() {
        return localPort;
    }

    protected String getLinkToSuite(WebAgentTestSuite s) {
        String suiteHttpName = WebAgentUtil.urlEncode(s.getSuiteId());
        return "http://" + NetworkUtils.getHostname() + ":" + getLocalPort() + "/" + getCache().getHttpName() + "/testSuite.xml?suiteId=" + suiteHttpName;
    }
}
