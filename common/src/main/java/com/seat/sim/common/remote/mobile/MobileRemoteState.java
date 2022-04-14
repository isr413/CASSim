package com.seat.sim.common.remote.mobile;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.sensor.SensorState;

/** A serializable state of a Mobile Remote. */
public class MobileRemoteState extends RemoteState {
    public static final String ACCELERATION = "acceleration";
    public static final String VELOCITY = "velocity";

    private Vector acceleration;
    private Vector velocity;

    public MobileRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Vector velocity, Vector acceleration) {
        this(remoteID, team, location, battery, active, null, velocity, acceleration);
    }

    public MobileRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(remoteID, team, location, battery, active, sensors);
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public MobileRemoteState(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.velocity = (json.hasKey(MobileRemoteState.VELOCITY)) ?
            new Vector(json.getJSONOptional(MobileRemoteState.VELOCITY)) :
            new Vector();
        this.acceleration = (json.hasKey(MobileRemoteState.ACCELERATION)) ?
            new Vector(json.getJSONOptional(MobileRemoteState.ACCELERATION)) :
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

    @Override
    public String getLabel() {
        return String.format("%s:<mobile>", this.getRemoteID());
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

    @Override
    public boolean isMobile() {
        return this.hasVelocity() || this.hasAcceleration();
    }

    public boolean equals(MobileRemoteState state) {
        if (state == null) return false;
        return super.equals(state) && this.velocity.equals(state.velocity) &&
            this.acceleration.equals(state.acceleration);
    }

}
