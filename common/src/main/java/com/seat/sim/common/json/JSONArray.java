package com.seat.sim.common.json;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/** An interface for classes that support a JSON Array representation. */
public interface JSONArray extends Iterable<Object>, JSONInterface {

    /** Applies the consumer for each element in the JSONArray. */
    void forEach(Consumer<? super Object> consumer);

    /** Applies the consumer for each element in the JSONArray. */
    <T> void forEach(Class<T> classType, Consumer<? super T> consumer);

    /** Returns the Object at index idx.
     * @throws JSONException if the value at idx cannot be converted to an Object or is out of bounds
     */
    Object get(int idx) throws JSONException;

    /** Returns the boolean value at index idx.
     * @throws JSONException if the value at idx cannot be converted to a boolean or is out of bounds
     */
    boolean getBoolean(int idx) throws JSONException;

    /** Returns the double value at index idx.
     * @throws JSONException if the value at idx cannot be converted to a double or is out of bounds
     */
    double getDouble(int idx) throws JSONException;

    /** Returns the int value at index idx.
     * @throws JSONException if the value at idx cannot be converted to an int or is out of bounds
     */
    int getInt(int idx) throws JSONException;

    /** Returns an Object at index idx that implements the JSONArray interface.
     * @throws JSONException if the value at idx cannot be converted to a JSONArray or is out of bounds
     */
    JSONArray getJSONArray(int idx) throws JSONException;

    /** Returns an Object at index idx that implements the JSONObject interface.
     * @throws JSONException if the value at idx cannot be converted to a JSONObject or is out of bounds
     */
    JSONObject getJSONObject(int idx) throws JSONException;

    /** Returns a JSONOptional wrapper for the Object at index idx.
     * @throws JSONException if the value at idx cannot be converted to a JSONOptional or is out of bounds
     */
    JSONOptional getJSONOptional(int idx) throws JSONException;

    /** Returns the long value at index idx.
     * @throws JSONException if the value at idx cannot be converted to a long or is out of bounds
     */
    long getLong(int idx) throws JSONException;

    /** Returns the String representation of the value at index idx.
     * @throws JSONException if the idx is out of bounds
     */
    String getString(int idx) throws JSONException;

    /** Returns the number of elements in the JSONArray. */
    int length();

    /** Returns a spliterator for the JSONArray. */
    Spliterator<Object> spliterator();

    /** Returns a List representation of the JSONArray. */
    List<Object> toList();

    /** Returns a List<T> representation of the JSONArray. */
    <T> List<T> toList(Class<T> classType);

}
