package org.chorusbdd.web.controller;

import org.chorusbdd.web.view.ErrorEntity;
import org.chorusbdd.web.view.ResourceEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

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

    public static String pakageLink(final String pakageId) {
        return createResourceUri("/packages/" + pakageId);
    }

    public static ResourceEntity resourceCreated(final HttpServletResponse response, final String id, final String link) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", link);
        final ResourceEntity entity = new ResourceEntity();
        entity.setId(id);
        entity.setLocation(link);
        return entity;
    }

    public static ErrorEntity resourceConflicted(final HttpServletResponse response, final String message) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        final ErrorEntity entity = new ErrorEntity();
        entity.setMessage(message);
        return entity;
    }

    public static ErrorEntity resourceNotFound(final HttpServletResponse response) {
        return resourceNotFound(response, "Requested resource not found");
    }

    public static ErrorEntity resourceNotFound(final HttpServletResponse response, final String message) {
        response.setStatus(SC_NOT_FOUND);
        final ErrorEntity entity = new ErrorEntity();
        entity.setMessage(message);
        return entity;
    }
}
