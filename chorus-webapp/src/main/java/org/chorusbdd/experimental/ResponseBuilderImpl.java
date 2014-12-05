package org.chorusbdd.experimental;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.LinkedList;
import java.util.List;

@NotThreadSafe
public class ResponseBuilderImpl implements ResponseBuilder {
    private final List<Header> headers = new LinkedList<>();
    private int statusCode = -2;
    private String body = "";
    private String message;

    @Override
    public ResponseBuilder withHeader(final String name, final String value) {
        headers.add(new Header() {
            public String name()  { return name;  }
            public String value() { return value; }
        });
        return this;
    }

    @Override
    public ResponseBuilder withStatus(final int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @Override
    public ResponseBuilder withBody(final String body) {
        this.body = body;
        return this;
    }

    @Override
    public ResponseBuilder withMessage(final String message) {
        this.message = message;
        return this;
    }

    @Override
    public Response create() {
        return new ResponseImpl(statusCode, message, headers, body);
    }


}
