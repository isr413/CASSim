package com.seat.rescuesim.common;

import org.json.JSONArray;

public class Tuple<K, V> {
    protected K first;
    protected V second;

    public Tuple(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public Tuple(Tuple<K, V> tuple) {
        this.first = tuple.first;
        this.second = tuple.second;
    }

    public K getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }

    public JSONArray toJSON() {
        JSONArray json = new JSONArray();
        json.put(this.first.toString());
        json.put(this.second.toString());
        return json;
    }

    public String toString() {
        return this.toJSON().toString();
    }

    public boolean equals(Tuple<K, V> tuple) {
        return this.first.equals(tuple.first) && this.second.equals(tuple.second);
    }

}
