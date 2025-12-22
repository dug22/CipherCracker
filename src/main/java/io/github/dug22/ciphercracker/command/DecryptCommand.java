package io.github.dug22.ciphercracker.command;

import io.github.dug22.ciphercracker.cipher.CaesarCipher;
import io.github.dug22.ciphercracker.cipher.VigenereCipher;

import java.nio.file.Files;
import java.nio.file.Path;

public class DecryptCommand extends SubCommand {

    @Override
    public String getName() {
        return "decrypt";
    }

    @Override
    public String getSyntax() {
        return "ciphercracker decrypt <vigenere/caesar> <file-path> <key/shift>";
    }

    @Override
    public String getDescription() {
        return "Reverses text encrypted using Vigenère or Caesar ciphers.";
    }

    @Override
    public void perform(String[] args) {
        if (args.length < 4) {
            System.out.println(getSyntax() + "-" + getDescription());
            return;
        }
        String encryptionMethod = args[2];
        String keyOrShift = args[args.length - 1];

        String filePath = args[3];

        try {

            String ciphertext = Files.readString(Path.of(filePath));

            if (ciphertext.isEmpty()) {
                System.out.println("[Error] File is empty.");
                return;
            }
            switch (encryptionMethod) {
                case "caesar" -> {
                    try {
                        if (Integer.parseInt(keyOrShift) < 1 || Integer.parseInt(keyOrShift) > 26) {
                            System.out.println("Shift must be between 1-26.");
                            return;
                        }
                        String plaintext = new CaesarCipher().decrypt(ciphertext, keyOrShift);
                        System.out.println("Plaintext with a shift of " + keyOrShift + ":\n" + plaintext);
                    } catch (Exception e) {
                        System.out.println("Shift must be an integer");
                    }
                }

                case "vigenere" -> {
                    for (int i = 0; i < keyOrShift.length(); i++) {
                        char character = keyOrShift.charAt(i);
                        if (!Character.isLetter(character)) {
                            System.out.println("Your key can only contain letters!");
                            return;
                        }
                    }
                    String plaintext = new VigenereCipher().decrypt(ciphertext, keyOrShift);
                    System.out.println("Plaintext with key " + keyOrShift + ":\n" + plaintext);
                }

                default -> {
                    System.out.println("Unknown decryption method: " + encryptionMethod);
                    System.out.println("Command Syntax: " + getSyntax() + "-" + getDescription());
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Could not read file: " + filePath);
            System.out.println("Message: " + e.getMessage());
        }
    }
}

