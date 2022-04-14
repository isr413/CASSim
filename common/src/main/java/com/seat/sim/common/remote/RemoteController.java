package com.seat.sim.common.remote;

import java.util.Collection;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.Intention;
import com.seat.sim.common.remote.intent.IntentionSet;

/** A controller to signal intents to a Remote. */
public class RemoteController extends JSONAble {

    private IntentionSet intentions;

    public RemoteController(String remoteID) {
        this.intentions = new IntentionSet(remoteID);
    }

    public RemoteController(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONOptional optional) throws JSONException {
        this.intentions = new IntentionSet(optional);
    }

    protected void addIntention(Intention intent) {
        this.intentions.addIntention(intent);
    }

    public void activateAllSensors() {
        this.addIntention(IntentRegistry.Activate());
    }

    public void activateAllSensors(Collection<String> sensorIDs) {
        this.addIntention(IntentRegistry.Activate(sensorIDs));
    }

    public void activateSensor(String sensorID) {
        this.addIntention(IntentRegistry.Activate(sensorID));
    }

    public void deactivateAllSensors() {
        this.addIntention(IntentRegistry.Deactivate());
    }

    public void deactivateAllSensors(Collection<String> sensorIDs) {
        this.addIntention(IntentRegistry.Deactivate(sensorIDs));
    }

    public void deactivateSensor(String sensorID) {
        this.addIntention(IntentRegistry.Deactivate(sensorID));
    }

    public void done() {
        this.addIntention(IntentRegistry.Done());
    }

    public IntentionSet getIntentions() {
        return this.intentions;
    }

    public void none() {
        this.addIntention(IntentRegistry.None());
    }

    public void shutdown() {
        this.addIntention(IntentRegistry.Shutdown());
    }

    public void startup() {
        this.addIntention(IntentRegistry.Startup());
    }

    public JSONOptional toJSON() throws JSONException {
        return this.intentions.toJSON();
    }

    public boolean equals(RemoteController controller) {
        if (controller == null) return false;
        return this.intentions.equals(controller.intentions);
    }

}
