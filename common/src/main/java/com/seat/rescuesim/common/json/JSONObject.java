package com.seat.rescuesim.common.json;

/** An interface for classes that support a JSON Object representation. */
public interface JSONObject {

    /** Returns the boolean value associated with the key.
     * @throws JSONException if the value associated with the key cannot be converted to a boolean or no such key
     */
    boolean getBoolean(String key) throws JSONException;

    /** Returns the double value associated with the key.
     * @throws JSONException if the value associated with the key cannot be converted to a double or no such key
     */
    double getDouble(String key) throws JSONException;

    /** Returns the int value associated with the key.
     * @throws JSONException if the value associated with the key cannot be converted to an int or no such key
     */
    int getInt(String key) throws JSONException;

    /** Returns an Object associated with the key that implements the JSONArray interface.
     * @throws JSONException if the value associated with the key cannot be converted to a JSONArray or no such key
     */
    JSONArray getJSONArray(String key) throws JSONException;

    /** Returns an Object associated with the key that implements the JSONObject interface.
     * @throws JSONException if the value associated with the key cannot be converted to a JSONObject or no such key
     */
    JSONObject getJSONObject(String key) throws JSONException;

    /** Returns a JSONOption wrapper for the Object associated with the key.
     * @throws JSONException if the value associated with the key cannot be converted to a JSONOption or no such key
     */
    JSONOption getJSONOption(String key) throws JSONException;

    /** Returns the long value associated with the key.
     * @throws JSONException if the value associated with the key cannot be converted to a long or no such key
     */
    long getLong(String key) throws JSONException;

    /** Returns the String representation of the value associated with the key.
     * @throws JSONException if the Object has no such key
     */
    String getString(String key) throws JSONException;

    /** Returns true if the JSONObject contains the specified key. */
    boolean hasKey(String key);

    /** Returns the number of key-value pairs stored in the JSONObject. */
    int size();

    /** Returns the String representation of the JSONObject.
     * @throws JSONException if the JSONObject cannot be converted to a JSON string
     */
    String toString() throws JSONException;

    /** Returns the String representation of the JSONObject (pretty printed).
     * @throws JSONException if the JSONObject cannot be converted to a JSON string
     */
    String toString(int tabSize) throws JSONException;

}
