package com.seat.sim.common.json;

public class JSONParser {

    private static boolean isQuoted(String encoding) {
        if (encoding == null || encoding.length() < 2) return false;
        return (encoding.charAt(0) == '"' && encoding.charAt(encoding.length()-1) == '"') ||
            (encoding.charAt(0) == '\'' && encoding.charAt(encoding.length()-1) == '\'');
    }

    private static String trimQuotesFromEncoding(String encoding) {
        if (encoding == null || encoding.isEmpty() || encoding.isBlank()) return encoding;
        if (JSONParser.isQuoted(encoding)) {
            return JSONParser.trimQuotesFromEncoding(encoding.substring(1, encoding.length()-1));
        }
        return encoding;
    }

    /** Returns true if the input string is a JSON encoding. */
    public static boolean isEncodedJSON(String encoding) {
        return JSONParser.isEncodedJSONArray(encoding) || JSONParser.isEncodedJSONObject(encoding);
    }

    /** Returns true if the input string is a JSON Array encoding. */
    public static boolean isEncodedJSONArray(String encoding) {
        encoding = JSONParser.trimEncoding(encoding);
        if (encoding == null || encoding.length() < 2) return false;
        return encoding.charAt(0) == '[' && encoding.charAt(encoding.length()-1) == ']';
    }

    /** Returns true if the input string is a JSON Object encoding. */
    public static boolean isEncodedJSONObject(String encoding) {
        encoding = JSONParser.trimEncoding(encoding);
        if (encoding == null || encoding.length() < 2) return false;
        return encoding.charAt(0) == '{' && encoding.charAt(encoding.length()-1) == '}';
    }

    /** Removes leading and trailing whitespace and quotes. */
    public static String trimEncoding(String encoding) {
        if (encoding == null || encoding.isEmpty() || encoding.isBlank()) return encoding;
        return JSONParser.trimQuotesFromEncoding(encoding.trim());
    }

}
