package com.seat.sim.common.json;

/** An interface for classes that support a JSON Array representation. */
public interface JSONArrayBuilder {

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(boolean value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(double value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(float value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(int value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(JSONArray value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(JSONObject value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(JSONOptional value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(Object value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(long value) throws JSONException;

    /** Appends the value to the JSONArray.
     * @throws JSONException if JSON does not support the input value
     */
    void put(String value) throws JSONException;

    /** Returns the JSONOptional representation of the JSONArray. */
    JSONOptional toJSON();

    /** Returns the String serialization of the JSONArray.
     * @throws JSONException if the JSONArray cannot be serialized
     */
    String toString() throws JSONException;

    /** Returns the String serialization of the JSONArray (pretty printed).
     * @throws JSONException if the JSONArray cannot be serialized
     */
    String toString(int tabSize) throws JSONException;

}
