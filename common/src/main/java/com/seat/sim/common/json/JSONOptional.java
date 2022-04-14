package com.seat.sim.common.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/** An optional class to wrap JSONArray and JSONObject interface types. */
public final class JSONOptional {

    /** Returns the None type wrapper. */
    public static JSONOptional empty() {
        return new JSONOptional();
    }

    /** Returns an optional wrapper for the JSONArray interface type. */
    public static JSONOptional of(JSONArray json) throws JSONException {
        if (json == null) throw new JSONException(new NullPointerException().toString());
        return new JSONOptional(json);
    }

    /** Returns an optional wrapper for the JSONObject interface type. */
    public static JSONOptional of(JSONObject json) throws JSONException {
        if (json == null) throw new JSONException(new NullPointerException().toString());
        return new JSONOptional(json);
    }

    /** Returns an optional wrapper for the JSONArray or JSONObject interace types based on the encoding. */
    public static JSONOptional of(String encoding) throws JSONException {
        if (encoding == null) throw new JSONException(new NullPointerException().toString());
        JSONOptional optional = JSONOptional.ofNullable(encoding);
        if (!optional.isPresent()) {
            throw new JSONException(String.format("Cannot decode invalid JSON string %s", encoding));
        }
        return optional;
    }

    /** Returns an optional wrapper for the JSONArray interface type or empty if json is null. */
    public static JSONOptional ofNullable(JSONArray json) {
        if (json == null) return JSONOptional.empty();
        return new JSONOptional(json);
    }

    /** Returns an optional wrapper for the JSONObject interface type or empty if json is null. */
    public static JSONOptional ofNullable(JSONObject json) throws JSONException {
        if (json == null) return JSONOptional.empty();
        return new JSONOptional(json);
    }

    /** Returns an optional wrapper for the JSONArray or JSONObject interace types based on the encoding
     * or empty if the encoding is invalid json.
     */
    public static JSONOptional ofNullable(String encoding) throws JSONException {
        if (encoding == null) return JSONOptional.empty();
        if (JSONParser.isEncodedJSONArray(encoding)) {
            return new JSONOptional(new JSONArrayOption(JSONParser.trimEncoding(encoding)));
        }
        if (JSONParser.isEncodedJSONObject(encoding)) {
            return new JSONOptional(new JSONObjectOption(JSONParser.trimEncoding(encoding)));
        }
        return JSONOptional.empty();
    }

    private JSONArray arrayOption;
    private JSONObject objectOption;

    private JSONOptional() {
        this.arrayOption = null;
        this.objectOption = null;
    }

    private JSONOptional(JSONArray json) {
        this.arrayOption = json;
        this.objectOption = null;
    }

    private JSONOptional(JSONObject json) {
        this.arrayOption = null;
        this.objectOption = json;
    }

    /** Applies the filter if the value is present. */
    @SuppressWarnings("unchecked")
    public <T extends JSONInterface> Optional<T> filter(Class<T> cls, Predicate<T> predicate) {
        if (JSONArray.class.isAssignableFrom(cls) && this.isPresentArray()) {
            return (predicate.test((T) this.arrayOption)) ?
                Optional.of((T) this.arrayOption) :
                Optional.empty();
        }
        if (JSONObject.class.isAssignableFrom(cls) && this.isPresentObject()) {
            return (predicate.test((T) this.objectOption)) ?
                Optional.of((T) this.objectOption) :
                Optional.empty();
        }
        return Optional.empty();
    }

    /** Applies the mapper if the value is present. */
    @SuppressWarnings("unchecked")
    public <T extends JSONInterface, U> Optional<U> flatMap(Class<T> cls, Function<T, Optional<U>> mapper) {
        if (JSONArray.class.isAssignableFrom(cls) && this.isPresentArray()) {
            return mapper.apply((T) this.arrayOption);
        }
        if (JSONObject.class.isAssignableFrom(cls) && this.isPresentObject()) {
            return mapper.apply((T) this.objectOption);
        }
        return Optional.empty();
    }

    /** Returns the JSONArray wrapped by this optional.
     * @throws JSONException if the optional being wrapped is not an Array optional
     */
    public JSONArray getArray() throws JSONException {
        if (!this.isPresentArray()) throw new JSONException(new NoSuchElementException().toString());
        return this.arrayOption;
    }

    /** Returns the JSONObject wrapped by this optional.
     * @throws JSONException if the optional being wrapped is not an Object optional
     */
    public JSONObject getObject() throws JSONException {
        if (!this.isPresentObject()) throw new JSONException(new NoSuchElementException().toString());
        return this.objectOption;
    }

    /** Invokes the consumer if the array option is present. */
    @SuppressWarnings("unchecked")
    public <T extends JSONInterface> void ifPresent(Class<T> cls, Consumer<T> consumer) {
        if (JSONArray.class.isAssignableFrom(cls) && this.isPresentArray()) {
            consumer.accept((T) this.arrayOption);
        }
        if (JSONObject.class.isAssignableFrom(cls) && this.isPresentObject()) {
            consumer.accept((T) this.objectOption);
        }
    }

    /** Returns true if there is a value present. */
    public boolean isPresent() {
        return this.arrayOption != null || this.objectOption != null;
    }

    /** Returns true if this is the JSONArray optional. */
    public boolean isPresentArray() {
        return this.arrayOption != null;
    }

    /** Returns true if this is the JSONObject optional. */
    public boolean isPresentObject() {
        return this.objectOption != null;
    }

    /** Applies the mapper if the value is present. */
    @SuppressWarnings("unchecked")
    public <T extends JSONInterface, U> Optional<U> map(Class<T> cls, Function<T, ? extends U> mapper) {
        if (JSONArray.class.isAssignableFrom(cls) && this.isPresentArray()) {
            return Optional.ofNullable(mapper.apply((T) this.arrayOption));
        }
        if (JSONObject.class.isAssignableFrom(cls) && this.isPresentObject()) {
            return Optional.ofNullable(mapper.apply((T) this.objectOption));
        }
        return Optional.empty();
    }

    /** Returns the value if present or the other. */
    @SuppressWarnings("unchecked")
    public <T extends JSONInterface> T orElse(Class<T> cls, T other) {
        if (JSONArray.class.isAssignableFrom(cls) && this.isPresentArray()) {
            return (T) this.arrayOption;
        }
        if (JSONObject.class.isAssignableFrom(cls) && this.isPresentObject()) {
            return (T) this.objectOption;
        }
        return other;
    }

    /** Returns the value if present or the other. */
    @SuppressWarnings("unchecked")
    public <T extends JSONInterface> T orElseGet(Class<T> cls, Supplier<T> other) {
        if (JSONArray.class.isAssignableFrom(cls) && this.isPresentArray()) {
            return (T) this.arrayOption;
        }
        if (JSONObject.class.isAssignableFrom(cls) && this.isPresentObject()) {
            return (T) this.objectOption;
        }
        return other.get();
    }

    /** Returns the value if present or the other. */
    @SuppressWarnings("unchecked")
    public <T extends JSONInterface, X extends Throwable> T orElseThrow(Class<T> cls,
            Supplier<X> exceptionSupplier) throws X {
        if (JSONArray.class.isAssignableFrom(cls) && this.isPresentArray()) {
            return (T) this.arrayOption;
        }
        if (JSONObject.class.isAssignableFrom(cls) && this.isPresentObject()) {
            return (T) this.objectOption;
        }
        throw exceptionSupplier.get();
    }

    /** Returns the String representation of the optional being wrapped.
     * @throws JSONException if the JSONOption cannot be converted to a JSON string
     */
    public String toString() throws JSONException {
        if (this.isPresentArray()) return this.arrayOption.toString();
        if (this.isPresentObject()) return this.objectOption.toString();
        return "";
    }

    /** Returns the String representation of the optional being wrapped (pretty printed).
     * @throws JSONException if the JSONOption cannot be converted to a JSON string
     */
    public String toString(int tabSize) throws JSONException {
        if (this.isPresentArray()) return this.arrayOption.toString(tabSize);
        if (this.isPresentObject()) return this.objectOption.toString(tabSize);
        return "";
    }

    public boolean equals(JSONOptional optional) {
        return this.arrayOption == optional.arrayOption && this.objectOption == optional.objectOption;
    }

    /** JSONOption implementation of the JSONArray interface. */
    static class JSONArrayOption implements JSONArray {

        org.json.JSONArray json;

        public JSONArrayOption(org.json.JSONArray json) {
            this.json = json;
        }

        public JSONArrayOption(String encoding) throws JSONException {
            try {
                this.json = new org.json.JSONArray(encoding);
            } catch(org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Throws a JSONException if the idx is out of bounds. */
        private void assertBounds(int idx) throws JSONException {
            if (idx < 0 || this.json.length() <= idx) {
                throw new JSONException(new IndexOutOfBoundsException(idx).toString());
            }
        }

        /** Applies the consumer for each element in the JSONArray. */
        public void forEach(Consumer<? super Object> consumer) {
            this.json.forEach(consumer);
        }

        /** Applies the consumer for each element in the JSONArray. */
        @SuppressWarnings("unchecked")
        public <T> void forEach(Class<T> cls, Consumer<? super T> consumer) {
            for (int i = 0; i < this.json.length(); i++) {
                if (JSONArray.class.isAssignableFrom(cls)) {
                    consumer.accept((T) this.getJSONArray(i));
                } else if (JSONObject.class.isAssignableFrom(cls)) {
                    consumer.accept((T) this.getJSONObject(i));
                } else if (JSONOptional.class.isAssignableFrom(cls)) {
                    consumer.accept((T) this.getJSONOptional(i));
                } else {
                    consumer.accept((T) this.get(i));
                }
            }
        }

        /** Returns the Object at index idx.
         * @throws JSONException if the value at idx cannot be converted to an Object or is out of bounds
         */
        public Object get(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                return this.json.get(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the boolean value at index idx.
         * @throws JSONException if the value at idx cannot be converted to a boolean or is out of bounds
         */
        public boolean getBoolean(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                return this.json.getBoolean(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the double value at index idx.
         * @throws JSONException if the value at idx cannot be converted to a double or is out of bounds
         */
        public double getDouble(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                return this.json.getDouble(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the int value at index idx.
         * @throws JSONException if the value at idx cannot be converted to an int or is out of bounds
         */
        public int getInt(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                return this.json.getInt(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns an Object at index idx that implements the JSONArray interface.
         * @throws JSONException if the value at idx cannot be converted to a JSONArray or is out of bounds
         */
        public JSONArray getJSONArray(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                return new JSONArrayOption(this.json.getJSONArray(idx));
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns an Object at index idx that implements the JSONObject interface.
         * @throws JSONException if the value at idx cannot be converted to a JSONObject or is out of bounds
         */
        public JSONObject getJSONObject(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                return new JSONObjectOption(this.json.getJSONObject(idx));
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns a JSONOptional wrapper for the Object at index idx.
         * @throws JSONException if the value at idx cannot be converted to a JSONOptional or is out of bounds
         */
        public JSONOptional getJSONOptional(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                if (this.json.get(idx) instanceof org.json.JSONArray) {
                    return new JSONOptional(new JSONArrayOption(this.json.getJSONArray(idx)));
                }
                if (this.json.get(idx) instanceof org.json.JSONObject) {
                    return new JSONOptional(new JSONObjectOption(this.json.getJSONObject(idx)));
                }
                return JSONOptional.empty();
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the long value at index idx.
         * @throws JSONException if the value at idx cannot be converted to a long or is out of bounds
         */
        public long getLong(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                return this.json.getLong(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String representation of the value at index idx.
         * @throws JSONException if the idx is out of bounds
         */
        public String getString(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                return this.json.getString(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        public Iterator<Object> iterator() {
            return this.json.iterator();
        }

        /** Returns the number of elements in the JSONArray. */
        public int length() {
            return this.json.length();
        }

        /** Returns a spliterator for the JSONArray. */
        public Spliterator<Object> spliterator() {
            return this.json.spliterator();
        }

        /** Returns a list of the JSONObject values. */
        public List<Object> toList() {
            return this.json.toList();
        }

        /** Returns a list of type T of the JSONObject values. */
        @SuppressWarnings("unchecked")
        public <T> List<T> toList(Class<T> cls) {
            ArrayList<T> list = new ArrayList<>();
            for (int i = 0; i < this.json.length(); i++) {
                if (JSONArray.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJSONArray(i));
                } else if (JSONObject.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJSONObject(i));
                } else if (JSONOptional.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJSONOptional(i));
                } else {
                    list.add((T) this.get(i));
                }
            }
            return list;
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

    /** JSONOption implementation of the JSONObject interface. */
    static class JSONObjectOption implements JSONObject {

        org.json.JSONObject json;

        public JSONObjectOption(org.json.JSONObject json) {
            this.json = json;
        }

        public JSONObjectOption(String encoding) {
            try {
                this.json = new org.json.JSONObject(encoding);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Throws a JSONException if the key is not in the JSONObject. */
        private void assertContains(String key) throws JSONException {
            if (!this.json.has(key)) {
                throw new JSONException(String.format("%s not found in JSONObject.", key));
            }
        }

        /** Returns the Object associated with the key.
         * @throws JSONException if the value associated with the key cannot be converted to an Object or no such key
         */
        public Object get(String key) throws JSONException {
            this.assertContains(key);
            try {
                return this.json.get(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the boolean value associated with the key.
         * @throws JSONException if the value associated with the key cannot be converted to a boolean or no such key
         */
        public boolean getBoolean(String key) throws JSONException {
            this.assertContains(key);
            try {
                return this.json.getBoolean(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the double value associated with the key.
         * @throws JSONException if the value associated with the key cannot be converted to a double or no such key
         */
        public double getDouble(String key) throws JSONException {
            this.assertContains(key);
            try {
                return this.json.getDouble(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the int value associated with the key.
         * @throws JSONException if the value associated with the key cannot be converted to an int or no such key
         */
        public int getInt(String key) throws JSONException {
            this.assertContains(key);
            try {
                return this.json.getInt(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns an Object associated with the key that implements the JSONArray interface.
         * @throws JSONException if the value associated with the key cannot be converted to a JSONArray or no such key
         */
        public JSONArray getJSONArray(String key) throws JSONException {
            this.assertContains(key);
            try {
                return new JSONArrayOption(this.json.getJSONArray(key));
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns an Object associated with the key that implements the JSONObject interface.
         * @throws JSONException if the value associated with the key cannot be converted to a JSONObject or no such key
         */
        public JSONObject getJSONObject(String key) throws JSONException {
            this.assertContains(key);
            try {
                return new JSONObjectOption(this.json.getJSONObject(key));
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns a JSONOptional wrapper for the Object associated with the key.
         * @throws JSONException if the value associated with the key cannot be converted to a JSONOptional or no such key
         */
        public JSONOptional getJSONOptional(String key) throws JSONException {
            this.assertContains(key);
            try {
                if (this.json.get(key) instanceof org.json.JSONArray) {
                    return new JSONOptional(new JSONArrayOption(this.json.getJSONArray(key)));
                }
                if (this.json.get(key) instanceof org.json.JSONObject) {
                    return new JSONOptional(new JSONObjectOption(this.json.getJSONObject(key)));
                }
                return JSONOptional.empty();
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the long value associated with the key.
         * @throws JSONException if the value associated with the key cannot be converted to a long or no such key
         */
        public long getLong(String key) throws JSONException {
            this.assertContains(key);
            try {
                return this.json.getLong(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String representation of the value associated with the key.
         * @throws JSONException if the Object has no such key
         */
        public String getString(String key) throws JSONException {
            this.assertContains(key);
            try {
                return this.json.getString(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns true if the JSONObject contains the specified key. */
        public boolean hasKey(String key) {
            return this.json.has(key);
        }

        /** Returns a collection of the JSONObject keys. */
        public Set<String> keySet() {
            return this.json.keySet();
        }

        /** Returns the number of key-value pairs stored in the JSONObject. */
        public int size() {
            return this.json.length();
        }

        /** Returns a Map representation of the JSONObject. */
        public Map<String, Object> toMap() {
            return this.json.toMap();
        }

        /** Returns a Map<String, T> representation of the JSONObject. */
        @SuppressWarnings("unchecked")
        public <T> Map<String, T> toMap(Class<T> cls) {
            Map<String, T> map = new HashMap<>();
            for (String key : this.keySet()) {
                if (JSONArray.class.isAssignableFrom(cls)) {
                    map.put(key, (T) this.getJSONArray(key));
                } else if (JSONObject.class.isAssignableFrom(cls)) {
                    map.put(key, (T) this.getJSONObject(key));
                } else if (JSONOptional.class.isAssignableFrom(cls)) {
                    map.put(key, (T) this.getJSONOptional(key));
                } else {
                    map.put(key, (T) this.get(key));
                }
            }
            return map;
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

        /** Returns a collection of the JSONObject values. */
        public Collection<Object> values() {
            return this.json.toMap().values();
        }

        /** Returns a collection of type T of the JSONObject values. */
        @SuppressWarnings("unchecked")
        public <T> Collection<T> values(Class<T> cls) {
            ArrayList<T> list = new ArrayList<>();
            for (String key : this.keySet()) {
                if (JSONArray.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJSONArray(key));
                } else if (JSONObject.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJSONObject(key));
                } else if (JSONOptional.class.isAssignableFrom(cls)) {
                    list.add((T) this.getJSONOptional(key));
                } else {
                    list.add((T) this.get(key));
                }
            }
            return list;
        }

    }

}
