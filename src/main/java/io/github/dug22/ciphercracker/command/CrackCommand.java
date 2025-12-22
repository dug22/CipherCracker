package io.github.dug22.ciphercracker.command;

import io.github.dug22.ciphercracker.cracker.CaesarBruteforce;
import io.github.dug22.ciphercracker.cracker.FriedmanTest;
import io.github.dug22.ciphercracker.cracker.KasiskiExamination;

import java.nio.file.Files;
import java.nio.file.Path;

public class CrackCommand extends SubCommand {

    @Override
    public String getName() {
        return "crack";
    }

    @Override
    public String getSyntax() {
        return "ciphercracker crack <file-path> <kasiski/friedman/caesarbf>";
    }

    @Override
    public String getDescription() {
        return "Cracks Caesar or Vigenère ciphertext loaded from a file.";
    }

    @Override
    public void perform(String[] args) {

        if (args.length != 4) {
            System.out.println(getSyntax() + " - " + getDescription());
            return;
        }

        String filePath = args[2];
        String attackVector = args[3];

        try {

            String ciphertext = Files.readString(Path.of(filePath));

            if (ciphertext.isEmpty()) {
                System.out.println("[Error] File is empty.");
                return;
            }

            switch (attackVector.toLowerCase()) {
                case "kasiski" ->
                        new KasiskiExamination(ciphertext).performKasiskiExamination();

                case "friedman" ->
                        new FriedmanTest(ciphertext).performFriedmanTest();

                case "caesarbf" ->
                        new CaesarBruteforce(ciphertext).performCaesarCipherBruteforce();

                default -> {
                    System.out.println("Unknown attack vector: " + attackVector);
                    System.out.println(getSyntax());
                }
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Could not read file: " + filePath);
            System.out.println("Message: " + e.getMessage());
        }
    }
}
