package com.example.projektakripto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FilePengguna implements Parcelable {
    @SerializedName("id_file")
    private String id_file;
    @SerializedName("nama_file")
    private String nama_file;
    @SerializedName("id_pengguna")
    private String id_pengguna;
    @SerializedName("kunci_file")
    private String kunci_file;

    protected FilePengguna(Parcel in) {
        id_file = in.readString();
        nama_file = in.readString();
        id_pengguna = in.readString();
        kunci_file = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_file);
        dest.writeString(nama_file);
        dest.writeString(id_pengguna);
        dest.writeString(kunci_file);
    }

    public String getId_file() {
        return id_file;
    }

    public void setId_file(String id_file) {
        this.id_file = id_file;
    }

    public String getNama_file() {
        return nama_file;
    }

    public void setNama_file(String nama_file) {
        this.nama_file = nama_file;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getKunci_file() {
        return kunci_file;
    }

    public void setKunci_file(String kunci_file) {
        this.kunci_file = kunci_file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilePengguna> CREATOR = new Creator<FilePengguna>() {
        @Override
        public FilePengguna createFromParcel(Parcel in) {
            return new FilePengguna(in);
        }

        @Override
        public FilePengguna[] newArray(int size) {
            return new FilePengguna[size];
        }
    };
}
