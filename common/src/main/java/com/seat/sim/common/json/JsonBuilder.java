package com.seat.sim.common.json;

import java.util.Collection;
import java.util.Map;

/** A builder class for Json options. */
public final class JsonBuilder {

    /** Returns a builder a {@link JsonArray}. */
    public static JsonArrayBuilder Array() {
        return new JsonBuilder.JsonArrayOptionBuilder();
    }

    /** Returns a builder for a {@link JsonObject}. */
    public static JsonObjectBuilder Object() {
        return new JsonBuilder.JsonObjectOptionBuilder();
    }

    /** Returns a builder for a {@link JsonArray}. */
    public static <T> JsonArray toJsonArray(Collection<T> collection) {
        return new JsonBuilder.JsonArrayOptionBuilder(collection).toJsonArray();
    }

    /** Returns a builder for a {@link JsonObject}. */
    public static <T> JsonObject toJsonObject(Map<String, T> map) {
        return new JsonBuilder.JsonObjectOptionBuilder(map).toJsonObject();
    }

    /** org.json implementation of the JsonArrayBuilder interface. */
    static final class JsonArrayOptionBuilder implements JsonArrayBuilder {

        org.json.JSONArray json;

        public JsonArrayOptionBuilder() {
            this.json = new org.json.JSONArray();
        }

        public <T> JsonArrayOptionBuilder(Collection<T> collection) {
            this.json = new org.json.JSONArray();
            for (T o : collection) {
                if (JsonArray.class.isAssignableFrom(o.getClass())) {
                    this.put((JsonArray) o);
                } else if (JsonObject.class.isAssignableFrom(o.getClass())) {
                    this.put((JsonObject) o);
                } else if (Json.class.isAssignableFrom(o.getClass())) {
                    this.put((Json) o);
                } else {
                    this.put(o);
                }
            }
        }

        public void put(boolean value) throws JsonException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(double value) throws JsonException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(float value) throws JsonException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(int value) throws JsonException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(Json value) throws JsonException {
            try {
                if (value.isJsonArray()) {
                    this.put(value.getJsonArray());
                } else if (value.isJsonObject()) {
                    this.put(value.getJsonObject());
                } else {
                    this.put(value.toString());
                }
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(JsonArray value) throws JsonException {
            try {
                if (value instanceof Json.JsonArrayOption) {
                    this.json.put(((Json.JsonArrayOption) value).json);
                } else {
                    this.json.put(new Json.JsonArrayOption(value.toString()).json);
                }
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(JsonObject value) throws JsonException {
            try {
                if (value instanceof Json.JsonObjectOption) {
                    this.json.put(((Json.JsonObjectOption) value).json);
                } else {
                    this.json.put(new Json.JsonObjectOption(value.toString()).json);
                }
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(long value) throws JsonException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(Object value) throws JsonException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String value) throws JsonException {
            try {
                this.json.put(value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public Json toJson() {
            return Json.of(this.toJsonArray());
        }

        public JsonArray toJsonArray() {
            return new Json.JsonArrayOption(this.json);
        }

        public String toString() throws JsonException {
            try {
                return this.json.toString();
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public String toString(int tabSize) throws JsonException {
            try {
                return this.json.toString(tabSize);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }
    }

    /** org.json implementation of the JsonObjectBuilder interface. */
    static final class JsonObjectOptionBuilder implements JsonObjectBuilder {

        org.json.JSONObject json;

        public JsonObjectOptionBuilder() {
            this.json = new org.json.JSONObject();
        }

        public <T> JsonObjectOptionBuilder(Map<String, T> map) {
            this.json = new org.json.JSONObject();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                if (JsonArray.class.isAssignableFrom(entry.getValue().getClass())) {
                    this.put(entry.getKey(), (JsonArray) entry.getValue());
                } else if (JsonObject.class.isAssignableFrom(entry.getValue().getClass())) {
                    this.put(entry.getKey(), (JsonObject) entry.getValue());
                } else if (Json.class.isAssignableFrom(entry.getValue().getClass())) {
                    this.put(entry.getKey(), (Json) entry.getValue());
                } else {
                    this.put(entry.getKey(), entry.getValue());
                }
            }
        }

        public void put(String key, boolean value) throws JsonException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, double value) throws JsonException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, float value) throws JsonException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, int value) throws JsonException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, Json value) throws JsonException {
            try {
                if (value.isJsonArray()) {
                    this.put(key, value.getJsonArray());
                } else if (value.isJsonObject()) {
                    this.put(key, value.getJsonObject());
                } else {
                    this.put(key, value.toString());
                }
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, JsonArray value) throws JsonException {
            try {
                if (value instanceof Json.JsonArrayOption) {
                    this.json.put(key, ((Json.JsonArrayOption) value).json);
                } else {
                    this.json.put(key, new Json.JsonArrayOption(value.toString()).json);
                }
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, JsonObject value) throws JsonException {
            try {
                if (value instanceof Json.JsonObjectOption) {
                    this.json.put(key, ((Json.JsonObjectOption) value).json);
                } else {
                    this.json.put(key, new Json.JsonObjectOption(value.toString()).json);
                }
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, Object value) throws JsonException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, long value) throws JsonException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public void put(String key, String value) throws JsonException {
            try {
                this.json.put(key, value);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public Json toJson() {
            return Json.of(this.toJsonObject());
        }

        public JsonObject toJsonObject() {
            return new Json.JsonObjectOption(this.json);
        }

        public String toString() throws JsonException {
            try {
                return this.json.toString();
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public String toString(int tabSize) throws JsonException {
            try {
                return this.json.toString(tabSize);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }
    }
}
