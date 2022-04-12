package com.seat.sim.common.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JSONBuilderTest {

    @Test
    public void builderShouldBuildJSONArraysFromPrimitives() {
        JSONArrayBuilder test = JSONBuilder.Array();
        test.put(1.2);
        test.put(3);
        assertEquals("[1.2,3]", test.toString());
    }

    @Test
    public void builderShouldBuildJSONArraysFromJSON() {
        JSONArrayBuilder jsonArray = JSONBuilder.Array();
        jsonArray.put(1.2);
        jsonArray.put(3);
        JSONArrayBuilder test = JSONBuilder.Array();
        test.put(jsonArray.toJSON());
        assertEquals("[[1.2,3]]", test.toString());
        JSONObjectBuilder jsonObject = JSONBuilder.Object();
        jsonObject.put("0", 1.2);
        jsonObject.put("1", 3);
        test = JSONBuilder.Array();
        test.put(jsonObject.toJSON().someObject());
        assertEquals("[{\"0\":1.2,\"1\":3}]", test.toString());
        test = JSONBuilder.Array();
        test.put(jsonArray.toJSON());
        test.put(jsonObject.toJSON());
        assertEquals("[[1.2,3],{\"0\":1.2,\"1\":3}]", test.toString());
    }

    @Test
    public void builderShouldBuildJSONArraysFromStrings() {
        JSONArrayBuilder test = JSONBuilder.Array();
        test.put("hello");
        test.put("world");
        assertEquals("[\"hello\",\"world\"]", test.toString());
    }

    @Test
    public void builderShouldBuildJSONObjectsFromPrimitives() {
        JSONObjectBuilder test = JSONBuilder.Object();
        test.put("0", 1.2);
        test.put("1", 3);
        assertEquals("{\"0\":1.2,\"1\":3}", test.toString());
    }

    @Test
    public void builderShouldBuildJSONObjectsFromJSON() {
        JSONArrayBuilder jsonArray = JSONBuilder.Array();
        jsonArray.put(1.2);
        jsonArray.put(3);
        JSONObjectBuilder test = JSONBuilder.Object();
        test.put("0", jsonArray.toJSON());
        assertEquals("{\"0\":[1.2,3]}", test.toString());
        JSONObjectBuilder jsonObject = JSONBuilder.Object();
        jsonObject.put("0", 1.2);
        jsonObject.put("1", 3);
        test = JSONBuilder.Object();
        test.put("0", jsonObject.toJSON().someObject());
        assertEquals("{\"0\":{\"0\":1.2,\"1\":3}}", test.toString());
        test = JSONBuilder.Object();
        test.put("0", jsonArray.toJSON());
        test.put("1", jsonObject.toJSON());
        assertEquals("{\"0\":[1.2,3],\"1\":{\"0\":1.2,\"1\":3}}", test.toString());
    }

    @Test
    public void builderShouldBuildJSONObjectsFromStrings() {
        JSONObjectBuilder test = JSONBuilder.Object();
        test.put("0", "hello");
        test.put("1", "world");
        assertEquals("{\"0\":\"hello\",\"1\":\"world\"}", test.toString());
    }

}
