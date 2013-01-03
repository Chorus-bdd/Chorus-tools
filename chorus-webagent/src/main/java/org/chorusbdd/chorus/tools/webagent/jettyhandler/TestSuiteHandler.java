package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;
import org.chorusbdd.chorus.tools.xml.writer.TestSuiteXmlWriter;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.PropertyException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;

/**
 * User: nick
 * Date: 29/12/12
 * Time: 09:31
 */
public class TestSuiteHandler extends XmlStreamingHandler {

    private static final Log log = LogFactory.getLog(TestSuiteHandler.class);

    private WebAgentFeatureCache cache;

    public TestSuiteHandler(WebAgentFeatureCache cache) {
        this.cache = cache;
    }

    @Override
    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter writer) throws IOException {
        String suiteId = baseRequest.getParameter("suiteId");
        if ( suiteId == null) {
            response.setStatus(HttpServletResponse.SC_MULTIPLE_CHOICES);
            response.getWriter().append("Must specify a suite using parameter suiteId");
        } else {
            WebAgentTestSuite s = cache.getSuite(suiteId);
            if ( s == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().append("Could not find a test suite " + suiteId);
            } else {
                try {
                    handleForSuite(response, s);
                } catch (PropertyException pe) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    log.error("Failed to serialize test suite to xml, PropertyException, probably your Marshaller " +
                         "implementation does not support the property com.sun.xml.internal.bind.xmlHeaders", pe);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); //client may not see this if we have already written output
                    log.error("Failed to serialize test suite to xml", e);
                }
            }
        }
    }

    private void handleForSuite(HttpServletResponse response, WebAgentTestSuite s) throws Exception {
        TestSuiteXmlWriter testSuiteWriter = new TestSuiteXmlWriter();
        testSuiteWriter.addMarshallerProperty(
            "com.sun.xml.internal.bind.xmlHeaders",      //:( this may break with some Marshaller implementations
            "<?xml-stylesheet type='text/xsl' href='/testSuiteResponse.xsl'?>\n"
        );
        testSuiteWriter.write(response.getWriter(), s);
    }

    @Override
    protected boolean shouldHandle(String target) {
        return target.endsWith("testSuite.xml");
    }
}
