package io.github.dug22.ciphercracker.cipher;

public abstract class Cipher {


    public abstract String encrypt(String plaintext, String key);

    public abstract String decrypt(String ciphertext, String key);
}
