package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 10:50
 *
 * Serve an xsl stylesheet from a classpath resouce
 */
public class StyleSheetHandler extends AbstractWebAgentHandler {

    private static final Log log = LogFactory.getLog(StyleSheetHandler.class);

    private final Map<String, char[]> stylesheetCache = Collections.synchronizedMap(new HashMap<String, char[]>());

    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        synchronized (stylesheetCache) {
            String classpathResource = "/stylesheets" + target;
            char[] stylesheet;
            if ( stylesheetCache.containsKey(classpathResource)) {
                stylesheet = stylesheetCache.get(classpathResource);
                log.info("Serving cached stylesheet from cache " + classpathResource);
            }  else {
                log.info("Serving stylesheet from classpath " + classpathResource);
                stylesheet = readStylesheetFromClasspath(response, classpathResource);
                stylesheetCache.put(classpathResource, stylesheet);
            }
            sendResponse(response, stylesheet);
        }
    }

    /**
     * @return char[] with stylesheet contents from classpath or null if not found
     */
    private char[] readStylesheetFromClasspath(HttpServletResponse response, String classpathResource) throws IOException {
        char[] stylesheet = null;
        URL u = getClass().getResource(classpathResource);
        if ( u != null ) {
            InputStream is = u.openStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            stylesheet = new String(buffer.toByteArray(), "UTF-8").toCharArray();
        }
        return stylesheet;
    }

    private void sendResponse(HttpServletResponse response, char[] stylesheet) throws IOException {
        log.info("Stylesheet exists? " + (stylesheet != null));
        if ( stylesheet == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.getWriter().write(stylesheet);
        }
    }

    protected boolean shouldHandle(String target) {
        return target.endsWith("xsl");
    }

    protected String getContentType() {
        return "text/xsl;charset=utf-8";
    }
}
