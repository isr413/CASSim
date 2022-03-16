package com.seat.rescuesim.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.seat.rescuesim.client.core.CoreException;
import com.seat.rescuesim.client.sandbox.ACSOS;
import com.seat.rescuesim.client.util.ArgsParser;
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
            return client;
        }
        Debugger.logger.info(String.format("Connecting to host=%s on port=%d ...", DEFAULT_ADDRESS.toString(), port));
        Socket client = new Socket(DEFAULT_ADDRESS, port);
        return client;
    }

    public static void main(String[] args) {
        Socket socket = null;
        PrintWriter out = null;
        try {
            Debugger.logger.info("Starting client ...");
            ArgsParser parser = new ArgsParser(args);
            socket = getClientSocket(parser);
            out = new PrintWriter(socket.getOutputStream(), true);
            Debugger.logger.info("Loading application ...");
            Application app = getApplication(args);
            Debugger.logger.info(String.format("Sending scenario <%s> ...", app.getScenarioID()));
            out.println(app.getScenarioConfig().encode());
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
