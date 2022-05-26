package com.seat.sim.common.json;

/** An inherited API to support the JSON serialization and deserialization of Objects. */
public abstract class JSONAble {

    /** Required default constructor. */
    protected JSONAble() {}

    /** JSONAble constructor for decoding a JSONOptional.
     * @throws JSONException if the JSONOptional does not decode
    */
    protected JSONAble(JSONOptional optional) throws JSONException {
        this.decode(optional);
    }

    /** Should be overriden by child classes that support a JSONArray representation.
     * @throws JSONException if the JSONArray does not decode
     */
    protected void decode(JSONArray json) throws JSONException {
        throw new JSONException(String.format("Cannot decode %s", json.toString()));
    }

    /** Should be overriden by child classes that support a JSONObject representation.
     * @throws JSONException if the JSONObject does not decode
     */
    protected void decode(JSONObject json) throws JSONException {
        throw new JSONException(String.format("Cannot decode %s", json.toString()));
    }

    /** Delegates to the JSONArray or JSONObject decode method.
     * @throws JSONException if the JSONOptional does not decode
     */
    protected void decode(JSONOptional optional) throws JSONException {
        if (optional.isPresentArray()) {
            this.decode(optional.getArray());
        } else if (optional.isPresentObject()) {
            this.decode(optional.getObject());
        } else {
            throw new JSONException(String.format("Cannot decode %s", optional.toString()));
        }
    }

    /** Returns the JSON serialization of this Object.
     * @throws JSONException if the Object cannot be serialized
    */
    public String encode() throws JSONException {
        return this.toJSON().toString();
    }

    /** Returns true if the the JSONAble has the same encoding.
     * @throws JSONException if the JSONAble cannot be serialized
    */
    public boolean equals(JSONAble json) throws JSONException {
        if (json == null) return false;
        return this.equals(json.encode());
    }

    /** Returns true if the the JSONArray has the same encoding.
     * @throws JSONException if the JSONArray cannot be serialized
    */
    public boolean equals(JSONArray json) throws JSONException {
        if (json == null) return false;
        return this.equals(json.toString());
    }

    /** Returns true if the the JSONObject has the same encoding.
     * @throws JSONException if the JSONObject cannot be serialized
    */
    public boolean equals(JSONObject json) throws JSONException {
        if (json == null) return false;
        return this.equals(json.toString());
    }

    /** Returns true if the the JSONOptional has the same encoding.
     * @throws JSONException if the JSONOptional cannot be serialized
    */
    public boolean equals(JSONOptional optional) throws JSONException {
        if (optional == null) return false;
        return this.equals(optional.toString());
    }

    /** Returns true if this Object has a matching encoding.
     * @throws JSONException if this Object cannot be serialized
    */
    public boolean equals(String encoding) throws JSONException {
        if (encoding == null) return false;
        return this.encode().equals(encoding);
    }

    /** Child classes must implement a JSONOptional representation. */
    public abstract JSONOptional toJSON() throws JSONException;

    /** Returns a String representation (encoding by default) of this Object for debugging purposes.
     * @throws JSONException if this Object cannot be converted to a String
    */
    public String toString() throws JSONException {
        return this.encode();
    }

}
