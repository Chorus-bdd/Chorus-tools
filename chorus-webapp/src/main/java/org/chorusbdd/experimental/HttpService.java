package org.chorusbdd.experimental;

public interface HttpService {
    RequestBuilder get();
    RequestBuilder post();
    RequestBuilder put();
    RequestBuilder delete();
}
