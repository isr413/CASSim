package com.seat.rescuesim.common.remote.mobile;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable state of a Mobile Remote. */
public class MobileRemoteState extends RemoteState {
    public static final String ACCELERATION = "acceleration";
    public static final String VELOCITY = "velocity";

    private Vector acceleration;
    private Vector velocity;

    public MobileRemoteState(String remoteID, Vector location, double battery, boolean active, Vector velocity,
            Vector acceleration) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, remoteID, location, battery, active,
            null, velocity, acceleration);
    }

    public MobileRemoteState(String remoteID, Vector location, double battery, boolean active,
            Collection<SensorState> sensors, Vector velocity, Vector acceleration) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, remoteID, location, battery, active, sensors, velocity,
            acceleration);
    }

    public MobileRemoteState(RemoteType remoteType, String remoteID, Vector location, double battery, boolean active,
            Vector velocity, Vector acceleration) {
        this(remoteType, remoteID, location, battery, active, null, velocity, acceleration);
    }

    public MobileRemoteState(RemoteType remoteType, String remoteID, Vector location, double battery, boolean active,
            Collection<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(remoteType, remoteID, location, battery, active, sensors);
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public MobileRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.velocity = (json.hasKey(MobileRemoteState.VELOCITY)) ?
            new Vector(json.getJSONOption(MobileRemoteState.VELOCITY)) :
            new Vector();
        this.acceleration = (json.hasKey(MobileRemoteState.ACCELERATION)) ?
            new Vector(json.getJSONOption(MobileRemoteState.ACCELERATION)) :
            new Vector();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(MobileRemoteState.VELOCITY, this.velocity.toJSON());
        json.put(MobileRemoteState.ACCELERATION, this.acceleration.toJSON());
        return json;
    }

    public Vector getAcceleration() {
        return this.acceleration;
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

    public boolean isMobile() {
        return this.hasVelocity() || this.hasAcceleration();
    }

    public boolean isStationary() {
        return !this.isMobile();
    }

    public boolean equals(MobileRemoteState state) {
        if (state == null) return false;
        return super.equals(state) && this.velocity.equals(state.velocity) &&
            this.acceleration.equals(state.acceleration);
    }

}
