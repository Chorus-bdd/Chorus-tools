package org.chorusbdd.handlers;

import org.chorusbdd.FeatureService;
import org.chorusbdd.HttpUtil;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.util.assertion.ChorusAssert;
import org.chorusbdd.experimental.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.io.IOException;

import static org.chorusbdd.HttpUtil.*;

@Handler("Chorus Package")
@SuppressWarnings("UnusedDeclaration")
public class PakageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(PakageHandler.class);

    private FeatureService featureService = new FeatureService();

    public PakageHandler() {
        // do nothing
    }

    // ------------------------------------------------------------------ Given

    @Step("^the package (.+) exists$")
    public void packageExists(final String packageId) {
        final Response response = featureService.putPakage(packageId);
        ChorusAssert.assertEquals(CREATED, response.statusLine());
    }

    @Step("^the package (.+) was moved to (.+)$")
    public void packageWasMoved(final String targetPackageId, final String destPackageId) {
        featureService.movePakage(targetPackageId, destPackageId);
    }

    @Step("^the package (.+) was deleted$")
    public void packageWasDeleted(final String packageId) {
        final Response response = featureService.deletePakage(packageId);
        ChorusAssert.assertEquals(NO_CONTENT, response.statusLine());
    }

    // ------------------------------------------------------------------- When

    @Step("^a user moves the package (.+) to (.+)$")
    public void userMovesPackage(final String targetPackageId, final String destPackageId) {
        featureService.movePakage(targetPackageId, destPackageId);
    }

    @Step("^a user deletes the package (.+)$")
    public void userDeletesPackage(final String packageId) {
        final Response response = featureService.deletePakage(packageId);
        ChorusAssert.assertEquals(NO_CONTENT, response.statusLine());
    }

    // ------------------------------------------------------------- Assertions

    @Step("^the package (.+) must exist$")
    public void pakageMustExists(final String packageId) {
        final Response response = featureService.getPakage(packageId);
        ChorusAssert.assertEquals(OK, response.statusLine());
    }

    @Step("^the package (.+) must not exist$")
    public void pakageMustNotExist(final String packageId) {
        final Response response = featureService.getPakage(packageId);
        ChorusAssert.assertEquals(NOT_FOUND, response.statusLine());
    }
}

