package com.example.projektakripto.response;

import com.example.projektakripto.model.KunciRSA;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetKunciRSA<T> {
    @SerializedName("kunci_rsa")
    private List<KunciRSA> kunci_rsa;

    public List<KunciRSA> getKunci_rsa() {
        return kunci_rsa;
    }

    public void setKunci_rsa(List<KunciRSA> kunci_rsa) {
        this.kunci_rsa = kunci_rsa;
    }
}
