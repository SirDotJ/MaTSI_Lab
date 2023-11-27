package org.example.lab6;

import org.example.common.Alphabet;
import org.example.common.AlphabetConstants;
import org.example.common.PrimeMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ElGamal {
    /* Из примера */
    private static final Alphabet DEFAULT_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
    private final static int DEFAULT_USER_COUNT = 5;
    private final static int DEFAULT_NETWORK_KEY_1 = 8147;
    private final static int DEFAULT_NETWORK_KEY_2 = 3853;

    private Alphabet alphabet;
    private int networkKey1;
    private int networkKey2;
    private List<User> userList;

    public ElGamal(Alphabet alphabet, int prime1, int prime2, int userCount) throws IllegalArgumentException {
        this.alphabet = alphabet;
        if (!publicKeysValid(prime1, prime2))
            throw new IllegalArgumentException("Provided public keys are not valid for ElGamal crypto-system");
        if (userCount <= 0)
            throw new IllegalArgumentException("User count must be greater than 0");

        this.networkKey1 = prime1;
        this.networkKey2 = prime2;

        this.userList = new ArrayList<>();
        for (int i = 0; i < userCount; i++)
            userList.add(new User(this));
    }

    public ElGamal(Alphabet alphabet, int prime1, int prime2) throws IllegalArgumentException {
        this(alphabet, prime1, prime2, DEFAULT_USER_COUNT);
    }

    public ElGamal(int prime1, int prime2) throws IllegalArgumentException {
        this(DEFAULT_ALPHABET, prime1, prime2, DEFAULT_USER_COUNT);
    }

    public ElGamal() {
        this(DEFAULT_ALPHABET, DEFAULT_NETWORK_KEY_1, DEFAULT_NETWORK_KEY_2, DEFAULT_USER_COUNT);
    }

    public void setNewAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }
    public void setNewKey(int prime1, int prime2, int userCount) throws IllegalArgumentException {
        if (!publicKeysValid(prime1, prime2))
            throw new IllegalArgumentException("Provided public keys are not valid for ElGamal crypto-system");
        if (userCount <= 0)
            throw new IllegalArgumentException("User count must be greater than 0");

        this.networkKey1 = prime1;
        this.networkKey2 = prime2;

        this.userList = new ArrayList<>();
        for (int i = 0; i < userCount; i++)
            userList.add(new User(this));
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

    public List<User> getAllUsers() {
        return this.userList;
    }

    public User getUser(int index) throws IllegalArgumentException {
        if ((index < 0) || (index >= this.userList.size()))
            throw new IllegalArgumentException("Provided user index is out of range");
        return this.userList.get(index);
    }

    public int getUserIndex(User user) {
        for (int i = 0; i < this.userList.size(); i++) {
            User foundUser = this.userList.get(i);
            if (foundUser == user)
                return i;
        }

        return -1;
    }
}
