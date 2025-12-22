package io.github.dug22.ciphercracker.command;

import io.github.dug22.ciphercracker.cipher.CaesarCipher;
import io.github.dug22.ciphercracker.cipher.VigenereCipher;

import java.nio.file.Files;
import java.nio.file.Path;

public class EncryptCommand extends SubCommand {

    @Override
    public String getName() {
        return "encrypt";
    }

    @Override
    public String getSyntax() {
        return "ciphercracker encrypt <vigenere/caesar> <file-path> <key/shift>";
    }

    @Override
    public String getDescription() {
        return "Applies Caesar or Vigenère encryption to a message.";
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
            String plaintext = Files.readString(Path.of(filePath));

            if (plaintext.isEmpty()) {
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
                    String ciphertext = new CaesarCipher().encrypt(plaintext, keyOrShift);
                    System.out.println("Ciphertext with a shift of " + keyOrShift + ":\n" + ciphertext);
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
                String ciphertext = new VigenereCipher().encrypt(plaintext, keyOrShift);
                System.out.println("Ciphertext with key " + keyOrShift + ":\n" + ciphertext);
            }

            default -> {
                System.out.println("Unknown encryption method: " + encryptionMethod);
                System.out.println("Command Syntax: " + getSyntax() + "-" + getDescription());
            }
        }
        } catch (Exception e) {
            System.out.println("[ERROR] Could not read file: " + filePath);
            System.out.println("Message: " + e.getMessage());
        }
    }
}
