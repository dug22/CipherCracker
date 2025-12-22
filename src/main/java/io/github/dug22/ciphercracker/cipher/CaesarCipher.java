package io.github.dug22.ciphercracker.cipher;

public class CaesarCipher extends Cipher{

    @Override
    public String encrypt(String plaintext, String key) {
        plaintext = plaintext.toUpperCase();
        StringBuilder ciphertext = new StringBuilder();
        int shift = Integer.parseInt(key);
        for(char character : plaintext.toCharArray()){
            boolean isLetter = Character.isLetter(character);
            if(isLetter){
                char targetChar = (char) ((character - 'A' + shift) % 26 + 'A');
                ciphertext.append(targetChar);
            }
        }

        return ciphertext.toString();
    }



    @Override
    public String decrypt(String ciphertext, String key) {
        StringBuilder plaintext = new StringBuilder();
        int shift = Integer.parseInt(key);
        for(char character : ciphertext.toCharArray()){
            boolean isLetter = Character.isLetter(character);
            if(isLetter){
                char targetChar = (char) ((character - 'A' - shift + 26) % 26 + 'A');
                plaintext.append(targetChar);
            }
        }

        return plaintext.toString();
    }
}
