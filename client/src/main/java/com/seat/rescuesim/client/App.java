package com.seat.rescuesim.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import com.seat.rescuesim.client.core.Application;
import com.seat.rescuesim.client.gui.GUIFrame;
import com.seat.rescuesim.client.sandbox.ACSOS;
import com.seat.rescuesim.client.util.ClientException;
import com.seat.rescuesim.common.SnapStatus;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.util.ArgsParser;
import com.seat.rescuesim.common.util.Debugger;

public class App {

    private static final InetAddress DEFAULT_ADDRESS = InetAddress.getLoopbackAddress();
    private static final int DEFAULT_PORT = 8080;
    private static final String HOST_ARG = "-h";
    private static final String ID_ARG = "-id";
    private static final String PORT_ARG = "-p";

    private static Application getApplication(ArgsParser parser, String[] args) throws ClientException {
        System.out.println(parser.getParams().toString());
        if (!parser.hasParam(App.ID_ARG)) {
            throw new ClientException("No application ID has been specified");
        }
        if (parser.getString(App.ID_ARG).equals("ACSOS")) {
            return new ACSOS(args);
        } else {
            throw new ClientException("Unrecognized application ID");
        }
    }

    private static Socket getClientSocket(ArgsParser parser) throws IOException {
        int port = App.DEFAULT_PORT;
        if (parser.hasParam(App.PORT_ARG)) {
            port = parser.getInt(App.PORT_ARG);
        }
        if (parser.hasParam(App.HOST_ARG)) {
            String host = parser.getString(App.HOST_ARG);
            Debugger.logger.info(String.format("Connecting to host=%s on port=%d ...", host, port));
            Socket client = new Socket(host, port);
            Debugger.logger.state(String.format("Connected to host=%s on port=%d", host, port));
            return client;
        }
        Debugger.logger.info(String.format("Connecting to host=%s on port=%d ...", App.DEFAULT_ADDRESS.toString(),
            port));
        Socket client = new Socket(App.DEFAULT_ADDRESS, port);
        Debugger.logger.state(String.format("Connected to host=%s on port=%d", App.DEFAULT_ADDRESS.toString(), port));
        return client;
    }

    private static void runApplicationSync(Application app, BufferedReader in,
            PrintWriter out) throws ClientException, InterruptedException, IOException {
        GUIFrame frame = new GUIFrame(app.getScenarioID());
        frame.setVisible(true);
        Debugger.logger.info(String.format("Sending scenario <%s> ...", app.getScenarioID()));
        out.println(app.getScenarioConfig().encode());
        boolean done = false;
        while (!done) {
            if (in.ready()) {
                String snapEncoding = in.readLine();
                if (!JSONOption.isJSON(snapEncoding)) {
                    throw new ClientException(snapEncoding);
                }
                Snapshot snap = new Snapshot(snapEncoding);
                Debugger.logger.state(String.format("Received snap <%s> for time=%.2f", snap.getHash(),
                    snap.getTime()));
                if (snap.getStatus().equals(SnapStatus.ERROR)) {
                    throw new ClientException(snapEncoding);
                }
                if (snap.getStatus().equals(SnapStatus.DONE)) {
                    done = true;
                    continue;
                }
                Debugger.logger.info("Displaying snap ...");
                frame.displaySnap(snap, app.getScenarioConfig().getMap().getWidth(),
                    app.getScenarioConfig().getMap().getHeight());
                System.out.println(snap);
                Debugger.logger.info("Getting next action(s) ...");
                ArrayList<RemoteController> controllers = app.update(snap);
                JSONArrayBuilder json = JSONBuilder.Array();
                for (RemoteController controller : controllers) {
                    json.put(controller.toJSON());
                }
                out.println(json.toJSON().toString());
                Debugger.logger.state("Action(s) sent");
            }
        }
    }

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            Debugger.logger.info("Starting client ...");
            ArgsParser parser = new ArgsParser(args);
            Application app = App.getApplication(parser, args);
            Debugger.logger.state(String.format("Loaded application <%s>", app.getScenarioID()));
            socket = App.getClientSocket(parser);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            App.runApplicationSync(app, in, out);
            Debugger.logger.state(String.format("Scenario <%s> is done", app.getScenarioID()));
            socket.close();
        } catch (Exception e) {
            if (out != null) {
                out.println(e.toString());
            }
            e.printStackTrace();
        }
    }

}
