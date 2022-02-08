package com.seat.rescuesim.common.json;

/** An option class to wrap JSONArray and JSONObject interface types. */
public class JSONOption {

    /** Returns an option wrapper for the JSONArray interface type. */
    public static JSONOption Array(JSONArray json) {
        return new JSONOption(json);
    }

    /** Returns the None type wrapper. */
    public static JSONOption None() {
        return new JSONOption();
    }

    /** Returns an option wrapper for the JSONObject interface type. */
    public static JSONOption Object(JSONObject json) {
        return new JSONOption(json);
    }

    /** Returns an option wrapper for the JSONArray or JSONObject interace types based on the encoding. */
    public static JSONOption String(String encoding) {
        if (encoding.isEmpty()) {
            return JSONOption.None();
        }
        if (encoding.charAt(0) == '[' && encoding.charAt(encoding.length()-1) == ']') {
            return new JSONOption(new JSONArrayOption(encoding));
        }
        if (encoding.charAt(0) == '{' && encoding.charAt(encoding.length()-1) == '}') {
            return new JSONOption(new JSONObjectOption(encoding));
        }
        return JSONOption.None();
    }

    private JSONArray arrayOption;
    private JSONObject objectOption;

    private JSONOption() {
        this.arrayOption = null;
        this.objectOption = null;
    }

    private JSONOption(JSONArray json) {
        this.arrayOption = json;
        this.objectOption = null;
    }

    private JSONOption(JSONObject json) {
        this.arrayOption = null;
        this.objectOption = json;
    }

    /** Returns true if this is the None option. */
    public boolean isNone() {
        return this.arrayOption == null && this.objectOption == null;
    }

    /** Returns true if this is the JSONArray option. */
    public boolean isSomeArray() {
        return this.arrayOption != null;
    }

    /** Returns true if this is the JSONObject option. */
    public boolean isSomeObject() {
        return this.objectOption != null;
    }

    /** Returns the JSONArray wrapped by this option.
     * @throws JSONException if the option being wrapped is not an Array option
     */
    public JSONArray someArray() throws JSONException {
        if (!this.isSomeArray()) {
            throw new JSONException("Cannot unwrap a null JSONArray option.");
        }
        return this.arrayOption;
    }

    /** Returns the JSONObject wrapped by this option.
     * @throws JSONException if the option being wrapped is not an Array option
     */
    public JSONObject someObject() throws JSONException {
        if (!this.isSomeObject()) {
            throw new JSONException("Cannot unwrap a null JSONObject option.");
        }
        return this.objectOption;
    }

    /** Returns the String representation of the option being wrapped. */
    public String toString() {
        if (this.isSomeArray()) {
            return this.arrayOption.toString();
        }
        if (this.isSomeObject()) {
            return this.objectOption.toString();
        }
        return "None";
    }

    /** JSONOption implementation of the JSONArray interface. */
    static class JSONArrayOption implements JSONArray {

        org.json.JSONArray json;

        public JSONArrayOption(org.json.JSONArray json) {
            this.json = json;
        }

        public JSONArrayOption(String encoding) {
            this.json = new org.json.JSONArray(encoding);
        }

        /** Throws an IndexOutOfBoundsException if the idx is out of bounds. */
        private void assertBounds(int idx) throws IndexOutOfBoundsException {
            if (idx < 0 || this.json.length() <= idx) {
                throw new IndexOutOfBoundsException(idx);
            }
        }

        /** Returns the double value at index idx.
         * @throws IndexOutOfBoundsException if the idx is out of bounds
         * @throws JSONException if the value at idx cannot be converted to a double
         */
        public double getDouble(int idx) throws IndexOutOfBoundsException, JSONException {
            this.assertBounds(idx);
            try {
                return this.json.getDouble(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the int value at index idx.
         * @throws IndexOutOfBoundsException if the idx is out of bounds
         * @throws JSONException if the value at idx cannot be converted to an int
         */
        public int getInt(int idx) throws IndexOutOfBoundsException, JSONException {
            this.assertBounds(idx);
            try {
                return this.json.getInt(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns an Object at index idx that implements the JSONArray interface.
         * @throws IndexOutOfBoundsException if the idx is out of bounds
         * @throws JSONException if the value at idx cannot be converted to a JSONArray
         */
        public JSONArray getJSONArray(int idx) throws IndexOutOfBoundsException, JSONException {
            this.assertBounds(idx);
            try {
                return new JSONArrayOption(this.json.getJSONArray(idx));
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns an Object at index idx that implements the JSONObject interface.
         * @throws IndexOutOfBoundsException if the idx is out of bounds
         * @throws JSONException if the value at idx cannot be converted to a JSONsObject
         */
        public JSONObject getJSONObject(int idx) throws IndexOutOfBoundsException, JSONException {
            this.assertBounds(idx);
            try {
                return new JSONObjectOption(this.json.getJSONObject(idx));
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns a JSONOption wrapper for the Object at index idx.
         * @throws IndexOutOfBoundsException if the idx is out of bounds
         * @throws JSONException if the value at idx cannot be converted to a JSONOption
         */
        public JSONOption getJSONOption(int idx) throws IndexOutOfBoundsException, JSONException {
            this.assertBounds(idx);
            try {
                if (this.json.get(idx) instanceof org.json.JSONArray) {
                    return new JSONOption(new JSONArrayOption(this.json.getJSONArray(idx)));
                }
                if (this.json.get(idx) instanceof org.json.JSONObject) {
                    return new JSONOption(new JSONObjectOption(this.json.getJSONObject(idx)));
                }
                return JSONOption.None();
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String representation of the value at index idx.
         * @throws IndexOutOfBoundsException if the idx is out of bounds
         */
        public String getString(int idx) throws IndexOutOfBoundsException {
            this.assertBounds(idx);
            try {
                return this.json.getString(idx);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String representation of the JSONArray. */
        public String toString() {
            return this.json.toString();
        }

    }

    /** JSONOption implementation of the JSONObject interface. */
    static class JSONObjectOption implements JSONObject {

        org.json.JSONObject json;

        public JSONObjectOption(org.json.JSONObject json) {
            this.json = json;
        }

        public JSONObjectOption(String encoding) {
            this.json = new org.json.JSONObject(encoding);
        }

        /** Throws an IndexOutOfBoundsException if the key is not in the JSONObject. */
        private void assertContains(String key) throws IndexOutOfBoundsException {
            if (!this.json.has(key)) {
                throw new IndexOutOfBoundsException(String.format("%s not found in JSONObject.", key));
            }
        }

        /** Returns the double value associated with the key.
         * @throws IndexOutOfBoundsException if the Object has no such key
         * @throws JSONException if the value associated with the key cannot be converted to a double
         */
        public double getDouble(String key) throws IndexOutOfBoundsException, JSONException {
            this.assertContains(key);
            try {
                return this.json.getDouble(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the int value associated with the key.
         * @throws IndexOutOfBoundsException if the Object has no such key
         * @throws JSONException if the value associated with the key cannot be converted to an int
         */
        public int getInt(String key) throws IndexOutOfBoundsException, JSONException {
            this.assertContains(key);
            try {
                return this.json.getInt(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns an Object associated with the key that implements the JSONArray interface.
         * @throws IndexOutOfBoundsException if the Object has no such key
         * @throws JSONException if the value associated with the key cannot be converted to a JSONArray
         */
        public JSONArray getJSONArray(String key) throws IndexOutOfBoundsException, JSONException {
            this.assertContains(key);
            try {
                return new JSONArrayOption(this.json.getJSONArray(key));
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns an Object associated with the key that implements the JSONObject interface.
         * @throws IndexOutOfBoundsException if the Object has no such key
         * @throws JSONException if the value associated with the key cannot be converted to a JSONObject
         */
        public JSONObject getJSONObject(String key) throws IndexOutOfBoundsException, JSONException {
            this.assertContains(key);
            try {
                return new JSONObjectOption(this.json.getJSONObject(key));
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns a JSONOption wrapper for the Object associated with the key.
         * @throws IndexOutOfBoundsException if the Object has no such key
         * @throws JSONException if the value associated with the key cannot be converted to a JSONOption
         */
        public JSONOption getJSONOption(String key) throws IndexOutOfBoundsException, JSONException {
            this.assertContains(key);
            try {
                if (this.json.get(key) instanceof org.json.JSONArray) {
                    return new JSONOption(new JSONArrayOption(this.json.getJSONArray(key)));
                }
                if (this.json.get(key) instanceof org.json.JSONObject) {
                    return new JSONOption(new JSONObjectOption(this.json.getJSONObject(key)));
                }
                return JSONOption.None();
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String representation of the value associated with the key.
         * @throws IndexOutOfBoundsException if the Object has no such key
         */
        public String getString(String key) throws IndexOutOfBoundsException {
            this.assertContains(key);
            try {
                return this.json.getString(key);
            } catch (org.json.JSONException e) {
                throw new JSONException(e.toString());
            }
        }

        /** Returns the String representation of the JSONObject. */
        public String toString() {
            return this.json.toString();
        }

    }

}
