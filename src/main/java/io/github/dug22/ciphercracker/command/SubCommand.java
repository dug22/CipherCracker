package io.github.dug22.ciphercracker.command;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getSyntax();

    public abstract String getDescription();

    public abstract void perform(String[] args);
}
