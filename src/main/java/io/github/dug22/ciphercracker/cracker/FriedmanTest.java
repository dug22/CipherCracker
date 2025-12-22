package io.github.dug22.ciphercracker.cracker;

import io.github.dug22.ciphercracker.cipher.CaesarCipher;
import io.github.dug22.ciphercracker.cipher.VigenereCipher;
import io.github.dug22.ciphercracker.util.Debugger;
import io.github.dug22.ciphercracker.util.StringGroupingUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FriedmanTest {

    private String ciphertext;
    private final AtomicReference<Double> atomicIndexOfCoincidence = new AtomicReference<>();
    private final AtomicInteger atomicEstimatedKeyLength = new AtomicInteger(0);

    public FriedmanTest(String ciphertext) {
        this.ciphertext = ciphertext.toUpperCase();
        cleanCiphertext();
    }

    private void cleanCiphertext() {
        ciphertext = ciphertext.replaceAll("[^A-Z]", "");
    }

    public void performFriedmanTest() {
        findIndexOfCoincidence();
        estimateKeyLength();
        recoverKey();
    }

    private void findIndexOfCoincidence() {
        int ciphertextLength = ciphertext.length();
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (char character : ciphertext.toCharArray()) {
            if (frequencyMap.get(character) != null) {
                frequencyMap.put(character, frequencyMap.get(character) + 1);
            } else {
                frequencyMap.put(character, 1);
            }
        }

        long numerator = 0;
        for (int frequency : frequencyMap.values()) {
            numerator += (long) frequency * (frequency - 1);
        }

        double denominator = (double) ciphertextLength * (ciphertextLength - 1);
        atomicIndexOfCoincidence.set(numerator / denominator);

        if (atomicIndexOfCoincidence.get() >= 0.065) {
            Debugger.print("Ciphertext is likely monoalphabetic substitution cipher: IOC=" + atomicIndexOfCoincidence.get());
        } else {
            Debugger.print("Ciphertext is likely polyalphabetic substitution: IOC=" + atomicIndexOfCoincidence.get());
        }
    }

    private void estimateKeyLength() {
        double indexOfCoincidence = atomicIndexOfCoincidence.get();
        int ciphertextLength = ciphertext.length();
        double estimatedKeyLength = (0.027 * ciphertextLength) / ((ciphertextLength - 1) * indexOfCoincidence - 0.038 * ciphertextLength + 0.065);
        atomicEstimatedKeyLength.set((int) Math.round(estimatedKeyLength));
        System.out.println("Estimated Key Length: " + atomicEstimatedKeyLength.get());
    }

    private void recoverKey() {
        if (atomicIndexOfCoincidence.get() >= 0.065) {
            System.out.println("Since it's a monoalphabetic substitution cipher we'll brute force this ciphertext with 26 possible shifts.");
            new CaesarBruteforce(ciphertext).performCaesarCipherBruteforce();
        } else {
            List<String> groupedColumns = StringGroupingUtil.group(ciphertext, atomicEstimatedKeyLength.get());
            for (int i = 0; i < groupedColumns.size(); i++) {
                String groupedLetters = groupedColumns.get(i);
                Debugger.print("Column " + i + ": " + groupedLetters);
            }

            StringBuilder key = new StringBuilder();
            for (String column : groupedColumns) {
                HashMap<Character, Integer> groupFrequencyMap = new HashMap<>();
                for (char character : column.toCharArray()) {
                    groupFrequencyMap.merge(character, 1, Integer::sum);
                }

                char mostFrequentCharacter = groupFrequencyMap.entrySet()
                        .stream().max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey).get();
                int letterEIndex = 4;
                int originalPosition = mostFrequentCharacter - 'A';
                int newPosition = (originalPosition - letterEIndex + 26) % 26;
                char keyCharacterCandidate = (char) ('A' + newPosition);
                key.append(keyCharacterCandidate);
            }

            System.out.println("\nRecovered Key: " + key);
            System.out.println("Decrypted Message: " + new VigenereCipher().decrypt(ciphertext, key.toString()));
        }
    }
}