package io.github.dug22.ciphercracker.command;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {

    private static final List<SubCommand> subCommands = new ArrayList<>();

    static {
        subCommands.add(new CrackCommand());
        subCommands.add(new DebugCommand());
        subCommands.add(new DecryptCommand());
        subCommands.add(new EncryptCommand());
    }

    public static List<SubCommand> getSubCommands() {
        return subCommands;
    }
}
