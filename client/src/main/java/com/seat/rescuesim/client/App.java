package com.seat.rescuesim.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import com.seat.rescuesim.client.client.AppClient;
import com.seat.rescuesim.client.client.ClientException;
import com.seat.rescuesim.client.gui.GUIFrame;
import com.seat.rescuesim.client.sandbox.ACSOS;
import com.seat.rescuesim.common.SnapStatus;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.util.ArgsParser;
import com.seat.rescuesim.common.util.Debugger;

public class App {

    public static final String HOST_ARG = "-h";
    public static final String PORT_ARG = "-p";


    private static final String ID_ARG = "-id";

    private static AppClient getApplication(String scenarioID, String[] args) throws ClientException {
        if (scenarioID.equals("ACSOS")) {
            return new AppClient(new ACSOS(args), args);
        }
        throw new ClientException(String.format("Unrecognized application ID <%s>", scenarioID));
    }

    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser(args);
        if (!parser.hasParam(App.ID_ARG)) {
            throw new ClientException("No application ID has been provided");
        }
        String scenarioID = parser.getString(App.ID_ARG);
        AppClient client = App.getApplication(scenarioID, args);
        client.run();
    }

}
