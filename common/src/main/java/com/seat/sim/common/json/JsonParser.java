package com.seat.sim.common.json;

/** A helper class to support parsing encoded Json. */
public class JsonParser {

    private static boolean isQuoted(String encoding) {
        if (encoding == null || encoding.length() < 2) return false;
        return (encoding.charAt(0) == '"' && encoding.charAt(encoding.length()-1) == '"') ||
            (encoding.charAt(0) == '\'' && encoding.charAt(encoding.length()-1) == '\'');
    }

    private static String trimQuotesFromEncoding(String encoding) {
        if (encoding == null || encoding.isEmpty() || encoding.isBlank()) return encoding;
        if (JsonParser.isQuoted(encoding)) {
            return JsonParser.trimQuotesFromEncoding(encoding.substring(1, encoding.length()-1));
        }
        return encoding;
    }

    /** Returns {@code true} if the input string is a Json encoding. */
    public static boolean isEncodedJson(String encoding) {
        return JsonParser.isEncodedJsonArray(encoding) || JsonParser.isEncodedJsonObject(encoding);
    }

    /** Returns {@code true} if the input string is a Json Array encoding. */
    public static boolean isEncodedJsonArray(String encoding) {
        encoding = JsonParser.trimEncoding(encoding);
        if (encoding == null || encoding.length() < 2) return false;
        return encoding.charAt(0) == '[' && encoding.charAt(encoding.length()-1) == ']';
    }

    /** Returns {@code true} if the input string is a Json Object encoding. */
    public static boolean isEncodedJsonObject(String encoding) {
        encoding = JsonParser.trimEncoding(encoding);
        if (encoding == null || encoding.length() < 2) return false;
        return encoding.charAt(0) == '{' && encoding.charAt(encoding.length()-1) == '}';
    }

    /** Removes leading and trailing whitespace and quotes. */
    public static String trimEncoding(String encoding) {
        if (encoding == null || encoding.isEmpty() || encoding.isBlank()) return encoding;
        return JsonParser.trimQuotesFromEncoding(encoding.trim());
    }
}
