package com.seat.rescuesim.common.remote;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.intent.IntentRegistry;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.remote.intent.IntentionSet;

public class RemoteController extends JSONAble {

    private IntentionSet intentions;

    public RemoteController(String remoteID) {
        this.intentions = new IntentionSet(remoteID);
    }

    public RemoteController(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONOption option) throws JSONException {
        this.intentions = new IntentionSet(option);
    }

    protected void addIntention(Intention intent) {
        this.intentions.addIntention(intent);
    }

    public void activateSensor(String sensor) {
        this.addIntention(IntentRegistry.Activate(sensor));
    }

    public void activateAllSensors() {
        this.addIntention(IntentRegistry.Activate());
    }

    public void activateAllSensors(Collection<String> sensors) {
        this.addIntention(IntentRegistry.Activate(sensors));
    }

    public void deactivateSensor(String sensor) {
        this.addIntention(IntentRegistry.Deactivate(sensor));
    }

    public void deactivateAllSensors() {
        this.addIntention(IntentRegistry.Deactivate());
    }

    public void deactivateAllSensors(Collection<String> sensors) {
        this.addIntention(IntentRegistry.Deactivate(sensors));
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

    public JSONOption toJSON() throws JSONException {
        return this.intentions.toJSON();
    }

    public boolean equals(RemoteController controller) {
        if (controller == null) return false;
        return this.intentions.equals(controller.intentions);
    }

}
