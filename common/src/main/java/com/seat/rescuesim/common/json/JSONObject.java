package com.seat.rescuesim.common.json;

/** An interface for classes that support a JSON Object representation. */
public interface JSONObject {

    /** Returns the double value associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to a double
     */
    public double getDouble(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns the int value associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to an int
     */
    public int getInt(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns an Object associated with the key that implements the JSONArray interface.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to a JSONArray
     */
    public JSONArray getJSONArray(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns an Object associated with the key that implements the JSONObject interface.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to a JSONObject
     */
    public JSONObject getJSONObject(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns a JSONOption wrapper for the Object associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     * @throws JSONException if the value associated with the key cannot be converted to a JSONOption
     */
    public JSONOption getJSONOption(String key) throws IndexOutOfBoundsException, JSONException;

    /** Returns the String representation of the value associated with the key.
     * @throws IndexOutOfBoundsException if the Object has no such key
     */
    public String getString(String key) throws IndexOutOfBoundsException;

    /** Returns the String representation of the JSONObject. */
    public String toString();

}
