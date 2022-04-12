package com.seat.sim.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.seat.sim.common.core.CommonException;

public class ArgsParser {

    private HashMap<String, String> parsedArgs;

    public ArgsParser() {
        this.parsedArgs = new HashMap<>();
    }

    public ArgsParser(String[] args) {
        this();
        this.parse(args);
    }

    private void parse(String[] args) throws CommonException {
        for (int i = 0; i < args.length; i++) {
            if (args[i].length() >= 3 && args[i].charAt(0) == '-' && args[i].charAt(1) == '-') {
                this.parsedArgs.put(args[i], args[i].substring(2));
            } else if (args[i].length() >= 2 && args[i].charAt(0) == '-') {
                if (i+1 >= args.length) {
                    throw new CommonException(String.format("No input arg for parameter %s", args[i]));
                }
                this.parsedArgs.put(args[i], args[i+1]);
                i++;
            } else {
                throw new CommonException(String.format("Unrecognized arg %s", args[i]));
            }
        }
    }

    public Collection<String> getArgs() {
        return new HashSet<String>(this.parsedArgs.values());
    }

    public Integer getInt(String param) throws CommonException {
        if (!this.hasParam(param)) {
            throw new CommonException(String.format("No param matching %s", param));
        }
        try {
            return Integer.parseInt(this.parsedArgs.get(param));
        } catch (NumberFormatException e) {
            throw new CommonException(e.toString());
        }
    }

    public Double getDouble(String param) throws NumberFormatException {
        if (!this.hasParam(param)) {
            throw new CommonException(String.format("No param matching %s", param));
        }
        try {
            return Double.parseDouble(this.parsedArgs.get(param));
        } catch (NumberFormatException e) {
            throw new CommonException(e.toString());
        }
    }

    public Long getLong(String param) throws NumberFormatException {
        if (!this.hasParam(param)) {
            throw new CommonException(String.format("No param matching %s", param));
        }
        try {
            return Long.parseLong(this.parsedArgs.get(param));
        } catch (NumberFormatException e) {
            throw new CommonException(e.toString());
        }
    }

    public Collection<String> getParams() {
        return new HashSet<String>(this.parsedArgs.keySet());
    }

    public String getString(String param) {
        if (!this.hasParam(param)) {
            throw new CommonException(String.format("No param matching %s", param));
        }
        return this.parsedArgs.get(param);
    }

    public boolean hasParam(String param) {
        return this.parsedArgs.containsKey(param);
    }

}
