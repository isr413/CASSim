package com.seat.sim.common.json;

/** An optional class to wrap JSONArray and JSONObject interface types. */
public class JSONOptional {

    /** Returns true if the input string is a JSON encoding. */
    public static boolean isJSON(String encoding) {
        return JSONOptional.isJSON(encoding, 0, encoding.length()-1);
    }

    private static boolean isJSON(String encoding, int i, int j) {
        return JSONOptional.isJSONArray(encoding, i, j) || JSONOptional.isJSONObject(encoding, i, j);
    }

    /** Returns true if the input string is a JSON Array encoding. */
    public static boolean isJSONArray(String encoding) {
        return JSONOptional.isJSONArray(encoding, 0, encoding.length()-1);
    }

    private static boolean isJSONArray(String encoding, int i, int j) {
        return 0 <= i && i < encoding.length() && 0 <= j && j < encoding.length() && i < j &&
            encoding.charAt(i) == '[' && encoding.charAt(j) == ']';
    }

    /** Returns true if the input string is a JSON Object encoding. */
    public static boolean isJSONObject(String encoding) {
        return JSONOptional.isJSONObject(encoding, 0, encoding.length()-1);
    }

    private static boolean isJSONObject(String encoding, int i, int j) {
        return 0 <= i && i < encoding.length() && 0 <= j && j < encoding.length() && i < j &&
            encoding.charAt(i) == '{' && encoding.charAt(j) == '}';
    }

    /** Returns true if the input string is a quoted String of a JSON encoding. */
    public static boolean isQuotedJSON(String encoding) {
        return JSONOptional.isQuotedJSON(encoding, 0, encoding.length()-1);
    }

    private static boolean isQuotedJSON(String encoding, int i, int j) {
        return 0 <= i && i < encoding.length() && 0 <= j && j < encoding.length() && i < j &&
            ((encoding.charAt(i) == '"' && encoding.charAt(j) == '"') ||
                (encoding.charAt(i) == '\'' && encoding.charAt(j) == '\'')) &&
            JSONOptional.isJSON(encoding, i+1, j-1);
    }

    /** Returns an optional wrapper for the JSONArray interface type. */
    public static JSONOptional Array(JSONArray json) {
        return new JSONOptional(json);
    }

    /** Returns the None type wrapper. */
    public static JSONOptional None() {
        return new JSONOptional();
    }

    /** Returns an optional wrapper for the JSONObject interface type. */
    public static JSONOptional Object(JSONObject json) {
        return new JSONOptional(json);
    }

    /** Returns an optional wrapper for the JSONArray or JSONObject interace types based on the encoding. */
    public static JSONOptional QuotedString(String encoding) {
        if (!JSONOptional.isQuotedJSON(encoding)) {
            return JSONOptional.None();
        }
        return JSONOptional.String(encoding.substring(0, encoding.length()-1));
    }

    /** Returns an optional wrapper for the JSONArray or JSONObject interace types based on the encoding. */
    public static JSONOptional String(String encoding) {
        if (JSONOptional.isJSONArray(encoding)) {
            return new JSONOptional(new JSONArrayOption(encoding));
        }
        if (JSONOptional.isJSONObject(encoding)) {
            return new JSONOptional(new JSONObjectOption(encoding));
        }
        return JSONOptional.None();
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

    /** Returns true if this is the None optional. */
    public boolean isNone() {
        return this.arrayOption == null && this.objectOption == null;
    }

    /** Returns true if this is the JSONArray optional. */
    public boolean isSomeArray() {
        return this.arrayOption != null;
    }

    /** Returns true if this is the JSONObject optional. */
    public boolean isSomeObject() {
        return this.objectOption != null;
    }

    /** Returns the JSONArray wrapped by this optional.
     * @throws JSONException if the optional being wrapped is not an Array optional
     */
    public JSONArray someArray() throws JSONException {
        if (!this.isSomeArray()) {
            throw new JSONException("Cannot unwrap a null JSONArray optional.");
        }
        return this.arrayOption;
    }

    /** Returns the JSONObject wrapped by this optional.
     * @throws JSONException if the optional being wrapped is not an Object optional
     */
    public JSONObject someObject() throws JSONException {
        if (!this.isSomeObject()) {
            throw new JSONException("Cannot unwrap a null JSONObject optional.");
        }
        return this.objectOption;
    }

    /** Returns the String representation of the optional being wrapped.
     * @throws JSONException if the JSONOption cannot be converted to a JSON string
     */
    public String toString() throws JSONException {
        if (this.isSomeArray()) {
            return this.arrayOption.toString();
        }
        if (this.isSomeObject()) {
            return this.objectOption.toString();
        }
        return "None";
    }

    /** Returns the String representation of the optional being wrapped (pretty printed).
     * @throws JSONException if the JSONOption cannot be converted to a JSON string
     */
    public String toString(int tabSize) throws JSONException {
        if (this.isSomeArray()) {
            return this.arrayOption.toString(tabSize);
        }
        if (this.isSomeObject()) {
            return this.objectOption.toString(tabSize);
        }
        return "None";
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

        /** Returns a JSONOption wrapper for the Object at index idx.
         * @throws JSONException if the value at idx cannot be converted to a JSONOption or is out of bounds
         */
        public JSONOptional getJSONOption(int idx) throws JSONException {
            this.assertBounds(idx);
            try {
                if (this.json.get(idx) instanceof org.json.JSONArray) {
                    return new JSONOptional(new JSONArrayOption(this.json.getJSONArray(idx)));
                }
                if (this.json.get(idx) instanceof org.json.JSONObject) {
                    return new JSONOptional(new JSONObjectOption(this.json.getJSONObject(idx)));
                }
                return JSONOptional.None();
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

        /** Returns the number of elements in the JSONArray. */
        public int length() {
            return this.json.length();
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

        /** Returns a JSONOption wrapper for the Object associated with the key.
         * @throws JSONException if the value associated with the key cannot be converted to a JSONOption or no such key
         */
        public JSONOptional getJSONOption(String key) throws JSONException {
            this.assertContains(key);
            try {
                if (this.json.get(key) instanceof org.json.JSONArray) {
                    return new JSONOptional(new JSONArrayOption(this.json.getJSONArray(key)));
                }
                if (this.json.get(key) instanceof org.json.JSONObject) {
                    return new JSONOptional(new JSONObjectOption(this.json.getJSONObject(key)));
                }
                return JSONOptional.None();
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

        /** Returns the number of key-value pairs stored in the JSONObject. */
        public int size() {
            return this.json.length();
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
