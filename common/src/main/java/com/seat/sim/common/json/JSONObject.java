package com.seat.sim.common.json;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** An interface for classes that support a JSON Object representation. */
public interface JSONObject extends JSONInterface {

    /** Returns the Object associated with the key.
     * @throws JSONException if the value associated with the key cannot be converted to an Object or no such key
     */
    Object get(String key) throws JSONException;

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

    /** Returns a JSONOptional wrapper for the Object associated with the key.
     * @throws JSONException if the value associated with the key cannot be converted to a JSONOptional or no such key
     */
    JSONOptional getJSONOptional(String key) throws JSONException;

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

    /** Returns a collection of the JSONObject keys. */
    Set<String> keySet();

    /** Returns the number of key-value pairs stored in the JSONObject. */
    int size();

    /** Returns a Map representation of the JSONObject. */
    Map<String, Object> toMap();

    /** Returns a Map<String, T> representation of the JSONObject. */
    <T> Map<String, T> toMap(Class<T> cls);

    /** Returns a collection of the JSONObject values. */
    Collection<Object> values();

    /** Returns a collection of type T of the JSONObject values. */
    <T> Collection<T> values(Class<T> cls);

}
