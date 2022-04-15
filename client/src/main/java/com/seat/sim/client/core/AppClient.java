package com.seat.sim.client.core;

import java.util.Collection;

import com.seat.sim.common.core.Application;
import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.gui.GUIGridFrame;
import com.seat.sim.common.net.JSONSocket;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.Debugger;

public class AppClient {

    protected final static double DEFAULT_DELAY = 0.033;
    protected final static boolean DEFAULT_DISPLAY = false;

    private Application app;
    private long frameTime;
    private JSONSocket socket;

    public AppClient(Application app) throws CommonException {
        this.app = app;
        this.socket = JSONSocket.Client();
    }

    public AppClient(Application app, String hostname) throws CommonException {
        this.app = app;
        this.socket = JSONSocket.Client(hostname);
    }

    public AppClient(Application app, int port) throws CommonException {
        this.app = app;
        this.socket = JSONSocket.Client(port);
    }

    public AppClient(Application app, String hostname, int port) throws CommonException {
        this.app = app;
        this.socket = JSONSocket.Client(hostname, port);
    }

    public void run() throws ClientException, CommonException {
        this.run(AppClient.DEFAULT_DISPLAY, AppClient.DEFAULT_DELAY);
    }

    public void run(boolean visualDisplay) throws ClientException, CommonException {
        this.run(visualDisplay, AppClient.DEFAULT_DELAY);
    }

    public void run(double delay) throws ClientException, CommonException {
        this.run(AppClient.DEFAULT_DISPLAY, delay);
    }

    public void run(boolean visualDisplay, double delay) throws ClientException, CommonException {
        Debugger.logger.info("Running client ...");
        Debugger.logger.state(String.format("Loaded application <%s>", this.app.getScenarioID()));

        Debugger.logger.info(String.format("Sending scenario <%s> ...", app.getScenarioID()));
        this.socket.sendScenarioConfig(this.app.getScenarioConfig());
        Debugger.logger.state(String.format("Scenario <%s> sent", app.getScenarioID()));

        Debugger.logger.info(this.app.getScenarioConfig().toString());

        GUIGridFrame frame = null;
        if (visualDisplay) {
            Debugger.logger.info("Starting visual display ...");
            frame = new GUIGridFrame(app.getScenarioID(), this.app.getGrid());
            frame.setVisible(true);
            Debugger.logger.state("Frame set to visible");
            this.frameTime = System.currentTimeMillis();
        }

        while (true) {
            Debugger.logger.info("Waiting for snap ...");
            Snapshot snap = this.socket.getSnapshotBlocking();
            Debugger.logger.state(String.format("Received snap <%s> for time=%.2f", snap.getHash(),
                snap.getTime()));

            if (snap.hasError()) {
                Debugger.logger.err(String.format("Snap <%s> contains error(s)", snap.getHash()));
                throw new ClientException(snap.toString());
            }

            if (snap.isDone()) {
                break;
            }

            if (visualDisplay && frame != null) {
                Debugger.logger.info(String.format("Displaying snap <%s> ...", snap.getHash()));
                frame.displaySnap(snap);
            }

            Debugger.logger.info("Updating application ...");
            Collection<IntentionSet> intentions = this.app.update(snap);

            Debugger.logger.info("Sending intention(s) ...");
            this.socket.sendIntentions(intentions);
            Debugger.logger.state("Intention(s) sent");

            if (!visualDisplay || frame == null || delay == 0) continue;

            try {
                long prevFrameTime = this.frameTime;
                this.frameTime = System.currentTimeMillis();
                long waitTime = (long) (delay * 1000) - (frameTime - prevFrameTime);
                if (waitTime < 0) continue;
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Debugger.logger.err(e.toString());
            }
        }

        Debugger.logger.state(String.format("Scenario <%s> done", app.getScenarioID()));
        socket.close();
    }

}
