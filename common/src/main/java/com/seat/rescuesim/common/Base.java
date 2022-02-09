package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.util.Debugger;

public class Base extends JSONAble {
    private static final String BASE_LOCATION = "location";
    private static final String BASE_SENSORS = "sensors";

    private Vector location;
    private HashMap<SensorType, SensorSpecification> sensors;

    public Base(JSONObject json) {
        super(json);
    }

    public Base(JSONOption option) {
        super(option);
    }

    public Base(String encoding) {
        super(encoding);
    }

    public Base() {
        this(new Vector(), new HashMap<SensorType, SensorSpecification>());
    }

    public Base(Vector location) {
        this(location, new HashMap<SensorType, SensorSpecification>());
    }

    public Base(Vector location, SensorSpecification[] sensors) {
        this(location, new ArrayList<SensorSpecification>(Arrays.asList(sensors)));
    }

    public Base(Vector location, ArrayList<SensorSpecification> sensors) {
        this(location, new HashMap<SensorType, SensorSpecification>());
        for (SensorSpecification spec : sensors) {
            this.sensors.put(spec.getSensorType(), spec);
        }
    }

    public Base(Vector location, HashMap<SensorType, SensorSpecification> sensors) {
        this.location = location;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.location = new Vector(json.getJSONArray(Base.BASE_LOCATION));
        this.sensors = new HashMap<>();
        JSONArray jsonSensors = json.getJSONArray(Base.BASE_SENSORS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            SensorSpecification spec = new SensorSpecification(jsonSensors.getJSONObject(i));
            this.sensors.put(spec.getSensorType(), spec);
        }
    }

    public String getLabel() {
        return String.format("<%s>", this.location.toString());
    }

    public Vector getLocation() {
        return this.location;
    }

    public ArrayList<SensorSpecification> getSensors() {
        return new ArrayList<>(this.sensors.values());
    }

    public SensorSpecification getSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err(String.format("No sensor with type %s found on base %s",
                type.getLabel(), this.getLabel()));
            return null;
        }
        return this.sensors.get(type);
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        return this.sensors.containsKey(type);
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Base.BASE_LOCATION, this.location.toJSON());
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (SensorSpecification spec : this.sensors.values()) {
            jsonSensors.put(spec.toJSON());
        }
        json.put(Base.BASE_SENSORS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(Base base) {
        return this.location.equals(base.location) && this.sensors.equals(base.sensors);
    }

}
