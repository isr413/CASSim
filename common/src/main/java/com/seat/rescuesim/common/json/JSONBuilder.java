package com.seat.rescuesim.common.json;

/** An option class to build JSONOptions. */
public class JSONBuilder {

    /** Returns a builder for the Array JSONOption. */
    public static JSONArrayBuilder Array() {
        return new JSONBuilder.JSONArrayOptionBuilder();
    }

    /** Returns a builder for the Object JSONOption. */
    public static JSONObjectBuilder Object() {
        return new JSONBuilder.JSONObjectOptionBuilder();
    }

    /** JSONBuilder implementation of the JSONArrayBuilder interface. */
    static class JSONArrayOptionBuilder implements JSONArrayBuilder {

        org.json.JSONArray json;

        public JSONArrayOptionBuilder() {
            this.json = new org.json.JSONArray();
        }

        /** Appends the value to the JSONArray. */
        public void put(double value) {
            this.json.put(value);
        }

        /** Appends the value to the JSONArray. */
        public void put(int value) {
            this.json.put(value);
        }

        /** Appends the value to the JSONArray. */
        public void put(JSONArray value) {
            if (value instanceof JSONOption.JSONArrayOption) {
                this.json.put(((JSONOption.JSONArrayOption) value).json);
            } else {
                this.json.put(new JSONOption.JSONArrayOption(value.toString()));
            }
        }

        /** Appends the value to the JSONArray. */
        public void put(JSONObject value) {
            if (value instanceof JSONOption.JSONObjectOption) {
                this.json.put(((JSONOption.JSONObjectOption) value).json);
            } else {
                this.json.put(new JSONOption.JSONObjectOption(value.toString()));
            }
        }

        /** Appends the value to the JSONArray. */
        public void put(JSONOption value) {
            if (value.isSomeArray()) {
                this.put(value.someArray());
            } else if (value.isSomeObject()) {
                this.put(value.someObject());
            } else {
                this.put(value.toString());
            }
        }

        /** Appends the value to the JSONArray. */
        public void put(long value) {
            this.json.put(value);
        }

        /** Appends the value to the JSONArray. */
        public void put(String value) {
            this.json.put(value);
        }

        /** Returns the JSONOption representation of the JSONArray. */
        public JSONOption toJSON() {
            return JSONOption.Array(new JSONOption.JSONArrayOption(this.json));
        }

        /** Returns the String representation of the JSONArray. */
        public String toString() {
            return this.json.toString();
        }

    }

    /** JSONBuilder implementation of the JSONObjectBuilder interface. */
    static class JSONObjectOptionBuilder implements JSONObjectBuilder {

        org.json.JSONObject json;

        public JSONObjectOptionBuilder() {
            this.json = new org.json.JSONObject();
        }

        /** Inserts the key-value pair into the JSONObject. */
        public void put(String key, double value) {
            this.json.put(key, value);
        }

        /** Inserts the key-value pair into the JSONObject. */
        public void put(String key, int value) {
            this.json.put(key, value);
        }

        /** Inserts the key-value pair into the JSONObject. */
        public void put(String key, JSONArray value) {
            if (value instanceof JSONOption.JSONArrayOption) {
                this.json.put(key, ((JSONOption.JSONArrayOption) value).json);
            } else {
                this.json.put(key, new JSONOption.JSONArrayOption(value.toString()));
            }
        }

        /** Inserts the key-value pair into the JSONObject. */
        public void put(String key, JSONObject value) {
            if (value instanceof JSONOption.JSONObjectOption) {
                this.json.put(key, ((JSONOption.JSONObjectOption) value).json);
            } else {
                this.json.put(key, new JSONOption.JSONObjectOption(value.toString()));
            }
        }

        /** Inserts the key-value pair into the JSONObject. */
        public void put(String key, JSONOption value) {
            if (value.isSomeArray()) {
                this.put(key, value.someArray());
            } else if (value.isSomeObject()) {
                this.put(key, value.someObject());
            } else {
                this.put(key, value.toString());
            }
        }

        /** Inserts the key-value pair into the JSONObject. */
        public void put(String key, long value) {
            this.json.put(key, value);
        }

        /** Inserts the key-value pair into the JSONObject. */
        public void put(String key, String value) {
            this.json.put(key, value);
        }

        /** Returns the JSONOption representation of the JSONObject. */
        public JSONOption toJSON() {
            return JSONOption.Object(new JSONOption.JSONObjectOption(this.json));
        }

        /** Returns the String representation of the JSONObject. */
        public String toString() {
            return this.json.toString();
        }

    }

}
