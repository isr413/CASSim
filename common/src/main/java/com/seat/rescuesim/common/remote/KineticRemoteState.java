package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable state of a kinetic Remote. */
public abstract class KineticRemoteState extends RemoteState {

    protected Vector acceleration;
    protected Vector velocity;

    public KineticRemoteState(RemoteType type, String remoteID, Vector location, double battery, Vector velocity,
            Vector acceleration) {
        this(type, remoteID, location, battery, new ArrayList<SensorState>(), velocity, acceleration);
    }

    public KineticRemoteState(RemoteType type, String remoteID, Vector location, double battery,
            ArrayList<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(type, remoteID, location, battery, sensors);
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public KineticRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.velocity = new Vector(json.getJSONOption(RemoteConst.VELOCITY));
        this.acceleration = new Vector(json.getJSONOption(RemoteConst.ACCELERATION));
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    public abstract SerializableEnum getSpecType();

    public Vector getVelocity() {
        return this.velocity;
    }

    public boolean hasAcceleration() {
        return this.acceleration.getMagnitude() > 0;
    }

    public boolean hasVelocity() {
        return this.velocity.getMagnitude() > 0;
    }

    public boolean isKinetic() {
        return this.hasVelocity() || this.hasAcceleration();
    }

    public boolean isStatic() {
        return !this.isKinetic();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(RemoteConst.VELOCITY, this.velocity.toJSON());
        json.put(RemoteConst.ACCELERATION, this.acceleration.toJSON());
        return json;
    }

    public abstract JSONOption toJSON();

    public boolean equals(KineticRemoteState state) {
        return super.equals(state) && this.velocity.equals(state.velocity) &&
            this.acceleration.equals(state.acceleration);
    }

}
