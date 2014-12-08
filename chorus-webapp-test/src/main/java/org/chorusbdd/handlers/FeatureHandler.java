package org.chorusbdd.handlers;

import org.chorusbdd.FeatureService;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.util.assertion.ChorusAssert;
import org.chorusbdd.experimental.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.io.IOException;

import static org.chorusbdd.HttpUtil.*;
import static org.chorusbdd.JsonUtil.asString;
import static org.chorusbdd.chorus.util.assertion.ChorusAssert.*;

@SuppressWarnings("UnusedDeclaration")
@Handler("Chorus Feature")
public class FeatureHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FeatureHandler.class);
    private static final String DEFAULT_FEATURE_TEXT = "Lorem ipsum dolor sit amet.";
    private static final TestContext context = new TestContext();

    private FeatureService featureService = new FeatureService();
    private Response contextResponse;

    public FeatureHandler() {
        // do nothing
    }

    // ------------------------------------------------------------------ Given

    @Step("^the feature (.+) exists$")
    public void featureExists(final String featureId) {
        setContextFeatureId(featureId);
        if (!featureService.featureExists(featureId)) {
            createFeature(featureId, DEFAULT_FEATURE_TEXT);
        }
    }

    @Step("^the feature (.+) has the text \"(.*)\"$")
    public void featureHasTheText(final String featureId, final String featureText) {
        setContextFeatureId(featureId);
        createFeature(featureId, featureText);
    }

    @Step("^the feature was modified with the text \"(.*)\"$")
    public void featureIsModifiedWithTheText(final String featureText) {
        requiresContextFeatureId();
        modifyFeature(context.featureId(), featureText);
    }

    @Step("^a user (?:[A-Za-z]+ )?has opened the feature for editing$")
    public void userHasOpenedAFeatureForEditing() {
        retrieveContextFeature();
    }

    @Step("^the feature (.+) was deleted$")
    public void featureWasDeleted(final String featureId) {
        setContextFeatureId(featureId);
        deleteFeature(featureId);
    }

    @Step("^the feature was deleted$")
    public void contextFeatureWasDeleted() {
        requiresContextFeatureId();
        deleteFeature(context.featureId());
    }

    @Step("^the feature was moved to (.+)$")
    public void contextFeatureWasMovedTo(final String destinationId) {
        requiresContextFeatureId();
        moveFeature(context.featureId(), destinationId);
    }

    @Step("^the feature (.+) was moved to (.+)$")
    public void featureWasMovedTo(final String targetId, final String destinationId) {
        moveFeature(targetId, destinationId);
    }

    // ------------------------------------------------------------------- When

    @Step("^a user creates the feature (.+) with the text \"(.*)\"$")
    public void userCreatesAFeatureWithTheText(final String featureId, final String featureText) {
        setContextFeatureId(featureId);
        createFeature(featureId, featureText);
    }

    @Step("^a user modifies the feature (.+) text with \"(.*)\"$")
    public void featureIsModifiedWithTheText(final String featureId, final String featureText) {
        setContextFeatureId(featureId);
        modifyFeature(featureId, featureText);
    }

    @Step("^a (?:different )?user modifies the feature text with \"(.*)\"$")
    public void contextFeatureIsModifiedWithTheText(final String featureText) {
        requiresContextFeatureId();
        modifyFeature(context.featureId(), featureText);
    }

    @Step("^[A-Za-z]+ submits her edit with the text \"(.*)\"$")
    public void contextFeatureEditSubmittedWithTheText(final String featureText) {
        requiresContextFeatureId();
        contextResponse = featureService.putFeature(context.featureId(), featureText, asString(context.featureJson().get("md5")));
    }

    @Step("^a (?:different )?user deletes the feature (.+)$")
    public void featureIsDeleted(final String featureId) {
        setContextFeatureId(featureId);
        deleteFeature(featureId);
    }

    @Step("^a (?:different )?user deletes the feature$")
    public void contextFeatureIsDeleted() throws IOException, ScriptException {
        requiresContextFeatureId();
        deleteFeature(context.featureId());
    }

    @Step("^a user moves the feature to (.+)$")
    public void userMovesTheFeature(final String destinationId) {
        requiresContextFeatureId();
        moveFeature(context.featureId(), destinationId);
    }

    // ------------------------------------------------------------- Assertions

    @Step("^[A-Za-z]+ must be presented with a Modification Conflict Error$")
    public void featureMustHaveModificationConflictError()  {
        requiresContextResponse();
        assertEquals(CONFLICT, contextResponse.statusLine());
    }

    @Step("^the feature (.+) must exist$")
    public void featureMustExist(final String featureId) {
        setContextFeatureId(featureId);
        final Response response = featureService.getFeature(featureId);
        assertEquals(OK, response.statusLine());
    }

    @Step("^the feature (.+) must not exist$")
    public void featureMustNotExist(final String featureId)  {
        setContextFeatureId(featureId);
        final Response response = featureService.getFeature(featureId);
        assertEquals(NOT_FOUND, response.statusLine());
    }

    @Step("^the feature (.+) must have the text \"(.*)\"$")
    public void featureMustHaveTheText(final String featureId, final String expectedText) {
        setContextFeatureId(featureId);
        retrieveContextFeature();
        assertEquals(expectedText, asString(context.featureJson().get("text")));
    }

    @Step("^the feature must have the text \"(.*)\"$")
    public void contextFeatureMustHaveTheText(final String expectedText) {
        retrieveContextFeature();
        assertEquals(expectedText, asString(context.featureJson().get("text")));
    }

    @Step("^must have the name \"(.+)\"$")
    public void mustHaveTheName(final String expectedName) {
        retrieveContextFeature();
        assertEquals(expectedName, asString(context.featureJson().get("name")));
    }

    @Step("^must have the package \"(.*)\"$")
    public void mustHaveThePackage(final String expectedPakage) {
        retrieveContextFeature();
        assertEquals(expectedPakage, asString(context.featureJson().get("packageId")));
    }

    // -------------------------------------------------------- Private Methods

    private void setContextFeatureId(final String featureId) {
        context.featureId(featureId);
        context.featureJson(null);
    }

    private void retrieveContextFeature() {
        requiresContextFeatureId();
        if (context.featureJson() == null) {
            context.featureJson(featureService.getFeature(context.featureId()).jsonBody());
        }
    }

    private void createFeature(final String featureId, final String featureText) {
        final Response response = featureService.putFeature(featureId, featureText);
        assertEquals(CREATED, response.statusLine());
    }

    private void modifyFeature(final String featureId, final String featureText) {
        final Response response = featureService.modifyFeature(featureId, featureText);
        assertEquals(CREATED, response.statusLine());
    }

    private void deleteFeature(final String featureId) {
        final Response response = featureService.deleteFeature(featureId);
        assertEquals(NO_CONTENT, response.statusLine());
    }

    private void moveFeature(final String targetFeatureId, final String destinationFeatureId) {
        final Response response = featureService.moveFeature(targetFeatureId, destinationFeatureId);
        assertEquals(OK, response.statusLine());
    }

    private void requiresContextFeatureId() {
        if (context.featureId() == null) {
            throw new RuntimeException("needs a context featureId");
        }
    }

    private void requiresContextResponse() {
        if (contextResponse == null) {
            throw new RuntimeException("Must have response in context to assert");
        }
    }
}

