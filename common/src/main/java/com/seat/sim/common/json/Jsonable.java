package com.seat.sim.common.json;

/** 
 * An inherited API to support the JSON serialization and deserialization of 
 * Objects.
 */
public abstract class Jsonable {

    /** Required default constructor. */
    protected Jsonable() {}

    /** 
     * Jsonable constructor for decoding JSON.
     *
     * @throws JsonException if the JSON does not decode
     */
    protected Jsonable(Json json) throws JsonException {
        this.decode(json);
    }

    /** 
     * Should be overriden by child classes that support a JsonArray 
     * representation.
     *
     * @throws JsonException if the JsonArray does not decode
     */
    protected void decode(JsonArray json) throws JsonException {
        throw new JsonException(String.format("Cannot decode %s", json.toString()));
    }

    /**
     * Should be overriden by child classes that support a JsonObject 
     * representation.
     *
     * @throws JsonException if the JsonObject does not decode
     */
    protected void decode(JsonObject json) throws JsonException {
        throw new JsonException(String.format("Cannot decode %s", json.toString()));
    }

    /** 
     * Delegates to the JsonArray or JsonObject decode method.
     *
     * @throws JsonException if the Json does not decode
     */
    protected void decode(Json json) throws JsonException {
        if (json.isJsonArray()) {
            this.decode(json.getJsonArray());
        } else if (json.isJsonObject()) {
            this.decode(json.getJsonObject());
        } else {
            throw new JsonException(String.format("Cannot decode %s", json.toString()));
        }
    }

    /** 
     * Returns the JSON serialization of this Object.
     *
     * @throws JsonException if the Object cannot be serialized
     */
    public String encode() throws JsonException {
        return this.toJson().toString();
    }

    /** 
     * Returns {@code true} if the the Jsonable has the same encoding.
     *
     * @throws JsonException if the Jsonable cannot be serialized
     */
    public boolean equals(Jsonable json) throws JsonException {
        if (json == null) return false;
        return this.equals(json.encode());
    }

    /** 
     * Returns {@code true} if the the JsonArray has the same encoding.
     *
     * @throws JsonException if the JsonArray cannot be serialized
     */
    public boolean equals(JsonArray json) throws JsonException {
        if (json == null) return false;
        return this.equals(json.toString());
    }

    /** 
     * Returns {@code true} if the the JsonObject has the same encoding.
     *
     * @throws JsonException if the JsonObject cannot be serialized
     */
    public boolean equals(JsonObject json) throws JsonException {
        if (json == null) return false;
        return this.equals(json.toString());
    }

    /** 
     * Returns {@code true} if the the Json has the same encoding.
     *
     * @throws JsonException if the Json cannot be serialized
     */
    public boolean equals(Json json) throws JsonException {
        if (json == null) return false;
        return this.equals(json.toString());
    }

    /** 
     * Returns {@code true} if this Object has a matching encoding.
     *
     * @throws JsonException if this Object cannot be serialized
     */
    public boolean equals(String encoding) throws JsonException {
        if (encoding == null) return false;
        return this.encode().equals(encoding);
    }

    /** Child classes must implement a Json representation. */
    public abstract Json toJson() throws JsonException;

    /** 
     * Returns a String representation (encoding by default) of this Object for debugging purposes.
     *
     * @throws JsonException if this Object cannot be converted to a String
     */
    public String toString() throws JsonException {
        return this.encode();
    }
}
