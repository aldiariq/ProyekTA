package com.example.projektakripto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class KunciRSA implements Parcelable {
    @SerializedName("id_kunci")
    private String id_kunci;
    @SerializedName("id_pengguna")
    private String id_pengguna;
    @SerializedName("kunci_private")
    private String kunci_private;
    @SerializedName("kunci_public")
    private String kunci_public;

    protected KunciRSA(Parcel in) {
        id_kunci = in.readString();
        id_pengguna = in.readString();
        kunci_private = in.readString();
        kunci_public = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_kunci);
        dest.writeString(id_pengguna);
        dest.writeString(kunci_private);
        dest.writeString(kunci_public);
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

    public String getKunci_private() {
        return kunci_private;
    }

    public void setKunci_private(String kunci_private) {
        this.kunci_private = kunci_private;
    }

    public String getKunci_public() {
        return kunci_public;
    }

    public void setKunci_public(String kunci_public) {
        this.kunci_public = kunci_public;
    }
}
