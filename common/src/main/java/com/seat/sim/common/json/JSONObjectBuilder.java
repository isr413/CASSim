package com.seat.sim.common.json;

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
    void put(String key, float value) throws JSONException;

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
    void put(String key, JSONOptional value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
     */
    void put(String key, Object value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
     */
    void put(String key, long value) throws JSONException;

    /** Inserts the key-value pair into the JSONObject.
     * @throws JSONException if JSON does not support the input value
     */
    void put(String key, String value) throws JSONException;

    /** Returns the JSONOptional representation of the JSONObject. */
    JSONOptional toJSON();

    /** Returns the String serialization of the JSONObject.
     * @throws JSONException if the JSONObject cannot be serialized
     */
    String toString() throws JSONException;

    /** Returns the String serialization of the JSONObject (pretty printed).
     * @throws JSONException if the JSONObject cannot be serialized
     */
    String toString(int tabSize) throws JSONException;

}
