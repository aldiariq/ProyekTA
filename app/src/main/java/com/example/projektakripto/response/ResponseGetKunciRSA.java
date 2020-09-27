package com.example.projektakripto.response;

import com.google.gson.annotations.SerializedName;

public class ResponseGetKunciRSA<T> {
    @SerializedName("kunci_rsa")
    private T kunci_rsa;

    public T getKunci_rsa() {
        return kunci_rsa;
    }

    public void setKunci_rsa(T kunci_rsa) {
        this.kunci_rsa = kunci_rsa;
    }
}
