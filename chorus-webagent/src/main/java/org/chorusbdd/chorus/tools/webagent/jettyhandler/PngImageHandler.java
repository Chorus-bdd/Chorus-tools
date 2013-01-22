package org.chorusbdd.chorus.tools.webagent.jettyhandler;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: nick
 * Date: 22/01/13
 * Time: 19:06
 */
public class PngImageHandler extends AbstractWebAgentHandler {

    private Map<String, byte[]> cachedImageData = Collections.synchronizedMap(new HashMap<String, byte[]>());

    @Override
    protected void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resource = getResourceSuffix(target);
        resource = STYLESHEET_RESOURCE_PATH + resource;
        byte[] imgData = getImageData(resource);
        if ( imgData != null ) {
            response.getOutputStream().write(imgData);
        }  else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private byte[] getImageData(String resource) throws IOException {
        byte[] result;
        synchronized (cachedImageData) {
            if ( cachedImageData.containsKey(resource) ) {
                result = cachedImageData.get(resource); //may be null
            } else {
                result = readByteArrayFromClasspath(resource);
                cachedImageData.put(resource, result);
            }
        }
        return result;
    }

    protected String getContentType(String target) {
        return "image/png";
    }

    @Override
    protected boolean shouldHandle(String target) {
        return target.endsWith(".png");
    }
}
