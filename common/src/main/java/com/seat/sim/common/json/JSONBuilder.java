package com.seat.sim.common.json;

/** An optional class to build JSONOptions. */
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

        /** Appends the value to the JSONArray.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(boolean value) throws JSONException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Appends the value to the JSONArray.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(double value) throws JSONException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Appends the value to the JSONArray.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(int value) throws JSONException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Appends the value to the JSONArray.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(JSONArray value) throws JSONException {
            try {
                if (value instanceof JSONOptional.JSONArrayOption) {
                    this.json.put(((JSONOptional.JSONArrayOption) value).json);
                } else {
                    this.json.put(new JSONOptional.JSONArrayOption(value.toString()).json);
                }
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Appends the value to the JSONArray.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(JSONObject value) throws JSONException {
            try {
                if (value instanceof JSONOptional.JSONObjectOption) {
                    this.json.put(((JSONOptional.JSONObjectOption) value).json);
                } else {
                    this.json.put(new JSONOptional.JSONObjectOption(value.toString()).json);
                }
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Appends the value to the JSONArray.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(JSONOptional value) throws JSONException {
            try {
                if (value.isSomeArray()) {
                    this.put(value.someArray());
                } else if (value.isSomeObject()) {
                    this.put(value.someObject());
                } else {
                    this.put(value.toString());
                }
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Appends the value to the JSONArray.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(long value) throws JSONException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Appends the value to the JSONArray.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String value) throws JSONException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the JSONOption representation of the JSONArray. */
        public JSONOptional toJSON() {
            return JSONOptional.Array(new JSONOptional.JSONArrayOption(this.json));
        }

        /** Returns the String representation of the JSONArray.
         * @throws JSONException if the JSONArray cannot be converted to a JSON string
         */
        public String toString() throws JSONException {
            try {
                return this.json.toString();
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String representation of the JSONArray (pretty printed).
         * @throws JSONException if the JSONArray cannot be converted to a JSON string
         */
        public String toString(int tabSize) throws JSONException {
            try {
                return this.json.toString(tabSize);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

    }

    /** JSONBuilder implementation of the JSONObjectBuilder interface. */
    static class JSONObjectOptionBuilder implements JSONObjectBuilder {

        org.json.JSONObject json;

        public JSONObjectOptionBuilder() {
            this.json = new org.json.JSONObject();
        }

        /** Inserts the key-value pair into the JSONObject.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String key, boolean value) throws JSONException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Inserts the key-value pair into the JSONObject.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String key, double value) throws JSONException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Inserts the key-value pair into the JSONObject.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String key, int value) throws JSONException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Inserts the key-value pair into the JSONObject.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String key, JSONArray value) throws JSONException {
            try {
                if (value instanceof JSONOptional.JSONArrayOption) {
                    this.json.put(key, ((JSONOptional.JSONArrayOption) value).json);
                } else {
                    this.json.put(key, new JSONOptional.JSONArrayOption(value.toString()).json);
                }
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Inserts the key-value pair into the JSONObject.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String key, JSONObject value) throws JSONException {
            try {
                if (value instanceof JSONOptional.JSONObjectOption) {
                    this.json.put(key, ((JSONOptional.JSONObjectOption) value).json);
                } else {
                    this.json.put(key, new JSONOptional.JSONObjectOption(value.toString()).json);
                }
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Inserts the key-value pair into the JSONObject.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String key, JSONOptional value) throws JSONException {
            try {
                if (value.isSomeArray()) {
                    this.put(key, value.someArray());
                } else if (value.isSomeObject()) {
                    this.put(key, value.someObject());
                } else {
                    this.put(key, value.toString());
                }
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Inserts the key-value pair into the JSONObject.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String key, long value) throws JSONException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Inserts the key-value pair into the JSONObject.
         * @throws JSONException if JSON does not support the input value
        */
        public void put(String key, String value) throws JSONException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the JSONOption representation of the JSONObject. */
        public JSONOptional toJSON() {
            return JSONOptional.Object(new JSONOptional.JSONObjectOption(this.json));
        }

        /** Returns the String representation of the JSONObject.
         * @throws JSONException if the JSONObject cannot be converted to a JSON string
         */
        public String toString() throws JSONException {
            try {
                return this.json.toString();
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String representation of the JSONObject (pretty printed).
         * @throws JSONException if the JSONObject cannot be converted to a JSON string
         */
        public String toString(int tabSize) throws JSONException {
            try {
                return this.json.toString(tabSize);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

    }

}
