package org.chorusbdd.experimental;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public interface Response {

    int statusCode();
    String statusLine();
    String message();
    String header(String headerName);

    String body();
    ScriptObjectMirror jsonBody();
}
