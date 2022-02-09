package com.seat.rescuesim.common.json;

/** An interface for classes that support a JSON Array representation. */
public interface JSONArrayBuilder {

    /** Appends the value to the JSONArray. */
    void put(double value);

    /** Appends the value to the JSONArray. */
    void put(int value);

    /** Appends the value to the JSONArray. */
    void put(JSONArray value);

    /** Appends the value to the JSONArray. */
    void put(JSONObject value);

    /** Appends the value to the JSONArray. */
    void put(JSONOption value);

    /** Appends the value to the JSONArray. */
    void put(String value);

    /** Returns the JSONOption representation of the JSONArray. */
    JSONOption toJSON();

}
