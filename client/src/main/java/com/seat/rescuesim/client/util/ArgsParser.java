package com.seat.rescuesim.client.util;

import java.util.HashMap;

import com.seat.rescuesim.common.util.Debugger;

public class ArgsParser {

    private HashMap<String, String> parsedArgs;

    public ArgsParser(String[] args) {
        this.parsedArgs = new HashMap<>();
        this.parse(args);
    }

    private void parse(String[] args) {
        int i = 0;
        while (i < args.length) {
            if (args[i].length() >= 3 && args[i].charAt(0) == '-' && args[i].charAt(1) == '-') {
                String param = args[i].substring(2);
                this.parsedArgs.put(param, param);
                i++;
            } else if (args[i].length() >= 2 && args[i].charAt(0) == '-') {
                String param = args[i].substring(1);
                if (i+1 < args.length) {
                    this.parsedArgs.put(param, args[i+1]);
                } else {
                    Debugger.logger.fatal(String.format("No input arg for parameter %s", args[i]));
                }
                i += 2;
            } else {
                Debugger.logger.err(String.format("Unrecognized arg %s", args[i]));
                i++;
            }
        }
    }

    public Integer getInt(String param) throws NumberFormatException {
        if (!this.hasParam(param)) {
            Debugger.logger.err(String.format("No param matching %s", param));
            return null;
        }
        return Integer.parseInt(this.parsedArgs.get(param));
    }

    public Double getDouble(String param) throws NumberFormatException {
        if (!this.hasParam(param)) {
            Debugger.logger.err(String.format("No param matching %s", param));
            return null;
        }
        return Double.parseDouble(this.parsedArgs.get(param));
    }

    public Long getLong(String param) throws NumberFormatException {
        if (!this.hasParam(param)) {
            Debugger.logger.err(String.format("No param matching %s", param));
            return null;
        }
        return Long.parseLong(this.parsedArgs.get(param));
    }

    public String getString(String param) {
        if (!this.hasParam(param)) {
            Debugger.logger.err(String.format("No param matching %s", param));
            return null;
        }
        return this.parsedArgs.get(param);
    }

    public boolean hasParam(String param) {
        return this.parsedArgs.containsKey(param);
    }

}
