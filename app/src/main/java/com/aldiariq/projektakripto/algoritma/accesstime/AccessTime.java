package com.aldiariq.projektakripto.algoritma.accesstime;

public class AccessTime {
    private long waktumulai;
    private long waktuselesai;

    public AccessTime(long waktumulai, long waktuselesai){
        this.waktumulai = waktumulai;
        this.waktuselesai = waktuselesai;
    }

    private long getWaktumulai(){
        return this.waktumulai;
    }

    private long getWaktuselesai(){
        return this.waktuselesai;
    }

    public long hitungAccesstime(){
        return this.getWaktuselesai() - this.getWaktumulai();
    }
}
