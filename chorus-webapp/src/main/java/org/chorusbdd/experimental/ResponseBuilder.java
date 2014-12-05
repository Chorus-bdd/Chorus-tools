package org.chorusbdd.experimental;

public interface ResponseBuilder {

    ResponseBuilder withHeader(final String name, final String value);
    ResponseBuilder withStatus(int statusCode);
    ResponseBuilder withBody(String body);
    ResponseBuilder withMessage(String message);

    Response create();

}
