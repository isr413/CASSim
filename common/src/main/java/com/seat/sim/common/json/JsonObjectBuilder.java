package com.seat.sim.common.json;

/** An interface for building a JSON Object representation of a class. */
public interface JsonObjectBuilder {

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, boolean value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, double value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, float value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, int value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, Json value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, JsonArray value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, JsonObject value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, Object value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, long value) throws JsonException;

    /** 
     * Inserts the key-value pair into the JsonObject.
     *
     * @throws JsonException if JSON does not support the input value
     */
    void put(String key, String value) throws JsonException;

    /** Returns the JsonObject. */
    JsonObject toJson();

    /** 
     * Returns the String serialization of the JsonObject.
     *
     * @throws JsonException if the JsonObject cannot be serialized
     */
    String toString() throws JsonException;

    /** 
     * Returns the String serialization of the JsonObject (pretty printed).
     *
     * @throws JsonException if the JsonObject cannot be serialized
     */
    String toString(int tabSize) throws JsonException;
}
