package com.seat.rescuesim.common.json;

/** An interface for classes that support a JSON Object representation. */
public interface JSONObjectBuilder {

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
    */
    void put(String key, boolean value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
    */
    void put(String key, double value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
    */
    void put(String key, int value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
    */
    void put(String key, JSONArray value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
    */
    void put(String key, JSONObject value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
    */
    void put(String key, JSONOption value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
    */
    void put(String key, long value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
    */
    void put(String key, String value) throws JSONException;

    /** Returns the JSONOption representation of the JSONObject. */
    JSONOption toJSON();

    /** Returns the String representation of the JSONObject.
     * @throws JSONException if the JSONObject cannot be converted to a JSON string
     */
    String toString() throws JSONException;

    /** Returns the String representation of the JSONObject (pretty printed).
     * @throws JSONException if the JSONObject cannot be converted to a JSON string
     */
    String toString(int tabSize) throws JSONException;

}
