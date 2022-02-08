package com.seat.rescuesim.common.json;

/** An interface for classes that support a JSON Array representation. */
public interface JSONArray {

    /** Returns the double value at index idx.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to a double
     */
    public double getDouble(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns the int value at index idx.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to an int
     */
    public int getInt(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns an Object at index idx that implements the JSONArray interface.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to a JSONArray
     */
    public JSONArray getJSONArray(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns an Object at index idx that implements the JSONObject interface.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to a JSONObject
     */
    public JSONObject getJSONObject(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns a JSONOption wrapper for the Object at index idx.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     * @throws JSONException if the value at idx cannot be converted to a JSONOption
     */
    public JSONOption getJSONOption(int idx) throws IndexOutOfBoundsException, JSONException;

    /** Returns the String representation of the value at index idx.
     * @throws IndexOutOfBoundsException if the idx is out of bounds
     */
    public String getString(int idx) throws IndexOutOfBoundsException;

    /** Returns the String representation of the JSONArray. */
    public String toString();

}
