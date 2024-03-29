package com.aldiariq.projektakripto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class KunciRSA implements Parcelable {

    //Atribut Model File Pengguna
    @SerializedName("id_kunci")
    private String id_kunci;

    @SerializedName("id_pengguna")
    private String id_pengguna;

    @SerializedName("kunci_public")
    private String kunci_public;

    @SerializedName("kunci_modulus")
    private String kunci_modulus;

    //Constructor Model KunciRSA
    protected KunciRSA(Parcel in) {
        id_kunci = in.readString();
        id_pengguna = in.readString();
        kunci_public = in.readString();
        kunci_modulus = in.readString();
    }

    //Proses Penulisan Atribut Model Pengguna
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_kunci);
        dest.writeString(id_pengguna);
        dest.writeString(kunci_public);
        dest.writeString(kunci_modulus);
    }

    public static final Creator<KunciRSA> CREATOR = new Creator<KunciRSA>() {
        @Override
        public KunciRSA createFromParcel(Parcel in) {
            return new KunciRSA(in);
        }

        @Override
        public KunciRSA[] newArray(int size) {
            return new KunciRSA[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //Setter & Getter Atribut Model KunciRSA
    public String getId_kunci() {
        return id_kunci;
    }

    public void setId_kunci(String id_kunci) {
        this.id_kunci = id_kunci;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getKunci_public() {
        return kunci_public;
    }

    public void setKunci_public(String kunci_public) {
        this.kunci_public = kunci_public;
    }

    public String getKunci_modulus() {
        return kunci_modulus;
    }

    public void setKunci_modulus(String kunci_modulus) {
        this.kunci_modulus = kunci_modulus;
    }
}
