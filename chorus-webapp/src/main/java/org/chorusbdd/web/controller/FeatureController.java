package org.chorusbdd.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.Features;
import org.chorusbdd.structure.feature.command.OptimisticLockFailedException;
import org.chorusbdd.web.view.ErrorEntity;
import org.chorusbdd.web.view.structure.FeatureView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.web.controller.Controllers.featureLink;
import static org.chorusbdd.web.controller.Controllers.resourceConflicted;
import static org.chorusbdd.web.controller.Controllers.resourceCreated;
import static org.chorusbdd.web.controller.Controllers.resourceNotFound;
import static org.chorusbdd.web.controller.ViewMapper.asFeatureView;


@RestController
@RequestMapping("/resource")
public class FeatureController {

    private final Features features;

    @Autowired
    public FeatureController(final Features features) {
        this.features = notNull(features);
    }

    // -------------------------------------------------------- Control Methods

    @RequestMapping(value = "/features/{featureId}", method = RequestMethod.PUT)
    public Object putFeature(final HttpServletResponse response,
                               @PathVariable final String featureId,
                               @RequestHeader(value = "If-Match", defaultValue = "") String expectedMd5,
                               @RequestParam(value = "text", required = true) String text) {
        try {
            features.commands().apply(features.events().modify(featureId, text, expectedMd5));
            return resourceCreated(response, featureId, featureLink(featureId));
        } catch (final OptimisticLockFailedException e) {
            return resourceConflicted(response, e.getMessage());
        }
    }

    @RequestMapping(value = "/features/{featureId}", method = RequestMethod.GET)
    @JsonView(FeatureView.FullDetailView.class)
    public Object viewFeature(final HttpServletResponse response,
                                    @PathVariable final String featureId) {
        final Feature feature = features.repository().findById(featureId);
        if (feature == null) {
            return resourceNotFound(response);
        }
        response.setHeader("ETag", feature.md5());
        return asFeatureView(feature);
    }

    @RequestMapping(value = "/features/{featureId}/{revisionId}", method = RequestMethod.GET)
    @JsonView(FeatureView.FullDetailView.class)
    public Object viewFeatureAtRevision(final HttpServletResponse response,
                                     @PathVariable final String featureId,
                                     @PathVariable final String revisionId) {
        final Feature feature = features.repository().findAtRevision(featureId, revisionId);
        if (feature == null) {
            return resourceNotFound(response);
        }
        return asFeatureView(feature);
    }

    @RequestMapping(value = "/features/{featureId}", method = RequestMethod.DELETE)
    public ErrorEntity deleteFeature(final HttpServletResponse response,
                                     @PathVariable final String featureId) {
        final ErrorEntity resultView = new ErrorEntity();
        try {
            features.commands().apply(features.events().delete(featureId));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resultView.setMessage(e);
        }
        return resultView;
    }

    @RequestMapping(value = "/features/{targetFeatureId}/move", method = RequestMethod.PUT)
    @JsonView(FeatureView.SummaryView.class)
    public FeatureView moveFeature(@PathVariable final String targetFeatureId,
                                   @RequestParam(value = "to", required = true) final String destFeatureId) {
        features.commands().apply(features.events().move(targetFeatureId, destFeatureId));
        return asFeatureView(features.repository().findById(destFeatureId));
    }
}