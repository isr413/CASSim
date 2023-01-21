package com.seat.sim.common.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonTest {

    @Test
    public void arrayOptionIsgetJsonArray() {
        String json = "[0,1,2]";
        Json test = Json.of(json);
        assertTrue(test.isJsonArray());
        assertFalse(test.isJsonObject());
        assertEquals(json, test.toString());
        assertNotSame(json, test.toString());
    }

    @Test
    public void arrayOptionSupportsPrimitives() {
        String json = "[1.2,3]";
        Json test = Json.of(json);
        assertEquals(1.2, test.getJsonArray().getDouble(0), 0);
        assertEquals(3, test.getJsonArray().getInt(1));
    }

    @Test
    public void arrayOptionSupportsJson() {
        String jsonArray = "[1.2,3]";
        String jsonObject = "{\"0\":1.2,\"1\":3}";
        String json = String.format("[%s,%s]", jsonArray, jsonObject);
        Json test = Json.of(json);
        assertEquals(jsonArray, test.getJsonArray().getJsonArray(0).toString());
        assertEquals(jsonObject, test.getJsonArray().getJsonObject(1).toString());
        assertEquals(jsonArray, test.getJsonArray().getJson(0).toString());
        assertEquals(jsonObject, test.getJsonArray().getJson(1).toString());
    }

    @Test
    public void arrayOptionSupportsStrings() {
        String foo = "foo";
        String bar = "bar";
        String json = String.format("[%s,%s]", foo, bar);
        Json test = Json.of(json);
        assertEquals(foo, test.getJsonArray().getString(0));
        assertEquals(bar, test.getJsonArray().getString(1));
    }

    @Test
    public void objectOptionIsgetJsonObject() {
        String json = "{\"0\":1.2,\"1\":3}";
        Json test = Json.of(json);
        assertFalse(test.isJsonArray());
        assertTrue(test.isJsonObject());
        assertEquals(json, test.toString());
        assertNotSame(json, test.toString());
    }

    @Test
    public void objectOptionSupportsPrimitives() {
        String json = "{\"0\":1.2,\"1\":3}";
        Json test = Json.of(json);
        assertEquals(1.2, test.getJsonObject().getDouble("0"), 0);
        assertEquals(3, test.getJsonObject().getInt("1"));
    }

    @Test
    public void objectOptionSupportsJson() {
        String jsonArray = "[1.2,3]";
        String jsonObject = "{\"0\":1.2,\"1\":3}";
        String json = String.format("{\"0\":%s,\"1\":%s}", jsonArray, jsonObject);
        Json test = Json.of(json);
        assertEquals(jsonArray, test.getJsonObject().getJsonArray("0").toString());
        assertEquals(jsonObject, test.getJsonObject().getJsonObject("1").toString());
        assertEquals(jsonArray, test.getJsonObject().getJson("0").toString());
        assertEquals(jsonObject, test.getJsonObject().getJson("1").toString());
    }

    @Test
    public void objectOptionSupportsStrings() {
        String foo = "foo";
        String bar = "bar";
        String json = String.format("{\"0\":%s,\"1\":%s}", foo, bar);
        Json test = Json.of(json);
        assertEquals(foo, test.getJsonObject().getString("0"));
        assertEquals(bar, test.getJsonObject().getString("1"));
    }
}
