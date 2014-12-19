package org.chorusbdd.experimental;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class RequestBuilderImpl implements RequestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestBuilderImpl.class);

    private final List<Header> headers = new LinkedList<>();
    private Action action;
    private ContentType type;
    private String entity;
    private String uri;

    @Override
    public RequestBuilder withAction(final Action action) {
        this.action = action;
        return this;
    }

    @Override
    public RequestBuilder withContentType(final ContentType type) {
        this.type = type;
        return this;
    }

    @Override
    public RequestBuilder withEntity(final String entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public RequestBuilder withHeader(final String name, final String value) {
        this.headers.add(new Header() {
            public String name()  { return name;  }
            public String value() { return value; }
        });
        return this;
    }

    @Override
    public RequestBuilder withUri(final String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public Request create() {
        validate();
        return () -> doHttpRequest(httpUriRequest());
    }

    @Override
    public Response execute() {
        return create().execute();
    }

    private void validate() {
        notEmpty(uri, "URI must not be empty");
        notNull(action, "Action must not be null");
        if (action == Action.PUT || action == Action.POST) {
            notNull(type, "Type must not be null");
        }
    }

    private HttpUriRequest httpUriRequest() {
        switch (action) {
            case GET: return new HttpGet(uri);
            case DELETE: return new HttpDelete(uri);
            case POST: return setContent(new HttpPost(uri));
            case PUT:  return setContent(new HttpPut(uri));
            default: throw new RuntimeException("unknown action");
        }
    }

    private String contentType() {
        switch (type) {
            case APPLICATION_URL_ENCODED: return "application/x-www-form-urlencoded";
            default: throw new RuntimeException("unknown content type");
        }
    }

    private Response doHttpRequest(final HttpUriRequest request) {
        for (final Header header : headers) {
            request.setHeader(header.name(), header.value());
        }

        final DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            //System.out.println(toString(request));
            //LOG.trace(toString(request));
            final HttpResponse httpResponse = httpClient.execute(request);
            LOG.debug("http {} '{}' responded with '{}'", new Object[] {request.getMethod(), request.getURI(), httpResponse.getStatusLine()});
            return parseResponse(httpResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private HttpEntityEnclosingRequestBase setContent(final HttpEntityEnclosingRequestBase request) {
        if (entity == null) {
            return request;
        }
        try {
            final StringEntity input = new StringEntity(entity);
            input.setContentType(contentType());
            request.setEntity(input);
            return request;
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private Response parseResponse(final HttpResponse httpResponse) throws IOException {
        final HttpEntity entity = httpResponse.getEntity();
        final ResponseBuilder response = new ResponseBuilderImpl();
        response.withStatus(httpResponse.getStatusLine().getStatusCode());
        for (final org.apache.http.Header header : httpResponse.getAllHeaders()) {
            response.withHeader(header.getName(), header.getValue());
        }
        response.withMessage(httpResponse.getStatusLine().getReasonPhrase());
        if (entity != null) {
            response.withBody(EntityUtils.toString(entity));
        }
        return response.create();
    }

    private String toString(final HttpUriRequest request) {
        final StringBuilder result = new StringBuilder();
        result.append("========================================\n");
        result.append(request.getMethod()).append(" ").append(request.getURI()).append("\n");
        for (org.apache.http.Header header : request.getAllHeaders()) {
            result.append(header.getName()).append(": ").append(header.getValue()).append("\n");
        }
        result.append("----------------------------------------\n");
        result.append(request.getParams());
        result.append("\n========================================\n");
        return result.toString();
    }
}