package io.github.dug22.ciphercracker.cracker;

import io.github.dug22.ciphercracker.cipher.VigenereCipher;
import io.github.dug22.ciphercracker.util.Debugger;
import io.github.dug22.ciphercracker.util.StringGroupingUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class KasiskiExamination {

    private String ciphertext;
    private final AtomicInteger atomicEstimatedKeyLength = new AtomicInteger(0);

    public KasiskiExamination(String ciphertext) {
        this.ciphertext = ciphertext.toUpperCase();
        cleanCiphertext();
    }

    private void cleanCiphertext() {
        ciphertext = ciphertext.replaceAll("[^A-Z]", "");
    }

    public void performKasiskiExamination() {
        getBestKeyLength();
        recoverKey();
    }

    private void getBestKeyLength() {
        Map<String, List<Integer>> trigramPositions = new HashMap<>();
        int trigramLength = 3;
        int ciphertextLength = ciphertext.length();
        for (int i = 0; i < ciphertextLength - trigramLength + 1; i++) {
            String trigram = ciphertext.substring(i, i + trigramLength);
            trigramPositions.putIfAbsent(trigram, new ArrayList<>());
            trigramPositions.get(trigram).add(i);
        }


        Debugger.print("Repeated Trigrams, Positions, Distances, and Common Factors:");

        HashMap<Integer, Integer> commonFactorsMap = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : trigramPositions.entrySet()) {
            List<Integer> positions = entry.getValue();

            if (positions.size() > 1) {
                String trigram = entry.getKey();
                List<Integer> distances = new ArrayList<>();

                for (int i = 1; i < positions.size(); i++) {
                    int distance = positions.get(i) - positions.get(i - 1);
                    distances.add(distance);
                }


                List<Integer> factorsFound = new ArrayList<>();
                for (int distance : distances) {
                    List<Integer> factors = findFactors(distance);

                    for (int factor : factors) {
                        if (factor <= 2) continue;
                        factorsFound.add(factor);
                        commonFactorsMap.put(factor, commonFactorsMap.getOrDefault(factor, 0) + 1);
                    }
                }


                Debugger.printf("* Trigram: '%s' | Positions: %s | Distances: %s | Factors: %s %n",
                        trigram, positions, distances, factorsFound);
            }
        }

        Map.Entry<Integer, Integer> maxEntry = commonFactorsMap.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElse(null);

        assert maxEntry != null;
        int estimatedKeyLength = maxEntry.getKey();
        System.out.println("Best Key Length: " + estimatedKeyLength);
        atomicEstimatedKeyLength.set(estimatedKeyLength);
    }


    private void recoverKey() {
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

    private List<Integer> findFactors(int n) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                factors.add(i);
            }
        }
        return factors;
    }
}
