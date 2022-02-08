package com.seat.rescuesim.common.json;

/** An interface for classes that support a JSON Object representation. */
public interface JSONObjectBuilder {

    /** Inserts the key-value pair into the JSONObject. */
    public void put(String key, double value);

    /** Inserts the key-value pair into the JSONObject. */
    public void put(String key, int value);

    /** Inserts the key-value pair into the JSONObject. */
    public void put(String key, JSONArray value);

    /** Inserts the key-value pair into the JSONObject. */
    public void put(String key, JSONObject value);

    /** Inserts the key-value pair into the JSONObject. */
    public void put(String key, JSONOption value);

    /** Inserts the key-value pair into the JSONObject. */
    public void put(String key, String value);

    /** Returns the JSONOption representation of the JSONObject. */
    public JSONOption toJSON();

}
