package com.seat.sim.common.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonBuilderTest {

    @Test
    public void builderShouldBuildJsonArraysFromPrimitives() {
        JsonArrayBuilder test = JsonBuilder.Array();
        test.put(1.2);
        test.put(3);
        assertEquals("[1.2,3]", test.toString());
    }

    @Test
    public void builderShouldBuildJsonArraysFromJson() {
        JsonArrayBuilder jsonArray = JsonBuilder.Array();
        jsonArray.put(1.2);
        jsonArray.put(3);
        JsonArrayBuilder test = JsonBuilder.Array();
        test.put(jsonArray.toJson());
        assertEquals("[[1.2,3]]", test.toString());
        JsonObjectBuilder jsonObject = JsonBuilder.Object();
        jsonObject.put("0", 1.2);
        jsonObject.put("1", 3);
        test = JsonBuilder.Array();
        test.put(jsonObject.toJson().getJsonObject());
        assertEquals("[{\"0\":1.2,\"1\":3}]", test.toString());
        test = JsonBuilder.Array();
        test.put(jsonArray.toJson());
        test.put(jsonObject.toJson());
        assertEquals("[[1.2,3],{\"0\":1.2,\"1\":3}]", test.toString());
    }

    @Test
    public void builderShouldBuildJsonArraysFromStrings() {
        JsonArrayBuilder test = JsonBuilder.Array();
        test.put("hello");
        test.put("world");
        assertEquals("[\"hello\",\"world\"]", test.toString());
    }

    @Test
    public void builderShouldBuildJsonObjectsFromPrimitives() {
        JsonObjectBuilder test = JsonBuilder.Object();
        test.put("0", 1.2);
        test.put("1", 3);
        assertEquals("{\"0\":1.2,\"1\":3}", test.toString());
    }

    @Test
    public void builderShouldBuildJsonObjectsFromJson() {
        JsonArrayBuilder jsonArray = JsonBuilder.Array();
        jsonArray.put(1.2);
        jsonArray.put(3);
        JsonObjectBuilder test = JsonBuilder.Object();
        test.put("0", jsonArray.toJson());
        assertEquals("{\"0\":[1.2,3]}", test.toString());
        JsonObjectBuilder jsonObject = JsonBuilder.Object();
        jsonObject.put("0", 1.2);
        jsonObject.put("1", 3);
        test = JsonBuilder.Object();
        test.put("0", jsonObject.toJson().getJsonObject());
        assertEquals("{\"0\":{\"0\":1.2,\"1\":3}}", test.toString());
        test = JsonBuilder.Object();
        test.put("0", jsonArray.toJson());
        test.put("1", jsonObject.toJson());
        assertEquals("{\"0\":[1.2,3],\"1\":{\"0\":1.2,\"1\":3}}", test.toString());
    }

    @Test
    public void builderShouldBuildJsonObjectsFromStrings() {
        JsonObjectBuilder test = JsonBuilder.Object();
        test.put("0", "hello");
        test.put("1", "world");
        assertEquals("{\"0\":\"hello\",\"1\":\"world\"}", test.toString());
    }
}
