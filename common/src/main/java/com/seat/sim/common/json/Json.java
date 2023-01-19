package com.seat.sim.common.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

/** A union type for the JsonArray and JsonObject types. */
public final class Json {

    /** 
     * Returns a wrapper for the JsonArray interface type.
     *
     * @throws JsonException if the JsonArray is null
     */
    public static Json of(JsonArray json) throws JsonException {
        if (json == null) throw new JsonException(new NullPointerException().toString());
        return new Json(json);
    }

    /** 
     * Returns a wrapper for the JsonObject interface type.
     *
     * @throws JsonException if the JsonObject is null
     */
    public static Json of(JsonObject json) throws JsonException {
        if (json == null) throw new JsonException(new NullPointerException().toString());
        return new Json(json);
    }

    /** 
     * Returns a wrapper for the JsonArray or JsonObject interace type based on 
     * the encoding.
     *
     * @throws JsonException if the encoding is null or does not decode
    */
    public static Json of(String encoding) throws JsonException {
        if (encoding == null) throw new JsonException(new NullPointerException().toString());
        if (JsonParser.isEncodedJsonArray(encoding)) {
            return new Json(new JsonArrayOption(JsonParser.trimEncoding(encoding)));
        }
        if (JsonParser.isEncodedJsonObject(encoding)) {
            return new Json(new JsonObjectOption(JsonParser.trimEncoding(encoding)));
        }
        throw new JsonException(String.format("Cannot decode invalid JSON string %s", encoding));
    }

    private JsonArray arrayOption;
    private JsonObject objectOption;

    private Json(JsonArray json) {
        this.arrayOption = json;
        this.objectOption = null;
    }

    private Json(JsonObject json) {
        this.arrayOption = null;
        this.objectOption = json;
    }

    /** Returns {@code true} if the json is a shallow copy. */
    public boolean equals(Json json) {
        return this.arrayOption == json.arrayOption && this.objectOption == json.objectOption;
    }

    /** 
     * Returns the wrapped JsonArray.
     *
     * @throws JsonException if the option being wrapped is not a JsonArray
     */
    public JsonArray getJsonArray() throws JsonException {
        if (!this.isJsonArray()) throw new JsonException(new NoSuchElementException().toString());
        return this.arrayOption;
    }

    /** 
     * Returns the wrapped JsonObject.
     *
     * @throws JsonException if the option being wrapped is not a JsonObject
     */
    public JsonObject getJsonObject() throws JsonException {
        if (!this.isJsonObject()) throw new JsonException(new NoSuchElementException().toString());
        return this.objectOption;
    }

    /** Returns {@code true} if the JsonArray option is present. */
    public boolean isJsonArray() {
        return this.arrayOption != null;
    }

    /** Returns {@code true} if the JsonObject option is present. */
    public boolean isJsonObject() {
        return this.objectOption != null;
    }

    /** 
     * Returns the String serialization of the value.
     *
     * @throws JsonException if the JSON cannot be serialized
     * @return the JSON String serialization
     */
    public String toString() throws JsonException {
        if (this.isJsonArray()) return this.arrayOption.toString();
        return this.objectOption.toString();
    }

    /** 
     * Returns the String serialization (pretty printed) of the value.
     *
     * @throws JsonException if the JSON cannot be serialized
     * @return the JSON String serialization
     */
    public String toString(int tabSize) throws JsonException {
        if (this.isJsonArray()) return this.arrayOption.toString(tabSize);
        return this.objectOption.toString(tabSize);
    }

    /** org.json implementation of the JsonArray interface. */
    static class JsonArrayOption implements JsonArray {

        org.json.JSONArray json;

        public JsonArrayOption(org.json.JSONArray json) {
            this.json = json;
        }

        public JsonArrayOption(String encoding) throws JsonException {
            try {
                this.json = new org.json.JSONArray(encoding);
            } catch(org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        private void assertBounds(int idx) throws JsonException {
            if (idx < 0 || this.json.length() <= idx) {
                throw new JsonException(new IndexOutOfBoundsException(idx).toString());
            }
        }

        public void forEach(Consumer<? super Object> consumer) {
            this.json.forEach(consumer);
        }

        @SuppressWarnings("unchecked")
        public <T> void forEach(Class<T> cls, Consumer<? super T> consumer) {
            for (int i = 0; i < this.json.length(); i++) {
                if (Json.class.isAssignableFrom(cls)) {
                    consumer.accept((T) this.getJson(i));
                } else if (JsonArray.class.isAssignableFrom(cls)) {
                    consumer.accept((T) this.getJsonArray(i));
                } else if (JsonObject.class.isAssignableFrom(cls)) {
                    consumer.accept((T) this.getJsonObject(i));
                } else {
                    consumer.accept((T) this.get(i));
                }
            }
        }

        public Object get(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return this.json.get(idx);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public boolean getBoolean(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return this.json.getBoolean(idx);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public double getDouble(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return this.json.getDouble(idx);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public float getFloat(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return this.json.getFloat(idx);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public int getInt(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return this.json.getInt(idx);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public Json getJson(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                if (this.json.get(idx) instanceof org.json.JSONArray) {
                    return new Json(this.getJsonArray(idx));
                } else if (this.json.get(idx) instanceof org.json.JSONObject) {
                    return new Json(this.getJsonObject(idx));
                } else {
                    return (Json) this.json.get(idx);
                }
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public JsonArray getJsonArray(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return new JsonArrayOption(this.json.getJSONArray(idx));
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public JsonObject getJsonObject(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return new JsonObjectOption(this.json.getJSONObject(idx));
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public long getLong(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return this.json.getLong(idx);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public String getString(int idx) throws JsonException {
            this.assertBounds(idx);
            try {
                return this.json.getString(idx);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public Iterator<Object> iterator() {
            return this.json.iterator();
        }

        public int length() {
            return this.json.length();
        }

        public Spliterator<Object> spliterator() {
            return this.json.spliterator();
        }

        public List<Object> toList() {
            return this.json.toList();
        }

        @SuppressWarnings("unchecked")
        public <T> List<T> toList(Class<T> cls) {
            ArrayList<T> list = new ArrayList<>();
            for (int i = 0; i < this.json.length(); i++) {
                if (Json.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJson(i));
                } else if (JsonArray.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJsonArray(i));
                } else if (JsonObject.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJsonObject(i));
                } else {
                    list.add((T) this.get(i));
                }
            }
            return list;
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

    /** org.json implementation of the JsonObject interface. */
    static class JsonObjectOption implements JsonObject {

        org.json.JSONObject json;

        public JsonObjectOption(org.json.JSONObject json) {
            this.json = json;
        }

        public JsonObjectOption(String encoding) {
            try {
                this.json = new org.json.JSONObject(encoding);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        private void assertContains(String key) throws JsonException {
            if (!this.json.has(key)) {
                throw new JsonException(String.format("%s not found in JsonObject.", key));
            }
        }

        public Object get(String key) throws JsonException {
            this.assertContains(key);
            try {
                return this.json.get(key);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public boolean getBoolean(String key) throws JsonException {
            this.assertContains(key);
            try {
                return this.json.getBoolean(key);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public double getDouble(String key) throws JsonException {
            this.assertContains(key);
            try {
                return this.json.getDouble(key);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public float getFloat(String key) throws JsonException {
            this.assertContains(key);
            try {
                return this.json.getFloat(key);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public int getInt(String key) throws JsonException {
            this.assertContains(key);
            try {
                return this.json.getInt(key);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public Json getJson(String key) throws JsonException {
            this.assertContains(key);
            try {
                if (this.json.get(key) instanceof org.json.JSONArray) {
                    return new Json(this.getJsonArray(key));
                } else if (this.json.get(key) instanceof org.json.JSONObject) {
                    return new Json(this.getJsonObject(key));
                } else {
                    return (Json) this.get(key);
                }
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public JsonArray getJsonArray(String key) throws JsonException {
            this.assertContains(key);
            try {
                return new JsonArrayOption(this.json.getJSONArray(key));
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public JsonObject getJsonObject(String key) throws JsonException {
            this.assertContains(key);
            try {
                return new JsonObjectOption(this.json.getJSONObject(key));
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public long getLong(String key) throws JsonException {
            this.assertContains(key);
            try {
                return this.json.getLong(key);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public String getString(String key) throws JsonException {
            this.assertContains(key);
            try {
                return this.json.getString(key);
            } catch (org.json.JSONException e) {
                throw new JsonException(e.toString());
            }
        }

        public boolean hasKey(String key) {
            return this.json.has(key);
        }

        public Set<String> keySet() {
            return this.json.keySet();
        }

        public int size() {
            return this.json.length();
        }

        public Map<String, Object> toMap() {
            return this.json.toMap();
        }

        @SuppressWarnings("unchecked")
        public <T> Map<String, T> toMap(Class<T> cls) {
            Map<String, T> map = new HashMap<>();
            for (String key : this.keySet()) {
                if (Json.class.isAssignableFrom(cls)) {
                    map.put(key, (T) this.getJson(key));
                } else if (JsonArray.class.isAssignableFrom(cls)) {
                    map.put(key, (T) this.getJsonArray(key));
                } else if (JsonObject.class.isAssignableFrom(cls)) {
                    map.put(key, (T) this.getJsonObject(key));
                } else {
                    map.put(key, (T) this.get(key));
                }
            }
            return map;
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

        public Collection<Object> values() {
            return this.json.toMap().values();
        }

        @SuppressWarnings("unchecked")
        public <T> Collection<T> values(Class<T> cls) {
            ArrayList<T> list = new ArrayList<>();
            for (String key : this.keySet()) {
                if (Json.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJson(key));
                } else if (JsonArray.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJsonArray(key));
                } else if (JsonObject.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJsonObject(key));
                } else {
                    list.add((T) this.get(key));
                }
            }
            return list;
        }
    }
}
