package com.seat.rescuesim.simserver;

import com.seat.rescuesim.common.util.ArgsParser;
import com.seat.rescuesim.simserver.core.AppServer;

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
