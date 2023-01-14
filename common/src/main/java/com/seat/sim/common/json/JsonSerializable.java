package com.seat.sim.common.json;

/** An interface for classes that can be serialized into a JSON string. */
public interface JsonSerializable {

    /** 
     * Returns the String serialization of the JSON instance.
     *
     * @throws JSONException if the JSON cannot be serialized
     */
    String toString() throws JsonException;

    /** 
     * Returns the String serialization of the JSON instance (pretty printed).
     *
     * @throws JSONException if the JSON cannot be serialized
     */
    String toString(int tabSize) throws JsonException;
}
