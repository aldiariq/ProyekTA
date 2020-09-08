package com.example.projektakripto.ui.tentangaplikasi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TentangAplikasiViewModel extends ViewModel {
    private MutableLiveData<String> tentangAplikasi, pembimbingTugasakhir, pembuatAplikasi, introAplikasi;

    public TentangAplikasiViewModel() {
        tentangAplikasi = new MutableLiveData<>();
        tentangAplikasi.setValue("Aplikasi Penyimpanan Dokumen Online Terenkripsi");

        pembimbingTugasakhir = new MutableLiveData<>();
        pembimbingTugasakhir.setValue("Drs. Megah Mulya, M.T");

        pembuatAplikasi = new MutableLiveData<>();
        pembuatAplikasi.setValue("M Aldi Ariqi");;

        introAplikasi = new MutableLiveData<>();
        introAplikasi.setValue("Aplikasi ini Merupakan Aplikasi Penyimpanan Online File Dokumen Terenkripsi yang Dibuat Untuk Penyusunan Skripsi di Jurusan Teknik Informatika Dengan Topik Kriptografi Menggunakan Metode Hybrid Cryptosystem Blowfish dan RSA(Rivest Shamir Adleman)");
    }

    public LiveData<String> gettentangAplikasi() {
        return tentangAplikasi;
    }

    public LiveData<String> getpembimbingTugasakhir(){
        return pembimbingTugasakhir;
    }

    public LiveData<String> getpembuatAplikasi() {
        return pembuatAplikasi;
    }

    public LiveData<String> getintroAplikasi(){
        return introAplikasi;
    }
}