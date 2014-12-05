package org.chorusbdd.handlers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.chorusbdd.chorus.core.interpreter.ChorusContext;

public class TestContext {
    private static final ChorusContext context = ChorusContext.getContext();

    public String featureId() {
        return (String)context.get("featureId");
    }

    public void featureId(String featureId) {
        context.put("featureId", featureId);
    }

    public ScriptObjectMirror featureJson() {
        return (ScriptObjectMirror)context.get("featureJson");
    }

    public void featureJson(ScriptObjectMirror featureJson) {
        context.put("featureJson", featureJson);
    }
}
