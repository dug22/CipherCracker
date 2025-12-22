package io.github.dug22.ciphercracker.command;

import io.github.dug22.ciphercracker.util.Debugger;

public class DebugCommand extends SubCommand{

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getSyntax() {
        return "ciphercracker debug";
    }

    @Override
    public String getDescription() {
        return "Turns the program's debugger on or off.";
    }

    @Override
    public void perform(String[] args) {
        if(!Debugger.isToggledOn()){
            Debugger.enable();
            System.out.println("You have enabled CipherCracker's debugger!");
        }else{
            Debugger.disable();
            System.out.println("You have disabled CipherCracker's debugger!");
        }
    }
}
