package com.seat.rescuesim.client;

import com.seat.rescuesim.client.core.AppClient;
import com.seat.rescuesim.client.core.ClientException;
import com.seat.rescuesim.client.sandbox.ACSOS;
import com.seat.rescuesim.common.core.Application;
import com.seat.rescuesim.common.util.ArgsParser;

public class App {
    private static final String HOST_ARG = "-h";
    private static final String ID_ARG = "-id";
    private static final String PORT_ARG = "-p";

    private static Application getApplication(String scenarioID, String[] args) throws ClientException {
        if (scenarioID.equals("ACSOS")) {
            return new ACSOS(args);
        }
        throw new ClientException(String.format("Unrecognized application ID <%s>", scenarioID));
    }

    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser(args);
        if (!parser.hasParam(App.ID_ARG)) {
            throw new ClientException("No application ID has been provided");
        }
        String scenarioID = parser.getString(App.ID_ARG);
        Application app = App.getApplication(scenarioID, args);
        AppClient client;
        if (parser.hasParam(App.HOST_ARG) && parser.hasParam(App.PORT_ARG)) {
            client = new AppClient(app, parser.getString(App.HOST_ARG), parser.getInt(App.PORT_ARG));
        } else if (parser.hasParam(App.HOST_ARG)) {
            client = new AppClient(app, parser.getString(App.HOST_ARG));
        } else if (parser.hasParam(App.PORT_ARG)) {
            client = new AppClient(app, parser.getInt(App.PORT_ARG));
        } else {
            client = new AppClient(app);
        }
        client.run();
    }

}
