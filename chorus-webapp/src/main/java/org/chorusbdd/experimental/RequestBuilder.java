package org.chorusbdd.experimental;

public interface RequestBuilder {
    enum  Action { GET, POST, PUT, DELETE }
    enum ContentType { APPLICATION_URL_ENCODED };

    RequestBuilder withAction(Action action);
    RequestBuilder withContentType(ContentType type);
    RequestBuilder withEntity(String body);
    RequestBuilder withHeader(String name, String value);
    RequestBuilder withUri(String uri);

    Request create();
    Response execute();
}
