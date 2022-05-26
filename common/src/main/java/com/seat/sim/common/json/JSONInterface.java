package com.seat.sim.common.json;

public interface JSONInterface {

    /** Returns the String serialization of the JSON instance.
     * @throws JSONException if the JSON cannot be serialized
     */
    String toString() throws JSONException;

    /** Returns the String serialization of the JSON instance (pretty printed).
     * @throws JSONException if the JSON cannot be serialized
     */
    String toString(int tabSize) throws JSONException;

}
