package com.seat.sim.common.json;

/** An interface for building a JSON Array representation of a class. */
public interface JsonArrayBuilder {

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(boolean value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     * @throws JsonException if JSON does not support the input value
     */
    void put(double value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(float value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(int value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(Json value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(JsonArray value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(JsonObject value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(Object value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(long value) throws JsonException;

    /** 
     * Appends the value to the JsonArray.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String value) throws JsonException;

    /** Returns the JsonArray. */
    JsonArray toJson();

    /** 
     * Returns the String serialization of the JsonArray.
     *
     * @throws JsonException if the JsonArray cannot be serialized
     */
    String toString() throws JsonException;

    /** 
     * Returns the String serialization of the JsonArray (pretty printed).
     *
     * @throws JsonException if the JsonArray cannot be serialized
     */
    String toString(int tabSize) throws JsonException;
}
