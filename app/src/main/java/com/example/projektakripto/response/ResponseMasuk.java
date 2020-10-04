package com.example.projektakripto.response;

import com.example.projektakripto.model.Pengguna;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMasuk<T> {
    @SerializedName("berhasil")
    private boolean berhasil;
    @SerializedName("pesan")
    private String pesan;
    @SerializedName("pengguna")
    private List<Pengguna> pengguna;

    public boolean isBerhasil() {
        return berhasil;
    }

    public void setBerhasil(boolean berhasil) {
        this.berhasil = berhasil;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<Pengguna> getPengguna() {
        return pengguna;
    }

    public void setPengguna(List<Pengguna> pengguna) {
        this.pengguna = pengguna;
    }
}
