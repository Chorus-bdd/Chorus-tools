package org.chorusbdd.experimental;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class ResponseImpl implements Response {

    private final int statusCode;
    private final List<Header> headers;
    private final String body;
    private final String message;

    public ResponseImpl(final int statusCode, final String message, final List<Header> headers, final String bodyText) {
        this.statusCode = notNull(statusCode);
        this.message = notNull(message);
        this.headers = notNull(headers);
        this.body = notNull(bodyText);
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String statusLine() {
        return statusCode + " " + message;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public ScriptObjectMirror jsonBody() {
        try {
            final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            return (ScriptObjectMirror)engine.eval("(" + body + ")");
        } catch (ScriptException e) {
            throw new RuntimeException("Could not parse JSON response for message: " + toString());
        }
    }

    @Override
    public String header(final String headerName) {
        final Header header = headers.stream().filter(h-> headerName.equalsIgnoreCase(h.name())).findFirst().get();
        if (header == null) {
            return null;
        }
        return header.value();
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(statusCode).append(" ").append(message).append("\n");
        for (Header header : headers) {
            result.append(header.name()).append(": ").append(header.value()).append("\n");
        }
        result.append("----------------------------------------\n");
        result.append(body);
        result.append("\n========================================\n");
        return result.toString();
    }
}

/*

    Scenario:
        Set HTTP base url localhost:8080


    Given the feature Foo.Bar.My_Feature does not exist
    When the feature Foo.Bar.My_Feature is created with the text "foo bar far"
    Then ensure the package Foo.Bar contains the feature My_Feature
     And ensure the feature Foo.Bar.My_Feature has the text "foo bar far"


    # ----------------------------------------------------------------- MACRO's

    Step Macro: the feature <feature> does not exist
        Execute Http GET /resource/features/<feature>
        Then ensure the response status code is not 200

    Step Macro: the feature <feature> is created with the text <feature-text>
        Execute Http PUT /resource/features/<feature> <feature-text>
        Then ensure the response status code is 200

    Step Macro: ensure the package <package> contains the feature <feature>
        Execute Http GET /resource/packages/<package>
        Then ensure the response body contains feature <feature>

    Step Macro: ensure the feature <feature> has the text <feature-text>
        Execute Http GET /resource/features/<feature>
        Then ensure the response status code is 200
        And ensure the response content-type header is text/html
        And ensure the response body is <feature-text>







*/