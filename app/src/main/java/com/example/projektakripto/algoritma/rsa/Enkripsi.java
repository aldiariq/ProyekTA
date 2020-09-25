package com.example.projektakripto.algoritma.rsa;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class Enkripsi {

    private final int PanjangBit = 1024;
    private final Random rand = new Random();

    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger tn;
    private BigInteger e;
    private BigInteger d;

    private String plaintextkunciblowfish;
    private byte[] byteplaintextkunciblowfish;
    private byte[] bytehasilEnkripsikunciblowfish;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Enkripsi(String plaintextkunciblowfish) throws FileNotFoundException, IOException{
        this.setP(BigInteger.probablePrime(PanjangBit, rand));
        this.setQ(BigInteger.probablePrime(PanjangBit, rand));
        this.setN(p.multiply(q));
        this.setTn(p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));
        this.setE(this.generateE(this.getTn()));
        this.setD(this.generateD(this.getE(), this.getTn()));

        //Proses Pengambilan Plaintext Kunci Blowfish
        this.setPlaintextkunciblowfish(plaintextkunciblowfish);

        //Proses Pengambilan Nilai Byte dari Plaintext Kunci Blowfish
        this.setByteplaintextkunciblowfish(this.getPlaintextkunciblowfish().getBytes());

        //Proses Pembuatan File Terenkripsi
        this.setBytehasilEnkripsikunciblowfish(prosesEnkripsi(this.getE(), this.getN(), this.getByteplaintextkunciblowfish()));

    }

    //Get Nilai P
    public BigInteger getP() {
        return p;
    }

    //Set Nilai P
    public void setP(BigInteger p) {
        this.p = p;
    }

    //Get Nilai Q
    public BigInteger getQ() {
        return q;
    }

    //Set Nilai Q
    public void setQ(BigInteger q) {
        this.q = q;
    }

    //Get Nilai N
    public BigInteger getN() {
        return n;
    }

    //Set Nilai N
    public void setN(BigInteger n) {
        this.n = n;
    }

    //Get Nilai TN
    public BigInteger getTn() {
        return tn;
    }

    //Set Nilai TN
    public void setTn(BigInteger tn) {
        this.tn = tn;
    }

    //Get Nilai E
    public BigInteger getE() {
        return e;
    }

    //Set Nilai E
    public void setE(BigInteger e) {
        this.e = e;
    }

    //Get Nilai D
    public BigInteger getD() {
        return d;
    }

    //Set Nilai D
    public void setD(BigInteger d) {
        this.d = d;
    }

    //Get String Plaintext Kunci Blowfish
    public String getPlaintextkunciblowfish(){
        return plaintextkunciblowfish;
    }

    //Set String Plaintext Kunci Blowfish
    public void setPlaintextkunciblowfish(String plaintextkunciblowfish){
        this.plaintextkunciblowfish = plaintextkunciblowfish;
    }

    //Get Byte Plaintextkunciblowfish
    public byte[] getByteplaintextkunciblowfish(){
        return byteplaintextkunciblowfish;
    }

    //Set Byte Plaintextkunciblowfish
    public void setByteplaintextkunciblowfish(byte[] byteplaintextkunciblowfish){
        this.byteplaintextkunciblowfish = byteplaintextkunciblowfish;
    }

    //Get Byte HasilEnkripsikunciblowfish
    public byte[] getBytehasilEnkripsikunciblowfish(){
        return bytehasilEnkripsikunciblowfish;
    }

    //Set Byte HasilEnkripsikunciblowfish
    public void setBytehasilEnkripsikunciblowfish(byte[] bytehasilEnkripsikunciblowfish){
        this.bytehasilEnkripsikunciblowfish = bytehasilEnkripsikunciblowfish;
    }

    //Proses Mencari Nilai E
    private BigInteger generateE(BigInteger tn) {
        BigInteger gene;
        gene = BigInteger.probablePrime(PanjangBit, rand);
        do {
            gene = BigInteger.probablePrime(PanjangBit, rand);
        } while (tn.gcd(gene) == BigInteger.ONE);

        return gene;
    }

    //Proses Mencari Nilai D
    private BigInteger generateD(BigInteger e, BigInteger tn) {
        BigInteger gend;
        gend = e.modInverse(tn);
        return gend;
    }

    //Proses Enkripsi
    public static byte[] prosesEnkripsi(BigInteger e, BigInteger n, byte[] bytekalimat) {
        return (new BigInteger(bytekalimat)).modPow(e, n).toByteArray();
    }

}
