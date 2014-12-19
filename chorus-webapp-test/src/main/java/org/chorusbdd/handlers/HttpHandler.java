package org.chorusbdd.handlers;

import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.util.assertion.ChorusAssert;
import org.chorusbdd.experimental.HttpServiceImpl;
import org.chorusbdd.experimental.Response;

@Handler("Http Handler")
public class HttpHandler {

    //private final HttpServiceImpl httpService;
    //private final String baseUrl;
    //private Response response;
    //
    //public HttpHandler() {
    //    httpService = new HttpServiceImpl();
    //    baseUrl = "http://localhost:8085";
    //}
    //
    //@Step("http GET ([^\\s]+)")
    //public void get(final String resource) {
    //    response = httpService.get(baseUrl + resource);
    //    System.out.println(response);
    //}
    //
    //@Step("http PUT ([^\\s]+) (.*)")
    //public void put(final String resource, final String content) {
    //    response = httpService.put(baseUrl + resource, "application/x-www-form-urlencoded", "text=foobar");
    //    System.out.println(response);
    //}
    //
    //@Step("http POST ([^\\s]+) (.*)")
    //public void post(final String resource, final String content) {
    //    response = httpService.post(baseUrl + resource, "application/x-www-form-urlencoded", "text=foobar");
    //    System.out.println(response);
    //}
    //
    //@Step("http DELETE ([^\\s]+) (.*)")
    //public void delete(final String uri) {
    //
    //}
    //
    //@Step("ensure the response status code is (\\d+)")
    //public void ensureTheResponseStatusCodeIs(final int expectedStatus) {
    //    ChorusAssert.assertEquals(expectedStatus, response.statusCode());
    //}
    //
    //@Step("ensure the response status code is not (\\d+)")
    //public void ensureTheResponseStatusCodeIsNot(final int expectedStatus) {
    //    ChorusAssert.assertTrue(expectedStatus != response.statusCode());
    //}
    //
    //@Step("ensure the response body is (.+)")
    //public void ensureTheResponseBodyIs(final String expectedBody) {
    //    ChorusAssert.assertEquals(expectedBody, response.body());
    //}
    //
    //@Step("ensure the response body contains (.+)")
    //public void ensureTheResponseBodyContains(final String expectedBody) {
    //    ChorusAssert.assertTrue(response.body().contains(expectedBody));
    //}
    //
    //@Step("ensure the response content-type header is (.+)")
    //public void ensureTheContentTypeHeaderIs(final int expectedType) {
    //    ChorusAssert.assertEquals(expectedType, response.header("Content-Type"));
    //}
}
