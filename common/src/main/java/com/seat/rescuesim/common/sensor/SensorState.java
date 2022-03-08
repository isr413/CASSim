package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;

public class SensorState extends RemoteState {
    private static final String DATA_FORMAT = "format";
    private static final String SENSOR_DATA = "data";

    private String data;
    private String format;

    public SensorState(JSONObject json) {
        super(json);
    }

    public SensorState(JSONOption option) {
        super(option);
    }

    public SensorState(String encoding) {
        super(encoding);
    }

    public SensorState(String remote, String data) {
        this(remote, data, "");
    }

    public SensorState(String remote, String data, String format) {
        super(RemoteType.SENSOR, remote);
        this.data = data;
        this.format = format;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        if (json.hasKey(SensorState.SENSOR_DATA)) {
            this.data = json.getString(SensorState.SENSOR_DATA);
        }
        if (json.hasKey(SensorState.DATA_FORMAT)) {
            this.format = json.getString(SensorState.DATA_FORMAT);
        }
    }

    public String getData() {
        return this.data;
    }

    public String getDataFormat() {
        return this.format;
    }

    public boolean hasData() {
        return !this.data.isEmpty();
    }

    public boolean hasDataFormat() {
        return !this.format.isEmpty();
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasData()) {
            json.put(SensorState.SENSOR_DATA, this.data);
        }
        if (this.hasDataFormat()) {
            json.put(SensorState.DATA_FORMAT, this.format);
        }
        return json.toJSON();
    }

    public boolean equals(SensorState state) {
        return this.data.equals(state.data);
    }

}
