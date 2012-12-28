package org.chorusbdd.chorus.tools.webagent.util;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * User: nick
 * Date: 28/12/12
 * Time: 14:52
 */
public class WebAgentUtil {

    private static final Log log = LogFactory.getLog(WebAgentUtil.class);

    public static String urlEncode(String token) {
        try {
            token = URLEncoder.encode(token, "utf-8");
        } catch (Exception e) {
            log.error("Failed to URL encode " + token, e);
        }
        return token;
    }

    /**
     * Return an XMLStreamWriter which indents the xml
     * (ideally XMLOutputFactory would support a property which would set this up for us to avoid a dependency
     * on a com.sun.xml class, but it doesn't for the present time appear to do so)
     */
    public static XMLStreamWriter getIndentingXmlStreamWriter(HttpServletResponse response) throws XMLStreamException, IOException {
        XMLOutputFactory f = XMLOutputFactory.newInstance();
        return new IndentingXMLStreamWriter(f.createXMLStreamWriter(response.getWriter()));
    }
}
