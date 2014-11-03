package org.chorusbdd.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.Features;
import org.chorusbdd.structure.feature.FeaturesImpl;
import org.chorusbdd.structure.feature.command.OptimisticLockFailedException;
import org.chorusbdd.structure.pakage.Pakage;
import org.chorusbdd.structure.pakage.Pakages;
import org.chorusbdd.structure.pakage.PakagesImpl;
import org.chorusbdd.web.view.BinaryResultView;
import org.chorusbdd.web.view.structure.FeatureView;
import org.chorusbdd.web.view.structure.PakageView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/resource")
public class FeatureController {
    private static final Log LOG = LogFactory.getLog(FeatureController.class);

    private final Features features = new FeaturesImpl();
    private final Pakages pakages = new PakagesImpl();

    public FeatureController() {
        // do nothing
    }

    // -------------------------------------------------------- Control Methods

    interface PackagesView extends PakageView.FullDetailView, FeatureView.SummaryView {}

    @RequestMapping(value = "/packages", method = RequestMethod.GET)
    @JsonView(PackagesView.class)
    public PakageView findPakagesRoot() {
        final Pakage pakage = pakages.repository().findRoot();
        if (pakage == null) {
            throw new RuntimeException("test root pakage not found");
        }
        return toPakageView(pakage);
    }

    @RequestMapping(value =  "/packages/{pakageId:.*}", method = RequestMethod.GET)
    @JsonView(PackagesView.class)
    public PakageView findPakage(@PathVariable final String pakageId) {
        final Pakage pakage = pakages.repository().findById(pakageId);
        if (pakage == null) {
            throw new RuntimeException("package not found");
        }
        return toPakageView(pakages.repository().findById(pakageId));
    }

    @RequestMapping(value = "/packages/p/{pakageId:.*}", method = RequestMethod.GET) // TODO: change to PUT
    public void createPakage(final HttpServletResponse response,
                            @PathVariable final String pakageId) {
        pakages.commands().apply(pakages.events().store(pakageId));
        redirect(response, pakageId);
    }

    private void redirect(final HttpServletResponse response, final String path) {
        try {
            response.sendRedirect(createResourceUri("/packages/" + path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/move-package/p/{pakageId:.*}", method = RequestMethod.GET) // TODO: change to PUT
    public void movePakage(final HttpServletResponse response,
                           @PathVariable final String target,
                           @RequestParam(value = "destination",   required = true) String destination) {
        pakages.commands().apply(pakages.events().move(target, destination));
        redirect(response, destination);
    }

    @RequestMapping(value = "/move-package", method = RequestMethod.GET) // TODO: change to PUT
    public void movePakage1(final HttpServletResponse response,
                           @RequestParam(value = "target",   required = true) String target,
                           @RequestParam(value = "destination",   required = true) String destination) {
        pakages.commands().apply(pakages.events().move(target, destination));
        redirect(response, destination);
    }


    @RequestMapping(value = "/packages/d/{pakageId:.*}", method = RequestMethod.GET) // TODO: change to delete, remove d
    public BinaryResultView deletePakage(@PathVariable final String pakageId) {
        final BinaryResultView resultView = new BinaryResultView();
        try {
            pakages.commands().apply(pakages.events().delete(pakageId));
            resultView.setSucceeded(true);
        } catch (Exception e) {
            resultView.setSucceeded(false);
            resultView.setMessage(e);
        }
        return resultView;
    }

    // features

    @RequestMapping(value = "/features/p/{featureId:.*}", method = RequestMethod.GET) // TODO: change to put, remove p
    public BinaryResultView putFeature(final HttpServletResponse response,
                                       @PathVariable final String featureId,
                                       @RequestParam(value = "current-md5", required = false, defaultValue = "") String optimisticMd5,
                                       @RequestParam(value = "text", required = true) String text) {
        try {
            features.commands().apply(features.events().modify(featureId, text, optimisticMd5));
            //featureRepository.store(featureId, text, optimisticMd5);
            return redirectToFeature(response, featureId);
        } catch (final OptimisticLockFailedException e) {
            final BinaryResultView resultView = new BinaryResultView();
            resultView.setSucceeded(false);
            resultView.setMessage(e);
            return resultView;
        }
    }

    @RequestMapping(value = "/features/{featureId:.*}", method = RequestMethod.GET)
    @JsonView(FeatureView.FullDetailView.class)
    public FeatureView listFeature(@PathVariable final String featureId) {
        final Feature feature = features.repository().findById(featureId);
        if (feature == null) {
            throw new RuntimeException("feature not found");
        }
        return toFeatureView(feature);
    }

    @RequestMapping(value = "/features/d/{featureId:.*}", method = RequestMethod.GET) // TODO: change to delete, remove d
    public BinaryResultView deleteFeature(@PathVariable final String featureId) {
        final BinaryResultView resultView = new BinaryResultView();
        try {
            features.commands().apply(features.events().delete(featureId));
            resultView.setSucceeded(true);
        } catch (Exception e) {
            resultView.setSucceeded(false);
            resultView.setMessage(e);
        }
        return resultView;
    }

    @RequestMapping(value = "/move-feature/p", method = RequestMethod.GET) // TODO: change to put, remove p
    @JsonView(FeatureView.SummaryView.class)
    public FeatureView moveFeature(@RequestParam(value = "target", required = true) String from,
                                   @RequestParam(value = "destination",   required = true) String to) {
        features.commands().apply(features.events().move(from, to));
        return toFeatureView(features.repository().findById(to));
    }

    @RequestMapping(value = "/echo/{value1:.*}", method = RequestMethod.GET)
    public String echo(@PathVariable String value1) {
        return value1;
    }

    // ------------------------------------------------------- View Translation

    private PakageView toPakageView(final Pakage pakage) {
        final PakageView view = new PakageView();
        view.setId(pakage.id());
        view.setName(pakage.humanName());
        view.setSubpackages(pakage.subpackages().stream().map(this::toPakageView));
        view.setFeatures(pakage.features().stream().map(this::toFeatureSummaryView));
        view.setLink(createResourceUri("/packages/" + pakage.id()));
        return view;
    }

    private FeatureView toFeatureSummaryView(final Feature feature) {
        final FeatureView view = new FeatureView();
        view.setId(feature.id());
        view.setPackageId(feature.pakageId());
        view.setName(feature.humanName());
        view.setMd5(feature.md5().toString());
        view.setLink(featureLink(feature.id()));
        return view;
    }

    private FeatureView toFeatureView(final Feature feature) {
        final FeatureView view = toFeatureSummaryView(feature);
        view.setBody(feature.text());
        return view;
    }

    // ------------------------------------------------------- URI's

    private <T> T redirectToFeature(final HttpServletResponse response, final String featureId) {
        try {
            response.sendRedirect(featureLink(featureId));
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String featureLink(final String featureId) {
        return createResourceUri("/features/" + featureId);
    }

    private static String createResourceUri(final String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/resource" + path).build().toUriString();
    }

}