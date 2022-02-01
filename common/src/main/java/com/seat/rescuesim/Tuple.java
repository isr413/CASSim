package com.seat.rescuesim;

import org.json.JSONArray;

public class Tuple<K, V> {
    protected K first;
    protected V second;

    public Tuple(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }

    public String toString() {
        JSONArray json = new JSONArray();
        json.put(this.first.toString());
        json.put(this.second.toString());
        return json.toString();
    }

}
