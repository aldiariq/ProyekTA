package com.aldiariq.projektakripto.response;

import com.google.gson.annotations.SerializedName;

public class ResponseGetFile<T> {

    @SerializedName("file_pengguna")
    private T file_pengguna;

    public T getFile_pengguna() {
        return file_pengguna;
    }

    public void setFile_pengguna(T file_pengguna) {
        this.file_pengguna = file_pengguna;
    }
}
