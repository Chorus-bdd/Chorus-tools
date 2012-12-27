package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 10:00
 */
public abstract class AbstractWebAgentHandler extends AbstractHandler {


    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (shouldHandle(target)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(getContentType());
            doHandle(target, baseRequest, request, response);
            baseRequest.setHandled(true);
        }
    }

    protected String getContentType() {
        return "text/xml;charset=utf-8";
    }

    protected abstract void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException;

    protected abstract boolean shouldHandle(String target);

    protected void writeSimpleTextElement(XMLStreamWriter writer, String element, String elementText) throws XMLStreamException {
        writer.writeStartElement(element);
        writer.writeCharacters(elementText);
        writer.writeEndElement();
    }
}
