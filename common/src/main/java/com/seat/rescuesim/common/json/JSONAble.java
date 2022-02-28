package com.seat.rescuesim.common.json;

/** An API to support the JSON serialization and deserialization of Objects. */
public abstract class JSONAble {

    /** Required default constructor. */
    protected JSONAble() {}

    /** JSONAble constructor for decoding a JSONArray. */
    protected JSONAble(JSONArray json) {
        this.decode(json);
    }

    /** JSONAble constructor for decoding a JSONObject. */
    protected JSONAble(JSONObject json) {
        this.decode(json);
    }

    /** JSONAble constructor for decoding a JSONOption. */
    protected JSONAble(JSONOption option) {
        this.decode(option);
    }

    /** JSONAble constructor for decoding a String encoding. */
    protected JSONAble(String encoding) {
        this.decode(encoding);
    }

    /** Decodes the provided JSONOption to set the instance fields of this class.
     * @throws JSONException if the JSONOption does not decode to this Object
     */
    protected void decode(JSONOption option) throws JSONException {
        if (option.isSomeArray()) {
            this.decode(option.someArray());
        } else if (option.isSomeObject()) {
            this.decode(option.someObject());
        } else {
            throw new JSONException(String.format("Cannot decode %s", option.toString()));
        }
    }

    /** Should be overriden by child classes that support a JSONArray representation.
     * @throws JSONException if the JSONArray does not decode to this Object
     */
    protected void decode(JSONArray json) throws JSONException {
        throw new JSONException(String.format("Cannot decode %s", json.toString()));
    }

    /** Should be overriden by child classes that support a JSONObject representation.
     * @throws JSONException if the JSONObject does not decode to this Object
     */
    protected void decode(JSONObject json) throws JSONException {
        throw new JSONException(String.format("Cannot decode %s", json.toString()));
    }

    /** Decodes the provided String encoding to set the instance fields of this class.
     * @throws JSONException if the JSONOption does not decode to this Object
     */
    protected void decode(String encoding) throws JSONException {
        this.decode(JSONOption.String(encoding));
    }

    /** Returns the JSON serialization of this Object. */
    public String encode() {
        return this.toJSON().toString();
    }

    /** Returns true if the the JSONAble has the same encoding. */
    public boolean equals(JSONAble json) {
        return this.equals(json.encode());
    }

    /** Returns true if the the JSONArray has the same encoding. */
    public boolean equals(JSONArray json) {
        return this.equals(json.toString());
    }

    /** Returns true if the the JSONObject has the same encoding. */
    public boolean equals(JSONObject json) {
        return this.equals(json.toString());
    }

    /** Returns true if the the JSONOption has the same encoding. */
    public boolean equals(JSONOption option) {
        return this.equals(option.toString());
    }

    /** Returns true if the encoding would deserialize to this Object. */
    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

    /** Child classes must implement JSONOption representation. */
    public abstract JSONOption toJSON();

    /** Returns the String encoding representation of this Object. */
    public String toString() {
        return this.encode();
    }

}
