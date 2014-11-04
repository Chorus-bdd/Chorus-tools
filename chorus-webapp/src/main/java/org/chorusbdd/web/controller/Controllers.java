package org.chorusbdd.web.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controllers {

    public static void redirect(final HttpServletResponse response, final String path) {
        try {
            response.sendRedirect(createResourceUri("/packages/" + path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createResourceUri(final String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/resource" + path).build().toUriString();
    }

    public static <T> T redirectToFeature(final HttpServletResponse response, final String featureId) {
         try {
             response.sendRedirect(featureLink(featureId));
             return null;
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }

    public static String featureLink(final String featureId) {
        return createResourceUri("/features/" + featureId);
    }
}
