package com.seat.rescuesim.common.util;

import java.util.HashMap;
import java.util.HashSet;

public class ArgsParser {

    private HashMap<String, String> parsedArgs;

    public ArgsParser(String[] args) {
        this.parsedArgs = new HashMap<>();
        this.parse(args);
    }

    private void parse(String[] args) throws CoreException {
        for (int i = 0; i < args.length; i++) {
            if (args[i].length() >= 3 && args[i].charAt(0) == '-' && args[i].charAt(1) == '-') {
                this.parsedArgs.put(args[i], args[i].substring(2));
            } else if (args[i].length() >= 2 && args[i].charAt(0) == '-') {
                if (i+1 >= args.length) {
                    throw new CoreException(String.format("No input arg for parameter %s", args[i]));
                }
                this.parsedArgs.put(args[i], args[i+1]);
                i++;
            } else {
                throw new CoreException(String.format("Unrecognized arg %s", args[i]));
            }
        }
    }

    public HashSet<String> getArgs() {
        return new HashSet<String>(this.parsedArgs.values());
    }

    public Integer getInt(String param) throws CoreException {
        if (!this.hasParam(param)) {
            throw new CoreException(String.format("No param matching %s", param));
        }
        try {
            return Integer.parseInt(this.parsedArgs.get(param));
        } catch (NumberFormatException e) {
            throw new CoreException(e.toString());
        }
    }

    public Double getDouble(String param) throws NumberFormatException {
        if (!this.hasParam(param)) {
            throw new CoreException(String.format("No param matching %s", param));
        }
        try {
            return Double.parseDouble(this.parsedArgs.get(param));
        } catch (NumberFormatException e) {
            throw new CoreException(e.toString());
        }
    }

    public Long getLong(String param) throws NumberFormatException {
        if (!this.hasParam(param)) {
            throw new CoreException(String.format("No param matching %s", param));
        }
        try {
            return Long.parseLong(this.parsedArgs.get(param));
        } catch (NumberFormatException e) {
            throw new CoreException(e.toString());
        }
    }

    public HashSet<String> getParams() {
        return new HashSet<String>(this.parsedArgs.keySet());
    }

    public String getString(String param) {
        if (!this.hasParam(param)) {
            throw new CoreException(String.format("No param matching %s", param));
        }
        return this.parsedArgs.get(param);
    }

    public boolean hasParam(String param) {
        return this.parsedArgs.containsKey(param);
    }

}
