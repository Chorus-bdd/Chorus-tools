package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 11:53
 *
 * Show an index of the available resources under a feature cache
 */
public class CacheIndexHandler extends XmlStreamingHandler {

    private WebAgentFeatureCache cache;
    private final String resourcePath;
    private final String resourcePathWithIndex;

    public CacheIndexHandler(WebAgentFeatureCache cache) {
        this.cache = cache;
        this.resourcePath = "/" + cache.getHttpName() + "/";
        this.resourcePathWithIndex = resourcePath + "index.xml";
    }

    @Override
    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument();
        addStylesheetInstruction(writer, "cacheIndexResponse.xsl");
        writer.writeStartElement("cache");
        writer.writeAttribute("name", cache.getName());
        writer.writeEmptyElement("resource");
        writer.writeAttribute("name", "allTestSuites");
        writer.writeAttribute("rssLink", "./allTestSuites.rss");
        writer.writeAttribute("xmlLink", "./allTestSuites.xml");
        writer.writeEndElement();
        writer.writeEndDocument();
    }

    @Override
    protected boolean shouldHandle(String target) {
        return target.equals(resourcePath) || target.equals(resourcePathWithIndex);
    }
}
