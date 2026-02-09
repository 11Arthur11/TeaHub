package dev.parhamziaei.teahub.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.nio.charset.StandardCharsets;

public class UriUtils {

//    public static String encodeURIComponent(String uri) {
//        try {
//            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
//            ScriptEngine scriptEngine = scriptEngineManager
//                    .getEngineByName("JavaScript");
//
//            return String.valueOf(scriptEngine
//                    .eval("encodeURIComponent('" + uri + "')"));
//        } catch (ScriptException e) {
//            return uri;
//        }
//    }



    /**
     * Encode a string like JavaScript encodeURIComponent
     * Safe for use in URL path
     */
    public static String encodeURIComponent(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (isUnreserved(c)) {
                result.append(c);
            } else {
                byte[] bytes = String.valueOf(c).getBytes(StandardCharsets.UTF_8);
                for (byte b : bytes) {
                    result.append(String.format("%%%02X", b));
                }
            }
        }
        return result.toString()
                .replace("(", "%28")
                .replace(")", "%29");
    }

    /**
     * Checks if character is unreserved (ALPHA / DIGIT / - _ . ! ~ * ' ( ) )
     * Same as JS encodeURIComponent
     */
    private static boolean isUnreserved(char c) {
        return (c >= 'A' && c <= 'Z') ||
                (c >= 'a' && c <= 'z') ||
                (c >= '0' && c <= '9') ||
                c == '-' || c == '_' || c == '.' || c == '!' ||
                c == '~' || c == '*' || c == '\'' || c == '(' || c == ')';
    }

}
