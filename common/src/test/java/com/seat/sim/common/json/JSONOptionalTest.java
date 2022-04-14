package com.seat.sim.common.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSONOptionalTest {

    @Test
    public void arrayOptionIsgetArray() {
        String json = "[0,1,2]";
        JSONOptional test = JSONOptional.of(json);
        assertFalse(!test.isPresent());
        assertTrue(test.isPresentArray());
        assertFalse(test.isPresentObject());
        assertEquals(json, test.toString());
        assertNotSame(json, test.toString());
    }

    @Test
    public void arrayOptionSupportsPrimitives() {
        String json = "[1.2,3]";
        JSONOptional test = JSONOptional.of(json);
        assertEquals(1.2, test.getArray().getDouble(0), 0);
        assertEquals(3, test.getArray().getInt(1));
    }

    @Test
    public void arrayOptionSupportsJSON() {
        String jsonArray = "[1.2,3]";
        String jsonObject = "{\"0\":1.2,\"1\":3}";
        String json = String.format("[%s,%s]", jsonArray, jsonObject);
        JSONOptional test = JSONOptional.of(json);
        assertEquals(jsonArray, test.getArray().getJSONArray(0).toString());
        assertEquals(jsonObject, test.getArray().getJSONObject(1).toString());
        assertEquals(jsonArray, test.getArray().getJSONOption(0).toString());
        assertEquals(jsonObject, test.getArray().getJSONOption(1).toString());
    }

    @Test
    public void arrayOptionSupportsStrings() {
        String foo = "foo";
        String bar = "bar";
        String json = String.format("[%s,%s]", foo, bar);
        JSONOptional test = JSONOptional.of(json);
        assertEquals(foo, test.getArray().getString(0));
        assertEquals(bar, test.getArray().getString(1));
    }

    @Test
    public void noneOptionIsNone() {
        JSONOptional test = JSONOptional.empty();
        assertTrue(!test.isPresent());
        assertFalse(test.isPresentArray());
        assertFalse(test.isPresentObject());
        assertEquals("", test.toString());
        test = JSONOptional.ofNullable("");
        assertTrue(!test.isPresent());
        assertFalse(test.isPresentArray());
        assertFalse(test.isPresentObject());
        assertEquals("", test.toString());
    }

    @Test
    public void objectOptionIsgetObject() {
        String json = "{\"0\":1.2,\"1\":3}";
        JSONOptional test = JSONOptional.of(json);
        assertFalse(!test.isPresent());
        assertFalse(test.isPresentArray());
        assertTrue(test.isPresentObject());
        assertEquals(json, test.toString());
        assertNotSame(json, test.toString());
    }

    @Test
    public void objectOptionSupportsPrimitives() {
        String json = "{\"0\":1.2,\"1\":3}";
        JSONOptional test = JSONOptional.of(json);
        assertEquals(1.2, test.getObject().getDouble("0"), 0);
        assertEquals(3, test.getObject().getInt("1"));
    }

    @Test
    public void objectOptionSupportsJSON() {
        String jsonArray = "[1.2,3]";
        String jsonObject = "{\"0\":1.2,\"1\":3}";
        String json = String.format("{\"0\":%s,\"1\":%s}", jsonArray, jsonObject);
        JSONOptional test = JSONOptional.of(json);
        assertEquals(jsonArray, test.getObject().getJSONArray("0").toString());
        assertEquals(jsonObject, test.getObject().getJSONObject("1").toString());
        assertEquals(jsonArray, test.getObject().getJSONOption("0").toString());
        assertEquals(jsonObject, test.getObject().getJSONOption("1").toString());
    }

    @Test
    public void objectOptionSupportsStrings() {
        String foo = "foo";
        String bar = "bar";
        String json = String.format("{\"0\":%s,\"1\":%s}", foo, bar);
        JSONOptional test = JSONOptional.of(json);
        assertEquals(foo, test.getObject().getString("0"));
        assertEquals(bar, test.getObject().getString("1"));
    }

}
