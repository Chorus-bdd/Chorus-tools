package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chorusbdd.chorus.tools.webagent.util.WebAgentUtil;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;

/**
 * User: nick
 * Date: 31/12/12
 * Time: 11:34
 */
public abstract class XmlStreamingHandler extends AbstractWebAgentHandler {

    private static final Log log = LogFactory.getLog(XmlStreamingHandler.class);

    @Override
    protected final void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            XMLStreamWriter writer = WebAgentUtil.getIndentingXmlStreamWriter(response);
            doHandle(target, baseRequest, request, response, writer);
            writer.flush();
        } catch (XMLStreamException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); //client may not see if content already sent
            log.error("Failed while streaming XML", e);
        }
    }

    protected abstract void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter writer) throws XMLStreamException, IOException;

}
