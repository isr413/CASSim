package com.seat.sim.common.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSONOptionTest {

    @Test
    public void arrayOptionIsSomeArray() {
        String json = "[0,1,2]";
        JSONOptional test = JSONOptional.String(json);
        assertFalse(test.isNone());
        assertTrue(test.isSomeArray());
        assertFalse(test.isSomeObject());
        assertEquals(json, test.toString());
        assertNotSame(json, test.toString());
    }

    @Test
    public void arrayOptionSupportsPrimitives() {
        String json = "[1.2,3]";
        JSONOptional test = JSONOptional.String(json);
        assertEquals(1.2, test.someArray().getDouble(0), 0);
        assertEquals(3, test.someArray().getInt(1));
    }

    @Test
    public void arrayOptionSupportsJSON() {
        String jsonArray = "[1.2,3]";
        String jsonObject = "{\"0\":1.2,\"1\":3}";
        String json = String.format("[%s,%s]", jsonArray, jsonObject);
        JSONOptional test = JSONOptional.String(json);
        assertEquals(jsonArray, test.someArray().getJSONArray(0).toString());
        assertEquals(jsonObject, test.someArray().getJSONObject(1).toString());
        assertEquals(jsonArray, test.someArray().getJSONOption(0).toString());
        assertEquals(jsonObject, test.someArray().getJSONOption(1).toString());
    }

    @Test
    public void arrayOptionSupportsStrings() {
        String foo = "foo";
        String bar = "bar";
        String json = String.format("[%s,%s]", foo, bar);
        JSONOptional test = JSONOptional.String(json);
        assertEquals(foo, test.someArray().getString(0));
        assertEquals(bar, test.someArray().getString(1));
    }

    @Test
    public void noneOptionIsNone() {
        JSONOptional test = JSONOptional.None();
        assertTrue(test.isNone());
        assertFalse(test.isSomeArray());
        assertFalse(test.isSomeObject());
        assertEquals("None", test.toString());
        test = JSONOptional.String("");
        assertTrue(test.isNone());
        assertFalse(test.isSomeArray());
        assertFalse(test.isSomeObject());
        assertEquals("None", test.toString());
    }

    @Test
    public void objectOptionIsSomeObject() {
        String json = "{\"0\":1.2,\"1\":3}";
        JSONOptional test = JSONOptional.String(json);
        assertFalse(test.isNone());
        assertFalse(test.isSomeArray());
        assertTrue(test.isSomeObject());
        assertEquals(json, test.toString());
        assertNotSame(json, test.toString());
    }

    @Test
    public void objectOptionSupportsPrimitives() {
        String json = "{\"0\":1.2,\"1\":3}";
        JSONOptional test = JSONOptional.String(json);
        assertEquals(1.2, test.someObject().getDouble("0"), 0);
        assertEquals(3, test.someObject().getInt("1"));
    }

    @Test
    public void objectOptionSupportsJSON() {
        String jsonArray = "[1.2,3]";
        String jsonObject = "{\"0\":1.2,\"1\":3}";
        String json = String.format("{\"0\":%s,\"1\":%s}", jsonArray, jsonObject);
        JSONOptional test = JSONOptional.String(json);
        assertEquals(jsonArray, test.someObject().getJSONArray("0").toString());
        assertEquals(jsonObject, test.someObject().getJSONObject("1").toString());
        assertEquals(jsonArray, test.someObject().getJSONOption("0").toString());
        assertEquals(jsonObject, test.someObject().getJSONOption("1").toString());
    }

    @Test
    public void objectOptionSupportsStrings() {
        String foo = "foo";
        String bar = "bar";
        String json = String.format("{\"0\":%s,\"1\":%s}", foo, bar);
        JSONOptional test = JSONOptional.String(json);
        assertEquals(foo, test.someObject().getString("0"));
        assertEquals(bar, test.someObject().getString("1"));
    }

}
