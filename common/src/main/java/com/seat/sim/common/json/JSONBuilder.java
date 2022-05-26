package com.seat.sim.common.json;

import java.util.Collection;
import java.util.Map;

/** A builder class for JSONOptionals. */
public final class JSONBuilder {

    /** Returns a builder for the JSONArray option. */
    public static JSONArrayBuilder Array() {
        return new JSONBuilder.JSONArrayOptionBuilder();
    }

    /** Returns a builder for the JSONArray option. */
    public static <T> JSONArrayBuilder Array(Collection<T> collection) {
        return new JSONBuilder.JSONArrayOptionBuilder(collection);
    }

    /** Returns a builder for the JSONObject option. */
    public static JSONObjectBuilder Object() {
        return new JSONBuilder.JSONObjectOptionBuilder();
    }

    /** Returns a builder for the JSONObject option. */
    public static <T> JSONObjectBuilder Object(Map<String, T> map) {
        return new JSONBuilder.JSONObjectOptionBuilder(map);
    }

    /** JSONBuilder implementation of the JSONArrayBuilder interface. */
    static final class JSONArrayOptionBuilder implements JSONArrayBuilder {

        org.json.JSONArray json;

        public JSONArrayOptionBuilder() {
            this.json = new org.json.JSONArray();
        }

        public <T> JSONArrayOptionBuilder(Collection<T> collection) {
            this.json = new org.json.JSONArray();
            for (T o : collection) {
                if (JSONArray.class.isAssignableFrom(o.getClass())) {
                    this.put((JSONArray) o);
                } else if (JSONObject.class.isAssignableFrom(o.getClass())) {
                    this.put((JSONObject) o);
                } else if (JSONOptional.class.isAssignableFrom(o.getClass())) {
                    this.put((JSONOptional) o);
                } else {
                    this.put(o);
                }
            }
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
        public void put(float value) throws JSONException {
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
                if (value.isPresentArray()) {
                    this.put(value.getArray());
                } else if (value.isPresentObject()) {
                    this.put(value.getObject());
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
        public void put(Object value) throws JSONException {
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

        /** Returns the JSONOptional representation of the JSONArray. */
        public JSONOptional toJSON() {
            return JSONOptional.of(new JSONOptional.JSONArrayOption(this.json));
        }

        /** Returns the String serialization of the JSONArray.
         * @throws JSONException if the JSONArray cannot be serialized
         */
        public String toString() throws JSONException {
            try {
                return this.json.toString();
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String serialization of the JSONArray (pretty printed).
         * @throws JSONException if the JSONArray cannot be serialized
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
    static final class JSONObjectOptionBuilder implements JSONObjectBuilder {

        org.json.JSONObject json;

        public JSONObjectOptionBuilder() {
            this.json = new org.json.JSONObject();
        }

        public <T> JSONObjectOptionBuilder(Map<String, T> map) {
            this.json = new org.json.JSONObject();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                if (JSONArray.class.isAssignableFrom(entry.getValue().getClass())) {
                    this.put(entry.getKey(), (JSONArray) entry.getValue());
                } else if (JSONObject.class.isAssignableFrom(entry.getValue().getClass())) {
                    this.put(entry.getKey(), (JSONObject) entry.getValue());
                } else if (JSONOptional.class.isAssignableFrom(entry.getValue().getClass())) {
                    this.put(entry.getKey(), (JSONOptional) entry.getValue());
                } else {
                    this.put(entry.getKey(), entry.getValue());
                }
            }
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
        public void put(String key, float value) throws JSONException {
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
                if (value.isPresentArray()) {
                    this.put(key, value.getArray());
                } else if (value.isPresentObject()) {
                    this.put(key, value.getObject());
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
        public void put(String key, Object value) throws JSONException {
            try {
                this.json.put(key, value);
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

        /** Returns the JSONOptional representation of the JSONObject. */
        public JSONOptional toJSON() {
            return JSONOptional.of(new JSONOptional.JSONObjectOption(this.json));
        }

        /** Returns the String serialization of the JSONObject.
         * @throws JSONException if the JSONObject cannot be serialized
         */
        public String toString() throws JSONException {
            try {
                return this.json.toString();
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String serialization of the JSONObject (pretty printed).
         * @throws JSONException if the JSONObject cannot be serialized
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
