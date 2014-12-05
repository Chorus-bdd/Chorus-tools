package org.chorusbdd;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.List;
import java.util.stream.Collectors;

public class JsonUtil {

    public static List<ScriptObjectMirror> asList(final ScriptObjectMirror obj) {
        return obj.values().stream().map((o) -> (ScriptObjectMirror) o).collect(Collectors.toList());
    }

    public static List<String> asStringList(final ScriptObjectMirror obj) {
        return obj.values().stream().map((o) -> (String) o).collect(Collectors.toList());
    }


    public static String asString(Object value) {
        return String.valueOf(value);
    }
}
