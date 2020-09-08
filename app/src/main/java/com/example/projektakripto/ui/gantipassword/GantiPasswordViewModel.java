package com.example.projektakripto.ui.gantipassword;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GantiPasswordViewModel extends ViewModel {
    private MutableLiveData<String> namaHalamangantipassword;

    public GantiPasswordViewModel() {
        namaHalamangantipassword = new MutableLiveData<>();
        namaHalamangantipassword.setValue("Ini Halaman Ganti Password");
    }

    public LiveData<String> getnamaHalamangantipassword() {
        return namaHalamangantipassword;
    }
}