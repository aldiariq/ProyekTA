package com.example.projektakripto.ui.penyimpanan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PenyimpananViewModel extends ViewModel {
    private MutableLiveData<String> namaHalamanpenyimpanan;

    public PenyimpananViewModel() {
        namaHalamanpenyimpanan = new MutableLiveData<>();
        namaHalamanpenyimpanan.setValue("Ini Halaman Penyimpanan");
    }

    public LiveData<String> getNamahalamanpenyimpanan() {
        return namaHalamanpenyimpanan;
    }
}