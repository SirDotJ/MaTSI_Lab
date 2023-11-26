package org.example.lab6;

import org.example.common.Alphabet;
import org.example.common.AlphabetConstants;
import org.example.common.PrimeMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ElGamal {
    private final static int DEFAULT_USER_COUNT = 5;
    private final static int DEFAULT_NETWORK_KEY_1 = 8147;
    private final static int DEFAULT_NETWORK_KEY_2 = 3853;

    private final Alphabet alphabet = AlphabetConstants.CYRILLIC_WITH_SPACE;
    private final int networkKey1;
    private final int networkKey2;
    private final List<User> userList;

    public static void main(String[] args) {
        ElGamal network = new ElGamal();

        String message = "здарова_бандиты";
        int senderIndex = 0;
        List<String> encryptedMessages = network.massSend(senderIndex, message);
        List<String> decryptedMessages = network.massReceive(senderIndex, encryptedMessages);

        System.out.println("Message: " + message);
        System.out.println("Sender: User №" + senderIndex);

        System.out.println("Received encrypted messages: ");
        int counter = 0;
        for (int i = 0; i < encryptedMessages.size() + 1; i++) {
            if (i == senderIndex)
                continue;
            System.out.println("\tUser №" + i + ": " + encryptedMessages.get(counter++));
        }

        counter = 0;
        System.out.println("Decrypted messages: ");
        for (int i = 0; i < decryptedMessages.size() + 1; i++) {
            if (i == senderIndex)
                continue;
            System.out.println("\tUser №" + i + ": " + decryptedMessages.get(counter++));
        }
    }

    public ElGamal(int prime1, int prime2, int userCount) throws IllegalArgumentException {
        if (!publicKeysValid(prime1, prime2))
            throw new IllegalArgumentException("Provided public keys are not valid for ElGamal crypto-system");
        this.networkKey1 = prime1;
        this.networkKey2 = prime2;

        this.userList = new ArrayList<>();
        for (int i = 0; i < userCount; i++)
            userList.add(new User(this));
    }

    public ElGamal(int prime1, int prime2) throws IllegalArgumentException {
        this(prime1, prime2, DEFAULT_USER_COUNT);
    }

    public ElGamal() {
        this(DEFAULT_NETWORK_KEY_1, DEFAULT_NETWORK_KEY_2, DEFAULT_USER_COUNT);
    }

    private static boolean publicKeysValid(int key1Candidate, int key2Candidate) {
        return (PrimeMath.primesToEachOther(key1Candidate, key2Candidate)
                && moduleTypeCheck(key1Candidate, key2Candidate));
    }

    private static boolean moduleTypeCheck(int key1Candidate, int key2Candidate) {
        BigInteger bigKey1 = BigInteger.valueOf(key1Candidate);
        BigInteger bigKey2 = BigInteger.valueOf(key2Candidate);

        return (bigKey2.pow(key1Candidate).mod(bigKey1).equals(bigKey2));
    }

    int getNetworkKey1() {
        return this.networkKey1;
    }

    int getNetworkKey2() {
        return this.networkKey2;
    }

    Alphabet getAlphabet() {
        return alphabet;
    }

    public List<String> massSend(int senderIndex, String message) throws IllegalArgumentException {
        if (senderIndex < 0 || senderIndex >= this.userList.size())
            throw new IllegalArgumentException("Provided index out of range");

        User sender = this.userList.get(senderIndex);

        List<String> encryptedMessages = new ArrayList<>();
        for (int i = 0; i < this.userList.size(); i++) {
            if (i == senderIndex)
                continue;
            User receiver = this.userList.get(i);
            encryptedMessages.add(sender.sendMessage(receiver, message));
        }

        return encryptedMessages;
    }

    public List<String> massReceive(int senderIndex, List<String> receivedMessages) throws IllegalArgumentException {
        if (senderIndex < 0 || senderIndex >= this.userList.size())
            throw new IllegalArgumentException("Provided index out of range");
        else if (receivedMessages.size() != (this.userList.size() - 1))
            throw new IllegalArgumentException("Invalid count of received messages");

        User sender = this.userList.get(senderIndex);

        List<String> decryptedMessages = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < this.userList.size(); i++) {
            if (i == senderIndex)
                continue;
            User receiver = this.userList.get(i);
            decryptedMessages.add(receiver.receiveMessage(sender, receivedMessages.get(counter++)));
        }

        return decryptedMessages;
    }
}
