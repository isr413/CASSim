package com.seat.rescuesim.common.remote.kinetic;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable state of a kinetic Remote. */
public abstract class KineticRemoteState extends RemoteState {

    protected Vector acceleration;
    protected KineticRemoteType type;
    protected Vector velocity;

    public KineticRemoteState(KineticRemoteType type, String remoteID, Vector location, double battery, Vector velocity,
            Vector acceleration) {
        this(type, remoteID, location, battery, new ArrayList<SensorState>(), velocity, acceleration);
    }

    public KineticRemoteState(KineticRemoteType type, String remoteID, Vector location, double battery,
            ArrayList<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(RemoteType.KINETIC, remoteID, location, battery, sensors);
        this.type = type;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public KineticRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = KineticRemoteType.decodeType(json);
        this.velocity = new Vector(json.getJSONOption(KineticRemoteConst.VELOCITY));
        this.acceleration = new Vector(json.getJSONOption(KineticRemoteConst.ACCELERATION));
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    public KineticRemoteType getSpecType() {
        return this.type;
    }

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
        json.put(KineticRemoteConst.KINETIC_REMOTE_TYPE, this.type.getType());
        json.put(KineticRemoteConst.VELOCITY, this.velocity.toJSON());
        json.put(KineticRemoteConst.ACCELERATION, this.acceleration.toJSON());
        return json;
    }

    public boolean equals(KineticRemoteState state) {
        return super.equals(state) && this.type.equals(state.type) && this.velocity.equals(state.velocity) &&
            this.acceleration.equals(state.acceleration);
    }

}
