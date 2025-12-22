package io.github.dug22.ciphercracker.cipher;

public class VigenereCipher extends Cipher {


    @Override
    public String encrypt(String plaintext, String key) {
        plaintext = plaintext.toUpperCase();
        key = key.toUpperCase();
        StringBuilder ciphertext = new StringBuilder();
        int keyIndex = 0;
        int keyLength = key.length();
        for(char character : plaintext.toCharArray()){
            boolean isLetter = Character.isLetter(character);
            if(isLetter) {
                char targetCharacter = key.charAt(keyIndex % keyLength);
                int result = ((character - 'A') + (targetCharacter - 'A')) % 26;
                ciphertext.append((char) (result + 'A'));
                keyIndex++;
            }
        }

        return ciphertext.toString();
    }

    @Override
    public String decrypt(String ciphertext, String key) {
        ciphertext = ciphertext.toUpperCase();
        key = key.toUpperCase();
        StringBuilder plaintext = new StringBuilder();
        int keyIndex = 0;
        int keyLength = key.length();
        for(char character : ciphertext.toCharArray()){
            boolean isLetter = Character.isLetter(character);
            if(isLetter) {
                char targetCharacter = key.charAt(keyIndex % keyLength);
                int result = ((character - 'A') - (targetCharacter - 'A') + 26) % 26;
                plaintext.append((char) (result + 'A'));
                keyIndex++;
            }
        }

        return plaintext.toString();
    }
}
