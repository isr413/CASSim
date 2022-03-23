package com.seat.rescuesim.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import com.seat.rescuesim.client.core.Application;
import com.seat.rescuesim.client.core.CoreException;
import com.seat.rescuesim.client.gui.GUIFrame;
import com.seat.rescuesim.client.sandbox.ACSOS;
import com.seat.rescuesim.client.util.ArgsParser;
import com.seat.rescuesim.common.SnapStatus;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.util.Debugger;

public class App {

    private static final InetAddress DEFAULT_ADDRESS = InetAddress.getLoopbackAddress();
    private static final int DEFAULT_PORT = 8080;
    private static final String HOST_ARG = "-h";
    private static final String ID_ARG = "-id";
    private static final String PORT_ARG = "-p";

    private static Application getApplication(ArgsParser parser, String[] args) throws CoreException {
        if (!parser.hasParam(ID_ARG)) {
            throw new CoreException("No application ID has been specified");
        }
        if (parser.getString(ID_ARG).equals("ACSOS")) {
            return new ACSOS(args);
        } else {
            throw new CoreException("Unrecognized application ID");
        }
    }

    private static Socket getClientSocket(ArgsParser args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.hasParam(PORT_ARG)) {
            port = args.getInt(PORT_ARG);
        }
        if (args.hasParam(HOST_ARG)) {
            String host = args.getString(HOST_ARG);
            Debugger.logger.info(String.format("Connecting to host=%s on port=%d ...", host, port));
            Socket client = new Socket(host, port);
            Debugger.logger.state(String.format("Connected to host=%s on port=%d", host, port));
            return client;
        }
        Debugger.logger.info(String.format("Connecting to host=%s on port=%d ...", DEFAULT_ADDRESS.toString(), port));
        Socket client = new Socket(DEFAULT_ADDRESS, port);
        Debugger.logger.state(String.format("Connected to host=%s on port=%d", DEFAULT_ADDRESS.toString(), port));
        return client;
    }

    private static void runApplicationSync(Application app, BufferedReader in,
            PrintWriter out) throws CoreException, IOException {
        GUIFrame frame = new GUIFrame(app.getScenarioID());
        Debugger.logger.info(String.format("Sending scenario <%s> ...", app.getScenarioID()));
        out.println(app.getScenarioConfig().encode());
        boolean done = false;
        while (!done) {
            if (in.ready()) {
                String snapEncoding = in.readLine();
                if (!JSONOption.isJSON(snapEncoding)) {
                    throw new CoreException(snapEncoding);
                }
                Snapshot snap = new Snapshot(snapEncoding);
                Debugger.logger.state(String.format("Received snap <%s> for time=%.2f", snap.getHash(), snap.getTime()));
                if (snap.getStatus().equals(SnapStatus.ERROR)) {
                    throw new CoreException(snapEncoding);
                }
                if (snap.getStatus().equals(SnapStatus.DONE)) {
                    done = true;
                    continue;
                }
                Debugger.logger.info("Displaying snap ...");
                frame.displaySnap(snap);
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
            Application app = getApplication(parser, args);
            Debugger.logger.state(String.format("Loaded application <%s>", app.getScenarioID()));
            socket = getClientSocket(parser);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            runApplicationSync(app, in, out);
            socket.close();
        } catch (IOException e) {
            if (out != null) {
                out.println(e.toString());
            }
            e.printStackTrace();
        } catch (CoreException e) {
            if (out != null) {
                out.println(e.toString());
            }
            e.printStackTrace();
        }
    }

}
