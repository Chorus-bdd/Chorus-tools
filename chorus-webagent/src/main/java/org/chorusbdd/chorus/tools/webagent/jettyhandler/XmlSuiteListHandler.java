package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.TestSuiteFilter;
import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.chorusbdd.chorus.tools.webagent.util.WebAgentUtil;
import org.chorusbdd.chorus.tools.xml.writer.TestSuite;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.List;

/**
 * User: nick
 * Date: 28/12/12
 * Time: 14:37
 */
public class XmlSuiteListHandler extends AbstractSuiteListHandler {

    private String title;

    public XmlSuiteListHandler(WebAgentFeatureCache cache, TestSuiteFilter testSuiteFilter, String handledPath, String pathSuffix, String title, int localPort) {
        super(cache, testSuiteFilter, handledPath, pathSuffix, localPort);
        this.title = title;
    }

    protected void doHandle(List<TestSuite> suites, String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            XMLStreamWriter writer = WebAgentUtil.getIndentingXmlStreamWriter(response);
            writer.writeStartDocument();
            addStylesheetInstruction(writer, "suiteListResponse.xsl");
            writer.writeStartElement("suiteList");
            writer.writeAttribute("title", title);
            for (TestSuite s : suites) {
                writer.writeStartElement("item");
                writer.writeAttribute("title", s.getSuiteNameWithTime());
                writer.writeAttribute("link", getLinkToSuite(s));
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
        } catch (XMLStreamException e) {
            throw new IOException("Failed to render response as xml stream", e);
        }
    }

}

