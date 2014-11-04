package org.chorusbdd.web.controller;

import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.pakage.Pakage;
import org.chorusbdd.web.view.structure.FeatureView;
import org.chorusbdd.web.view.structure.PakageView;

import static org.chorusbdd.web.controller.Controllers.createResourceUri;
import static org.chorusbdd.web.controller.Controllers.featureLink;

public class ViewMapper {
    public static PakageView asPakageView(final Pakage pakage) {
        final PakageView view = new PakageView();
        view.setId(pakage.id());
        view.setName(pakage.humanName());
        view.setSubpackages(pakage.subpackages().stream().map(ViewMapper::asPakageView));
        view.setFeatures(pakage.features().stream().map(ViewMapper::asFeatureSummaryView));
        view.setLink(createResourceUri("/packages/" + pakage.id()));
        return view;
    }

    public static FeatureView asFeatureSummaryView(final Feature feature) {
        final FeatureView view = new FeatureView();
        view.setId(feature.id());
        view.setPackageId(feature.pakageId());
        view.setName(feature.humanName());
        view.setMd5(feature.md5());
        view.setLink(featureLink(feature.id()));
        return view;
    }

    public static FeatureView asFeatureView(final Feature feature) {
        final FeatureView view = asFeatureSummaryView(feature);
        view.setBody(feature.text());
        return view;
    }
}
