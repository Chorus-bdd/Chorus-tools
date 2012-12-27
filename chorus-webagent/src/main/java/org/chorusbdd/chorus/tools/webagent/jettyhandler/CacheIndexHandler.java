package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 11:53
 *
 * Show an index of the available resources under a feature cache
 */
public class CacheIndexHandler extends AbstractWebAgentHandler {

    private WebAgentFeatureCache cache;
    private final String resourcePath;
    private final String resourcePathWithTrailingSlash;

    public CacheIndexHandler(WebAgentFeatureCache cache) {
        this.cache = cache;
        this.resourcePath = "/" + cache.getHttpName();
        this.resourcePathWithTrailingSlash = resourcePath + "/";
    }

    @Override
    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        XMLOutputFactory f = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = f.createXMLStreamWriter(response.getWriter());
            writer.writeStartDocument();
            writer.writeProcessingInstruction("xml-stylesheet", "type='text/xsl' href='cacheIndexResponse.xsl'");
            writer.writeStartElement("cache");
            writer.writeAttribute("name", cache.getName());
            writer.writeEmptyElement("resource");
            writer.writeAttribute("name", "allTestSuites");
            writer.writeAttribute("rssFeedLink", "allTestSuites.rss");
            writer.writeAttribute("htmlLink", "allTestSuites.html");
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new IOException("Failed to render response as xml stream", e);
        }
    }

    @Override
    protected boolean shouldHandle(String target) {
        return target.endsWith("/") ?
            resourcePathWithTrailingSlash.equals(target) :
            resourcePath.equals(target);
    }
}
