package com.seat.sim.common.json;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/** An interface for classes that support a JSON Array representation. */
public interface JsonArray extends Iterable<Object>, JsonSerializable {

    /** Applies the consumer for each element in the JsonArray. */
    void forEach(Consumer<? super Object> consumer);

    /** Applies the consumer for each element in the JsonArray. */
    <T> void forEach(Class<T> cls, Consumer<? super T> consumer);

    /** 
     * Returns the Object at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to an 
     * Object or is out of bounds
     * @return the Object at idx
     */
    Object get(int idx) throws JsonException;

    /** 
     * Returns the boolean value at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to a 
     * boolean or is out of bounds
     * @return the boolean at idx
     */
    boolean getBoolean(int idx) throws JsonException;

    /** 
     * Returns the double value at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to a 
     * double or is out of bounds
     * @return the double at idx
     */
    double getDouble(int idx) throws JsonException;

    /** 
     * Returns the float value at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to a 
     * float or is out of bounds
     * @return the float at idx
     */
    float getFloat(int idx) throws JsonException;

    /** 
     * Returns the int value at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to an int 
     * or is out of bounds
     * @return the int at idx
     */
    int getInt(int idx) throws JsonException;

    /** 
     * Returns the {@link Json} at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to
     * Json or is out of bounds
     * @return the Json at idx
     */
    Json getJson(int idx) throws JsonException;

    /** 
     * Returns the {@link JsonArray} at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to a 
     * JsonArray or is out of bounds
     * @return the JsonArray at idx
     */
    JsonArray getJsonArray(int idx) throws JsonException;

    /** 
     * Returns an {@link JsonObject} at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to a 
     * JsonObject or is out of bounds
     * @return the JsonObject at idx
     */
    JsonObject getJsonObject(int idx) throws JsonException;

    /** 
     * Returns the long value at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to a long 
     * or is out of bounds
     * @return the long at idx
     */
    long getLong(int idx) throws JsonException;

    /** 
     * Returns the String value at index idx.
     *
     * @throws JsonException if the value at idx cannot be converted to a 
     * String or idx is out of bounds
     * @return the String at idx
     */
    String getString(int idx) throws JsonException;

    /** Returns an Iterator. */
    Iterator<Object> iterator();

    /** Returns the number of elements. */
    int length();

    /** Returns a Spliterator. */
    Spliterator<Object> spliterator();

    /** Returns a List representation. */
    List<Object> toList();

    /** Returns a List<T> representation. */
    <T> List<T> toList(Class<T> cls);
}
