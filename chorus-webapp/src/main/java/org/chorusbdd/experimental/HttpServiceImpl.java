package org.chorusbdd.experimental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.chorusbdd.experimental.RequestBuilder.Action.DELETE;
import static org.chorusbdd.experimental.RequestBuilder.Action.GET;
import static org.chorusbdd.experimental.RequestBuilder.Action.POST;
import static org.chorusbdd.experimental.RequestBuilder.Action.PUT;

public class HttpServiceImpl implements HttpService {

    @Override
    public RequestBuilder get() {
        return new RequestBuilderImpl().withAction(GET);
    }

    @Override
    public RequestBuilder post() {
        return new RequestBuilderImpl().withAction(POST);
    }

    @Override
    public RequestBuilder put() {
        return new RequestBuilderImpl().withAction(PUT);
    }

    @Override
    public RequestBuilder delete() {
        return new RequestBuilderImpl().withAction(DELETE);
    }


    //
    //@Override
    //public Response get(final String URI) {
    //    return doHttpRequest(new HttpGet(URI));
    //}
    //
    //@Override
    //public Response put(final String URI, final String contentType, final String content) {
    //    final HttpPut put = new HttpPut(URI);
    //    setContent(contentType, content, put);
    //    return doHttpRequest(put);
    //}
    //
    //@Override
    //public Response post(final String URI, final String contentType, final String content) {
    //    final HttpPost post = new HttpPost(URI);
    //    setContent(contentType, content, post);
    //    return doHttpRequest(post);
    //}
    //
    //@Override
    //public Response delete(final String URI) {
    //    return doHttpRequest(new HttpDelete(URI));
    //}
    //
    //
    //private void setContent(final String contentType, final String content, final HttpEntityEnclosingRequestBase request) {
    //    try {
    //        final StringEntity input = new StringEntity(content);
    //        input.setContentType(contentType);
    //        request.setEntity(input);
    //    } catch (final UnsupportedEncodingException e) {
    //        throw new RuntimeException(e);
    //    }
    //}
    //
    //private Response doHttpRequest(final HttpUriRequest request) {
    //    final DefaultHttpClient httpClient = new DefaultHttpClient();
    //    try {
    //        final HttpResponse httpResponse = httpClient.execute(request);
    //        LOG.info("http {} '{}' responded with '{}'", new Object[] {request.getMethod(), request.getURI(), httpResponse.getStatusLine()});
    //        return parseResponse(httpResponse);
    //    } catch (IOException e) {
    //        LOG.error("Error processing http response for URI '" + request.getURI() + "'", e);
    //        throw new RuntimeException(e);
    //    } finally {
    //        httpClient.getConnectionManager().shutdown();
    //    }
    //}
    //
    //private Response parseResponse(final HttpResponse httpResponse) throws IOException {
    //    final HttpEntity entity = httpResponse.getEntity();
    //    final ResponseBuilder response = new ResponseBuilderImpl();
    //    response.withStatus(httpResponse.getStatusLine().getStatusCode());
    //    for (final Header header : httpResponse.getAllHeaders()) {
    //        response.withHeader(header.getName(), header.getValue());
    //    }
    //    response.withMessage(httpResponse.getStatusLine().getReasonPhrase());
    //    if (entity != null) {
    //        response.withBody(EntityUtils.toString(entity));
    //    }
    //    return response.create();
    //}

    public static void main(String ... args) {
        //"application/x-www-form-urlencoded"
        // "foo=bar&far=sar"
        //final Response response = new HttpServiceImpl().get("http://ibportal.intranet.commerzbank.com/home.aspx");
        //System.out.println(response);
    }

}
