package com.example.projektakripto.algoritma.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSA {
    private final static SecureRandom random = new SecureRandom();
    private final static BigInteger one = new BigInteger("1");

    private BigInteger modulus;

    private BigInteger publicKey;
    private BigInteger privateKey;

    public RSA(int bitLength) {
        BigInteger p = BigInteger.probablePrime(bitLength / 2, new Random());
        BigInteger q = BigInteger.probablePrime(bitLength / 2, new Random());
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

        this.modulus = p.multiply(q);
        this.publicKey = new BigInteger("65537");
        this.privateKey = publicKey.modInverse(phi);
    }

    public RSA(BigInteger privateKey, BigInteger modulus){
        this.privateKey = privateKey;
        this.modulus = modulus;
    }

    public BigInteger prosesencrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    public BigInteger prosesdecrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

    public String encrypt(String message) {
        return this.prosesencrypt(new BigInteger(message)).toString();
    }

    public String decrypt(String message) {
        return this.prosesdecrypt(new BigInteger(message)).toString();
    }

    public String getModulus() {
        return this.modulus.toString();
    }

    public String getPrivatekey() {
        return this.privateKey.toString();
    }

    public String getPublicKey() {
        return this.publicKey.toString() + this.modulus.toString();
    }
}