package com.seat.sim.common.util;

public class Debugger {
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public static final Debugger logger = new Debugger();

    public void err(Object o) {
        this.err(o.toString());
    }

    public void err(String msg) {
        System.err.println(Debugger.ANSI_RED + "ERR: " + msg + Debugger.ANSI_RESET);
    }

    public void fatal(Object o) {
        this.fatal(o.toString(), 1);
    }

    public void fatal(String msg) {
        this.fatal(msg, 1);
    }

    public void fatal(String msg, int errorCode) {
        System.err.println(Debugger.ANSI_RED + "ERR: " + msg + Debugger.ANSI_RESET);
        System.exit(errorCode);
    }

    public void info(Object o) {
        this.info(o.toString());
    }

    public void info(String msg) {
        System.out.println(Debugger.ANSI_GREEN + "INFO: " + msg + Debugger.ANSI_RESET);
    }

    public void state(Object o) {
        this.state(o.toString());
    }

    public void state(String msg) {
        System.out.println(Debugger.ANSI_BLUE + "STATE: " + msg + Debugger.ANSI_RESET);
    }

    public void warn(Object o) {
        this.warn(o.toString());
    }

    public void warn(String msg) {
        System.out.println(Debugger.ANSI_YELLOW + "WARN: " + msg + Debugger.ANSI_RESET);
    }

}
