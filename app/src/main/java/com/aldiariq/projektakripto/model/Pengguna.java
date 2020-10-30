package com.aldiariq.projektakripto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Pengguna implements Parcelable {

    //Atribut Model Pengguna
    @SerializedName("id_pengguna")
    private String id_pengguna;

    @SerializedName("email_pengguna")
    private String email_pengguna;

    @SerializedName("nama_pengguna")
    private String nama_pengguna;

    //Constructor Model Pengguna
    public Pengguna(Parcel in) {
        id_pengguna = in.readString();
        email_pengguna = in.readString();
        nama_pengguna = in.readString();
    }

    public static final Creator<Pengguna> CREATOR = new Creator<Pengguna>() {
        @Override
        public Pengguna createFromParcel(Parcel in) {
            return new Pengguna(in);
        }

        @Override
        public Pengguna[] newArray(int size) {
            return new Pengguna[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //Proses Penulisan Atribut Model Pengguna
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_pengguna);
        dest.writeString(email_pengguna);
        dest.writeString(nama_pengguna);
    }

    //Setter & Getter Atribut Model Pengguna
    public String getId_pengguna() {
        return id_pengguna;
    }

    public String getEmail_pengguna() {
        return email_pengguna;
    }

    public String getNama_pengguna() {
        return nama_pengguna;
    }
}
