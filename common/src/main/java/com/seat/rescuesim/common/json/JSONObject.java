package com.seat.rescuesim.common.json;

/** An interface for classes that support a JSON Object representation. */
public interface JSONObject {

    /** Returns the double value associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to a double
     */
    double getDouble(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns the int value associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to an int
     */
    int getInt(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns an Object associated with the key that implements the JSONArray interface.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to a JSONArray
     */
    JSONArray getJSONArray(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns an Object associated with the key that implements the JSONObject interface.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to a JSONObject
     */
    JSONObject getJSONObject(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns a JSONOption wrapper for the Object associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to a JSONOption
     */
    JSONOption getJSONOption(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns the long value associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to an int
     */
    long getLong(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns the String representation of the value associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     */
    String getString(String key) throws IndexOutOfBoundsException;

    /** Returns true if the JSONObject contains the specified key. */
    boolean hasKey(String key);

    /** Returns the number of key-value pairs stored in the JSONObject. */
    int size();

    /** Returns the String representation of the JSONObject. */
    String toString();

}
