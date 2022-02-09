package com.seat.rescuesim.common.json;

/** An interface for classes that support a JSON Array representation. */
public interface JSONArray {

    /** Returns the double value at index idx.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to a double
     */
    double getDouble(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns the int value at index idx.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to an int
     */
    int getInt(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns an Object at index idx that implements the JSONArray interface.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to a JSONArray
     */
    JSONArray getJSONArray(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns an Object at index idx that implements the JSONObject interface.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to a JSONObject
     */
    JSONObject getJSONObject(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns a JSONOption wrapper for the Object at index idx.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to a JSONOption
     */
    JSONOption getJSONOption(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns the String representation of the value at index idx.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     */
    String getString(int idx) throws IndexOutOfBoundsException;

    /** Returns the String representation of the JSONArray. */
    String toString();

}
