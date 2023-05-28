package com.seat.sim.common.json;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** An interface for classes that support a JSON Object representation. */
public interface JsonObject extends JsonSerializable {

    /** 
     * Returns the Object associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to an Object or no such key exists
     * @return the Object mapped by key
     */
    Object get(String key) throws JsonException;

    /** 
     * Returns the boolean value associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to a boolean or no such key exists
     * @return the boolean mapped by key
     */
    boolean getBoolean(String key) throws JsonException;

    /** 
     * Returns the double value associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to a double or no such key exists
     * @return the double mapped by key
     */
    double getDouble(String key) throws JsonException;

    /** 
     * Returns the float value associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to a float or no such key exists
     * @return the float mapped by key
     */
    float getFloat(String key) throws JsonException;

    /** 
     * Returns the int value associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to an int or no such key exists
     * @return the int mapped by key
     */
    int getInt(String key) throws JsonException;

    /** 
     * Returns {@link Json} associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to Json or no such key exists
     * @return the Json mapped by key
     */
    Json getJson(String key) throws JsonException;

    /** 
     * Returns {@link JsonArray} associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to a JsonArray or no such key exists
     * @return the JsonArray mapped by key
     */
    JsonArray getJsonArray(String key) throws JsonException;

    /** 
     * Returns a {@link JsonObject} associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to a JsonObject or no such key exists
     * @return the JsonObject mapped by key
     */
    JsonObject getJsonObject(String key) throws JsonException;

    /** 
     * Returns the long value associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to a long or no such key exists
     * @return the long mapped by key
     */
    long getLong(String key) throws JsonException;

    /** 
     * Returns the String associated with the key.
     *
     * @throws JsonException if the value associated with the key cannot be 
     * converted to a String or no such key exists
     * @return the String mapped by key
     */
    String getString(String key) throws JsonException;

    /** Returns {@code true} if the key is contained. */
    boolean hasKey(String key);

    /** Returns a collection of keys. */
    Set<String> keySet();

    /** Returns the number of key-value pairs. */
    int size();

    /** Returns the Json representation. */
    default Json toJson() {
        return Json.of(this);
    }

    /** Returns a Map representation. */
    Map<String, Object> toMap();

    /** Returns a Map<String, T> representation. */
    <T> Map<String, T> toMap(Class<T> cls);

    /** Returns a collection of values. */
    Collection<Object> values();

    /** Returns a collection of values of type T. */
    <T> Collection<T> values(Class<T> cls);
}
