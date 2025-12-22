package io.github.dug22.ciphercracker;

import io.github.dug22.ciphercracker.command.CommandRegistry;
import io.github.dug22.ciphercracker.command.SubCommand;

import java.util.List;
import java.util.Scanner;

public class CipherCrackerMain {

    private static final List<SubCommand> subCommands = CommandRegistry.getSubCommands();

    public static void main(String[] args) {
        System.out.println("Welcome To CipherCracker!\nCipher Crackers allows you to do the following");
        System.out.println(" ".repeat(5) + "•" + " Encrypt plaintext using the Caesar or Vigenère cipher");
        System.out.println(" ".repeat(5) + "•" + " Decrypt Caesar or Vigenère ciphertext");
        System.out.println(" ".repeat(5) + "•" + " Crack Caesar or Vigenère ciphertext");
        System.out.println("To get started, type the following command 'ciphercracker'!");
        System.out.println("If you wish to exit the program simply type 'quit' or 'exit'!");
        displayHelpMenu();
        Scanner scanner = new Scanner(System.in);
        while(true){

            String input = scanner.nextLine();
            boolean quitProgram = input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit");
            if(quitProgram){
                System.out.println("Exiting CipherCracker!");
                break;
            }

            if(input.isEmpty()) continue;

            String[] userArgs = input.split("\\s+");

            if (!userArgs[0].equalsIgnoreCase("ciphercracker")) {
                System.out.println("Command must begin with 'ciphercracker'");
                continue;
            }

            if(userArgs.length < 2){
                displayHelpMenu();
                continue;
            }

            String commandName = userArgs[1];



            boolean found = false;

            for(SubCommand command : subCommands){
                if(command.getName().equalsIgnoreCase(commandName)){
                    command.perform(userArgs);
                    found = true;
                    break;
                }
            }

            if(!found){
               displayHelpMenu();
            }
        }

        scanner.close();
    }

    public static void displayHelpMenu(){
        System.out.println(" ".repeat(100) + "CipherCracker Command List" + " ".repeat(100));
        System.out.println("-".repeat(200));
        for(SubCommand command : subCommands){
            System.out.println( "\t• " + command.getSyntax() + " - " + command.getDescription());
        }
        System.out.println("-".repeat(200));
    }
}
