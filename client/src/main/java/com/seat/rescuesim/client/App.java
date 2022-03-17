package com.seat.rescuesim.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.seat.rescuesim.client.core.Application;
import com.seat.rescuesim.client.core.CoreException;
import com.seat.rescuesim.client.sandbox.ACSOS;
import com.seat.rescuesim.client.util.ArgsParser;
import com.seat.rescuesim.common.SnapStatus;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.Debugger;

public class App {

    private static final InetAddress DEFAULT_ADDRESS = InetAddress.getLoopbackAddress();
    private static final int DEFAULT_PORT = 8080;
    private static final String HOST_ARG = "-h";
    private static final String PORT_ARG = "-p";

    private static Application getApplication(String[] args) {
        return new ACSOS(args);
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
        Debugger.logger.info(String.format("Sending scenario <%s> ...", app.getScenarioID()));
        out.println(app.getScenarioConfig().encode());
        boolean running = true;
        do {
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
                    running = false;
                }
            }
        } while (running);
    }

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            Debugger.logger.info("Starting client ...");
            ArgsParser parser = new ArgsParser(args);
            socket = getClientSocket(parser);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Application app = getApplication(args);
            Debugger.logger.state(String.format("Loaded application <%s>", app.getScenarioID()));
            runApplicationSync(app, in, out);
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
            if (out != null) {
                out.println(e.toString());
            }
        } catch (CoreException e) {
            System.err.println(e);
            if (out != null) {
                out.println(e.toString());
            }
        }
    }

}
