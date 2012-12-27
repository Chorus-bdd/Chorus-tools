package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
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
 * Time: 10:05
 */
public class IndexHandler extends AbstractWebAgentHandler {

    private List<WebAgentFeatureCache> cacheList;

    public IndexHandler(List<WebAgentFeatureCache> cacheList) {
        this.cacheList = cacheList;
    }

    @Override
    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        XMLOutputFactory f = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = f.createXMLStreamWriter(response.getWriter());
            writer.writeStartDocument();
            writer.writeProcessingInstruction("xml-stylesheet", "type='text/xsl' href='indexResponse.xsl'");
            writer.writeStartElement("index");
            for ( WebAgentFeatureCache cache : cacheList) {
                writer.writeEmptyElement("featureCache");
                writer.writeAttribute("name", cache.getName());
                writer.writeAttribute("numberOfTestSuites", String.valueOf(cache.getNumberOfTestSuites()));
                writer.writeAttribute("maxHistory", String.valueOf(cache.getMaxHistory()));
                writer.writeAttribute("suitesReceived", String.valueOf(cache.getSuitesReceived()));
                writer.writeAttribute("indexLink", "/" + cache.getHttpName() + "/index.xml" );
            }
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new IOException("Failed to render response as xml stream", e);
        }
    }

    @Override
    protected boolean shouldHandle(String target) {
        return "/".equals(target);
    }
}
