package com.seat.rescuesim.client.client;

import java.util.Collection;

import com.seat.rescuesim.client.gui.GUIFrame;
import com.seat.rescuesim.common.app.Application;
import com.seat.rescuesim.common.net.JSONSocket;
import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.scenario.Snapshot;
import com.seat.rescuesim.common.util.CoreException;
import com.seat.rescuesim.common.util.Debugger;

public class AppClient {

    private Application app;
    private JSONSocket socket;

    public AppClient(Application app) throws CoreException {
        this.app = app;
        this.socket = JSONSocket.Client();
    }

    public AppClient(Application app, String hostname) throws CoreException {
        this.app = app;
        this.socket = JSONSocket.Client(hostname);
    }

    public AppClient(Application app, int port) throws CoreException {
        this.app = app;
        this.socket = JSONSocket.Client(port);
    }

    public AppClient(Application app, String hostname, int port) throws CoreException {
        this.app = app;
        this.socket = JSONSocket.Client(hostname, port);
    }

    public void run() throws CoreException {
        this.run(false);
    }

    public void run(boolean visualDisplay) throws CoreException {
        Debugger.logger.info("Running client ...");
        Debugger.logger.state(String.format("Loaded application <%s>", this.app.getScenarioID()));

        Debugger.logger.info(String.format("Sending scenario <%s> ...", app.getScenarioID()));
        this.socket.sendScenario(this.app.getScenarioConfig());
        Debugger.logger.state(String.format("Scenario <%s> sent", app.getScenarioID()));

        GUIFrame frame = null;
        if (visualDisplay) {
            Debugger.logger.info("Starting visual display ...");
            frame = new GUIFrame(app.getScenarioID());
            frame.setVisible(true);
            Debugger.logger.state("Frame set to visible");
        }

        while (true) {
            Snapshot snap = this.socket.getSnapshotBlocking();
            Debugger.logger.state(String.format("Received snap <%s> for time=%.2f", snap.getHash(), snap.getTime()));

            if (snap.hasError()) {
                Debugger.logger.err(String.format("Snap <%s> contains error(s)", snap.getHash()));
                throw new ClientException(snap.toString());
            }

            if (snap.isDone()) {
                break;
            }

            Debugger.logger.info(snap.toString());
            if (visualDisplay && frame != null) {
                Debugger.logger.info(String.format("Displaying snap <%s> ...", snap.getHash()));
                frame.displaySnap(snap, app.getMap().getWidth(), app.getMap().getHeight());
            }

            Debugger.logger.info("Updating application ...");
            Collection<IntentionSet> intentions = this.app.update(snap);

            Debugger.logger.info("Sending intention(s) ...");
            this.socket.sendIntentions(intentions);
            Debugger.logger.state("Intention(s) sent");
        }

        Debugger.logger.state(String.format("Scenario <%s> done", app.getScenarioID()));
        socket.close();
    }

}
