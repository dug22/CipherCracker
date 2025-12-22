package io.github.dug22.ciphercracker.util;

public class Debugger {

    private static boolean isEnabled = false;

    public static void print(String debugMessage) {
        if (isEnabled) {
            System.out.println(debugMessage);
        }
    }

    public static void printf(String debugMessage, Object... args) {
        if (isEnabled) {
            System.out.printf(debugMessage, args);
        }
    }

    public static void enable() {
        isEnabled = true;
    }

    public static void disable() {
        isEnabled = false;
    }

    public static boolean isToggledOn(){
        return isEnabled;
    }
}
