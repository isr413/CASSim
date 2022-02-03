package com.seat.rescuesim.common;

public class Debugger {
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public static final Debugger logger = new Debugger();

    public void err(String msg) {
        System.err.println(Debugger.ANSI_RED + "ERR: " + msg + Debugger.ANSI_RESET);
    }

    public void fatal(String msg) {
        this.fatal(msg, 1);
    }

    public void fatal(String msg, int errorCode) {
        System.err.println(Debugger.ANSI_RED + "ERR: " + msg + Debugger.ANSI_RESET);
        System.exit(errorCode);
    }

    public void info(String msg) {
        System.out.println(Debugger.ANSI_GREEN + "ERR: " + msg + Debugger.ANSI_RESET);
    }

    public void state(String msg) {
        System.out.println(Debugger.ANSI_BLUE + "ERR: " + msg + Debugger.ANSI_RESET);
    }

    public void warn(String msg) {
        System.out.println(Debugger.ANSI_YELLOW + "ERR: " + msg + Debugger.ANSI_RESET);
    }

}
