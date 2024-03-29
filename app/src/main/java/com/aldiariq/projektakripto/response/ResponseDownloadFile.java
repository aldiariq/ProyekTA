package com.aldiariq.projektakripto.response;

import com.aldiariq.projektakripto.model.FilePengguna;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDownloadFile<T> {
    @SerializedName("file_pengguna")
    private List<FilePengguna> file_pengguna;

    public List<FilePengguna> getFile_pengguna() {
        return file_pengguna;
    }

    public void setFile_pengguna(List<FilePengguna> download_file_pengguna) {
        this.file_pengguna = file_pengguna;
    }
}
