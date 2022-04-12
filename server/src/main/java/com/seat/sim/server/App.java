package com.seat.sim.server;

import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.server.core.AppServer;

public class App {
    private static final String PORT_ARG = "-p";

    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser(args);
        AppServer server;
        if (parser.hasParam(App.PORT_ARG)) {
            server = new AppServer(parser.getInt(App.PORT_ARG));
        } else {
            server = new AppServer();
        }
        server.run();
    }

}
