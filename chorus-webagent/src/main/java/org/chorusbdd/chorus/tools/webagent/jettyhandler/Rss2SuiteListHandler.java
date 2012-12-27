package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.TestSuiteFilter;
import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
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
 * Date: 27/12/12
 * Time: 16:58
 *
 * Presents a filtered list of test suites from a web agent cache as a rss2 xml document representation
 */
public class Rss2SuiteListHandler extends AbstractSuiteListHandler {

    private final WebAgentFeatureCache webAgentFeatureCache;
    private final String handledPath;
    private final String title;
    private final String description;
    private final int localPort;
    private final String handledPathWithSuffix;

    public Rss2SuiteListHandler(WebAgentFeatureCache webAgentFeatureCache, TestSuiteFilter testSuiteFilter, String handledPath, String pathSuffix, String title, String description, int localPort) {
        super(webAgentFeatureCache, testSuiteFilter, handledPath);
        this.webAgentFeatureCache = webAgentFeatureCache;
        this.handledPath = handledPath;
        this.handledPathWithSuffix = handledPath + pathSuffix;
        this.title = title;
        this.description = description;
        this.localPort = localPort;
    }

    @Override
    protected void doHandle(List<TestSuite> suites, String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        XMLOutputFactory f = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = f.createXMLStreamWriter(response.getWriter());
            writer.writeStartDocument();
            writer.writeStartElement("rss");
            writer.writeAttribute("version", "2.0");
            writer.writeStartElement("channel");
            writeSimpleTextElement(writer, "title", title);
            writeSimpleTextElement(writer, "link", "http://localhost:" + localPort + "/" + handledPath + ".html");
            writeSimpleTextElement(writer, "description", description);
            for (TestSuite s : suites) {
                writer.writeStartElement("item");
                writeSimpleTextElement(writer, "title", s.getSuiteNameWithTime());
                writeSimpleTextElement(writer, "link", "http://localhost:" + localPort + "/" + webAgentFeatureCache.getHttpName() + "/" + s.getSuiteNameWithTimestamp() + ".html");
                writeSimpleTextElement(writer, "description", "Test suite named " + s.getSuiteName() + " run at " + s.getSuiteStartTime() + " status " + s.getFinalStatusAsString());
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new IOException("Failed to render response as xml stream", e);
        }
    }

    @Override
    protected boolean shouldHandle(String target) {
        return handledPathWithSuffix.equals(target);
    }

}
