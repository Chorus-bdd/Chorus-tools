package org.chorusbdd.web.view.structure;

import com.fasterxml.jackson.annotation.JsonView;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Arrays;
import java.util.stream.Stream;

@NotThreadSafe
public class PakageView {

    // ------------------------------------------------------------------ Views

    public interface BaseView {}
    public interface FullDetailView extends BaseView {}

    public PakageView() { /* do nothing */ }

    // ------------------------------------------------------------- Properties

    private String id   = "";
    private String name = "";
    private String link = "";
    private FeatureView[] features    = {};
    private PakageView[]  subpackages = {};

    @JsonView(BaseView.class)
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @JsonView(BaseView.class)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @JsonView(BaseView.class)
    public PakageView[] getSubpackages() {
        return subpackages;
    }

    public void setSubpackages(final Stream<PakageView> subpackages) {
        this.subpackages = subpackages.toArray(PakageView[]::new);
    }

    @JsonView(BaseView.class)
    public FeatureView[] getFeatures() {
        return features;
    }

    public void setFeatures(final Stream<FeatureView> features) {
        this.features = features.toArray(FeatureView[]::new);
    }

    @JsonView(BaseView.class)
    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    // ----------------------------------------------------------------- Object

    @Override
    public String toString() {
        return "PakageView{" +
                "features=" + Arrays.toString(features) +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", subpackages=" + Arrays.toString(subpackages) +
                '}';
    }
}
