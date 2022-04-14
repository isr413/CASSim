package com.seat.sim.common.json;

public interface JSONInterface {

    /** Returns the String representation of the JSON.
     * @throws JSONException if the JSON cannot be converted to a JSON string
     */
    String toString() throws JSONException;

    /** Returns the String representation of the JSON (pretty printed).
     * @throws JSONException if the JSON cannot be converted to a JSON string
     */
    String toString(int tabSize) throws JSONException;

}
