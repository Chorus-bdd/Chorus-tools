package org.chorusbdd.web.view.structure;

import com.fasterxml.jackson.annotation.JsonView;
import org.chorusbdd.web.view.BaseView;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class FeatureView {

    // ------------------------------------------------------------------ Views

    public interface SummaryView extends BaseView {}
    public interface FullDetailView extends BaseView {}

    public FeatureView() { /* do nothing */ }

    // ------------------------------------------------------------- Properties

    private String id   = "";
    private String name = "";
    private String link = "";
    private String md5  = "";
    private String text = "";
    private String packageId = "";

    @JsonView(BaseView.class)
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @JsonView(BaseView.class)
    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(final String pid) {
        this.packageId = pid;
    }

    @JsonView(BaseView.class)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @JsonView(BaseView.class)
    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    @JsonView(FullDetailView.class)
    public String getMd5() {
        return md5;
    }

    public void setMd5(final String md5) {
        this.md5 = md5;
    }

    @JsonView(FullDetailView.class)
    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    // ----------------------------------------------------------------- Object

    @Override
    public String toString() {
        return "FeatureView{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
