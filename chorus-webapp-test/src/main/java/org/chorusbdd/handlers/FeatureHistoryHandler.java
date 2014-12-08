package org.chorusbdd.handlers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.chorusbdd.FeatureService;
import org.chorusbdd.HttpUtil;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.util.assertion.ChorusAssert;
import org.chorusbdd.experimental.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;
import static org.chorusbdd.HttpUtil.NOT_FOUND;
import static org.chorusbdd.JsonUtil.asList;
import static org.chorusbdd.JsonUtil.asString;
import static org.chorusbdd.JsonUtil.asStringList;
import static org.chorusbdd.chorus.util.assertion.ChorusAssert.assertEquals;

@Handler("Chorus Feature History")
@SuppressWarnings("UnusedDeclaration")
public class FeatureHistoryHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FeatureHistoryHandler.class);
    private static final TestContext context = new TestContext();
    private final FeatureService featureService = new FeatureService();
    private Response contextResponse;
    private ScriptObjectMirror contextLogJson;
    private ScriptObjectMirror contextRevision;
    private ScriptObjectMirror contextChangeset;

    public FeatureHistoryHandler() {
        // do nothing
    }

    // ------------------------------------------------------------------ Given

    // ------------------------------------------------------------------- When

    @Step("^a user views the previous version of feature (.+)$")
    public void userViewsPreviousVersionOfFeature(final String featureId) {
        userViewsNVersionOfFeature(-1, featureId);
    }

    @Step("^a user views the -(\\d+) version of feature (.+)$")
    public void userViewsNVersionOfFeature(final int version, final String featureId) {
        setContextFeatureId(featureId);
        userViewsFeaturesLog(featureId);
        final ScriptObjectMirror revision = revisions().get(abs(version));
        final String id = (String) revision.get("id");
        contextResponse = featureService.getFeatureAtVersion(featureId, id);
        final ScriptObjectMirror featureJson = contextResponse.jsonBody();
        context.featureJson(featureJson);
    }

    @Step("^a user views the log for feature (.+)$")
    public void userViewsFeaturesLog(final String featureId) {
        setContextFeatureId(featureId);
        contextResponse = featureService.getFeatureHistory(context.featureId());
        contextLogJson = contextResponse.jsonBody();
    }

    @Step("^a user views the log for package (.+)$")
    public void userViewsPackageLog(final String packageId) {
        setContextFeatureId(packageId);
        contextResponse = featureService.getPakageHistory(packageId);
        contextLogJson = contextResponse.jsonBody();
    }

    @Step("^a user views the features log$")
    public void userViewsContextFeaturesLog() {
        checkContextFeatureId();
        contextResponse = featureService.getFeatureHistory(context.featureId());
        contextLogJson = contextResponse.jsonBody();
    }

    @Step("^a user views the log$")
    public void userViewsTheLog() {
        checkContextFeatureId();
        final Response response = featureService.getHistory();
        contextLogJson = response.jsonBody();
        System.out.println(response);
        //logJson = response.jsonBody();

        final ScriptObjectMirror revision = revisions().get(0);
        final String revId = asString(revision.get("id"));
        //
        //
        //String revision = asString(response.jsonBody().get("id"));
        //System.out.println("Rev number is '" + revision + "'");
        final Response revisionChangeset = featureService.getRevisionChangeset(revId);
        System.out.println(revisionChangeset);
    }

    // ------------------------------------------------------------- Assertions

    @Step("^on the most recent entry$")
    public void mostRecentEntry() {
        contextRevision = revisions().get(0);
        final String revisionId = asString(contextRevision.get("id"));
        contextChangeset = featureService.getRevisionChangeset(revisionId).jsonBody();
    }

    @Step("^on the most recent entries changeset$")
    public void mostRecentEntriesChangeset() {
        mostRecentEntry();

    }

    @Step("^on the first entry$")
    public void firstEntry() {
        contextRevision = lastItem(revisions());
    }

    @Step("^on the second entry$")
    public void secondEntry() {
        contextRevision = secondLastItem(revisions());
    }

    @Step("^the log must have one entry$")
    public void logMustHaveOneEntry() {
        logMustHaveNEntries(1);
        contextRevision = revisions().get(0);
    }

    @Step("^the log must have (\\d+) entries$")
    public void logMustHaveNEntries(final int nrEntries) {
        assertEquals(nrEntries, revisions().size());
    }

    @Step("^(?:the )?comment must be \"(.*)\"$")
    public void commentMustBe(final String expectedComment) {
        assertEquals(expectedComment, contextRevision.get("comment"));
    }

    @Step("^the author name must be \"(.*)\"$")
    public void authorNameMustBe(final String expectedAuthorName) {
        assertEquals(expectedAuthorName, contextRevision.get("authorName"));
    }

    @Step("^(?:the )?changeset contains (\\w+) (.+)$")
    public void changesetContains(final String expectedEvent, final String expectedFeatureId) {
        final List<ScriptObjectMirror> changeset = asList(contextChangeset);
        for (final ScriptObjectMirror change : changeset) {
            final String event = asString(change.get("event"));
            final String featureId = asString(change.get("id"));
            if (event.equals(expectedEvent) && featureId.equals(expectedFeatureId)) {
                return;
            }
        }
        ChorusAssert.fail("No event for feature id matched");
    }

    @Step("^the user is notified that \"the feature does not exist in this version\"$")
    public void userNotifiedThatFeatureDoesNotExistInThisVersion() {
        assertEquals(NOT_FOUND, contextResponse.statusLine());
    }

    //
    //@Step("^(?:the )?changeset contains \\{(.+)\\}$")
    //public void changesetContains(final String expectedChangesetStr) {
    //    final List<String> expectedChangeset =  parameterList(expectedChangesetStr);
    //    final String revisionId = asString(contextRevision.get("id"));
    //    final Response revisionChangeset = featureService.getRevisionChangeset(revisionId);
    //
    //    System.out.println(revisionChangeset);
    //    final List<String> changeset = asStringList(revisionChangeset.jsonBody());
    //    System.out.println(changeset);
    //
    //    for (final String expectedFeatureId : expectedChangeset) {
    //        ChorusAssert.assertTrue("Changeset does not contain: " + expectedFeatureId, changeset.contains(expectedFeatureId));
    //    }
    //}

    // -------------------------------------------------------- Private Methods

    private void setContextFeatureId(final String featureId) {
        context.featureId(featureId);
        context.featureJson(null);
    }

    private void checkContextFeatureId() {
        if (context.featureId() == null) {
            throw new RuntimeException("needs a context featureId");
        }
    }

    private List<ScriptObjectMirror> revisions() {
        return asList(contextLogJson);
    }

    private <T> T lastItem(final List<T> l) {
        return l.get(l.size() - 1);
    }

    private <T> T secondLastItem(final List<T> l) {
        return l.get(l.size() - 2);
    }

    private List<String> parameterList(final String changeset) {
        final List<String> result = new LinkedList<>();
        for (final String token : changeset.split(",")) {
            result.add(token.trim());
        }
        return result;
    }
}

