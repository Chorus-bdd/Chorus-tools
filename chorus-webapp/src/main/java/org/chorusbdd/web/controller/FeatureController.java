package org.chorusbdd.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.Features;
import org.chorusbdd.structure.feature.command.OptimisticLockFailedException;
import org.chorusbdd.web.view.ResultView;
import org.chorusbdd.web.view.structure.FeatureView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.web.controller.Controllers.redirectToFeature;
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

    @RequestMapping(value = "/features/{featureId:.*}", method = RequestMethod.PUT)
    public ResultView putFeature(final HttpServletResponse response,
                                       @PathVariable final String featureId,
                                       @RequestParam(value = "current-md5", required = false, defaultValue = "") String optimisticMd5,
                                       @RequestParam(value = "text", required = true) String text) {
        try {
            features.commands().apply(features.events().modify(featureId, text, optimisticMd5));
            return redirectToFeature(response, featureId);
        } catch (final OptimisticLockFailedException e) {
            final ResultView resultView = new ResultView();
            resultView.setSucceeded(false);
            resultView.setMessage(e);
            return resultView;
        }
    }

    @RequestMapping(value = "/features/{featureId:.*}", method = RequestMethod.GET)
    @JsonView(FeatureView.FullDetailView.class)
    public FeatureView viewFeature(@PathVariable final String featureId) {
        final Feature feature = features.repository().findById(featureId);
        if (feature == null) {
            throw new RuntimeException("feature not found");
        }
        return asFeatureView(feature);
    }

    @RequestMapping(value = "/features/{featureId}/{revisionId}", method = RequestMethod.GET)
    @JsonView(FeatureView.FullDetailView.class)
    public FeatureView viewFeatureAtRevision(@PathVariable final String featureId,
                                             @PathVariable final String revisionId) {
        final Feature feature = features.repository().findAtRevision(featureId, revisionId);
        if (feature == null) {
            throw new RuntimeException("feature not found");
        }
        return asFeatureView(feature);
    }

    @RequestMapping(value = "/features/{featureId:.*}", method = RequestMethod.DELETE)
    public ResultView deleteFeature(@PathVariable final String featureId) {
        final ResultView resultView = new ResultView();
        try {
            features.commands().apply(features.events().delete(featureId));
            resultView.setSucceeded(true);
        } catch (Exception e) {
            resultView.setSucceeded(false);
            resultView.setMessage(e);
        }
        return resultView;
    }

    @RequestMapping(value = "/move-feature", method = RequestMethod.PUT)
    @JsonView(FeatureView.SummaryView.class)
    public FeatureView moveFeature(@RequestParam(value = "target", required = true) String from,
                                   @RequestParam(value = "destination",   required = true) String to) {
        features.commands().apply(features.events().move(from, to));
        return asFeatureView(features.repository().findById(to));
    }
}