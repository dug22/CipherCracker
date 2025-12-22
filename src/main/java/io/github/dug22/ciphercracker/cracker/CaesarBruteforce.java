package io.github.dug22.ciphercracker.cracker;

import io.github.dug22.ciphercracker.cipher.CaesarCipher;

public class CaesarBruteforce {

    private final String ciphertext;

    public CaesarBruteforce(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public void performCaesarCipherBruteforce() {
        bruteforceCaesarCipher();
    }

    private void bruteforceCaesarCipher() {
        int maxShifts = 26;
        for (int shift = 1; shift < maxShifts; shift++) {
            String decryptedText = new CaesarCipher().decrypt(ciphertext, String.valueOf(shift));
            System.out.println("Shift " + shift + ": " + decryptedText);
        }
    }
}
