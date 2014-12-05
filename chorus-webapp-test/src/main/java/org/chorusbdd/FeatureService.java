package org.chorusbdd;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.chorusbdd.experimental.HttpServiceImpl;
import org.chorusbdd.experimental.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.chorusbdd.experimental.RequestBuilder.ContentType.APPLICATION_URL_ENCODED;

public class FeatureService {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureService.class);

    private final HttpServiceImpl httpService;
    private final String baseUrl;

    public FeatureService() {
        httpService = new HttpServiceImpl();
        baseUrl = "http://localhost:8085";
    }

    // ---------------------------------------------------------------- T---est

    public boolean isRunning() {
        try {
            final Response response = httpService.get().withUri(baseUrl + "/resource/status").execute();
            //System.out.println(response);
            return "\"OK\"".equals(response.body());
        } catch (Throwable e) {
            return false;
        }
    }

    // --------------------------------------------------------------- Packages

    public Response getPakage(final String pakageName) {
        return httpService.get().withUri(baseUrl + "/resource/packages/" + pakageName).execute();
    }

    public Response putPakage(final String pakageName) {
        return httpService.put()
                .withUri(baseUrl + "/resource/packages/" + pakageName)
                .withContentType(APPLICATION_URL_ENCODED)
                .execute();
    }

    public Response getPakageHistory(final String pakageId) {
        return httpService.get().withUri(baseUrl + "/resource/log/packages/" + pakageId).execute();
    }

    public Response movePakage(final String targetPackageId, final String destPackageId) {
        return httpService.put()
                .withUri(baseUrl + "/resource/packages/" + targetPackageId + "/move")
                .withContentType(APPLICATION_URL_ENCODED)
                .withEntity("to=" + destPackageId)
                .execute();
    }

    public Response deletePakage(final String packageId) {
        return httpService.delete().withUri(baseUrl + "/resource/packages/" + packageId).execute();
    }

    // --------------------------------------------------------------- Features

    public Response getFeature(final String featureId) {
        return httpService.get().withUri(baseUrl + "/resource/features/" + featureId).execute();
    }

    public Response putFeature(final String featureId, final String featureText) {
        final Response response = httpService.put()
                .withUri(baseUrl + "/resource/features/" + featureId)
                .withContentType(APPLICATION_URL_ENCODED)
                .withEntity("text=" + featureText)
                .execute();
        LOG.debug("Put feature {} got http response: {}", featureId, response);
        return response;
    }

    public Response putFeature(final String featureId, final String featureText, final String md5) {
        final Response response = httpService.put()
                .withUri(baseUrl + "/resource/features/" + featureId)
                .withContentType(APPLICATION_URL_ENCODED)
                .withEntity("text=" + featureText)
                .withHeader("If-Match", md5)
                .execute();
        LOG.debug("Put feature {} got http response: {}", featureId, response);
        return response;
    }

    public boolean featureExists(final String featureId) {
        return "200 OK".equals(getFeature(featureId).statusLine());
    }

    public Response modifyFeature(final String featureId, final String featureText) {
        final Response response = getFeature(featureId);
        final ScriptObjectMirror json = response.jsonBody();
        final String md5 = String.valueOf(json.get("md5"));
        return putFeature(featureId, featureText, md5);
    }

    public Response deleteFeature(final String featureId) {
        return httpService.delete().withUri(baseUrl + "/resource/features/" + featureId).execute();
    }

    public Response moveFeature(final String targetFeatureId, final String destinationFeatureId) {
        return httpService.put()
                .withUri(baseUrl + "/resource/features/" + targetFeatureId + "/move")
                .withContentType(APPLICATION_URL_ENCODED)
                .withEntity("to=" + destinationFeatureId)
                .execute();
    }

    // ---------------------------------------------------------------- History

    public Response getFeatureAtVersion(final String featureId, final String id) {
        return httpService.get().withUri(baseUrl + "/resource/features/" + featureId + "/" + id).execute();
    }

    public Response getFeatureHistory(final String featureId) {
        return httpService.get().withUri(baseUrl + "/resource/log/features/" + featureId).execute();
    }

    public Response getRevisionChangeset(final String revision) {
        return httpService.get().withUri(baseUrl + "/resource/log/" + revision + "/changeset").execute();
    }

    public Response getHistory() {
        return httpService.get().withUri(baseUrl + "/resource/log").execute();
    }

}
