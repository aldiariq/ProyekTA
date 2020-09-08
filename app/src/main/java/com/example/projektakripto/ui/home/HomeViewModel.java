package com.example.projektakripto.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> namaHalamanhome;

    public HomeViewModel() {
        namaHalamanhome = new MutableLiveData<>();
        namaHalamanhome.setValue("Selamat Datang, ");
    }

    public LiveData<String> getNamahalamanhome() {
        return namaHalamanhome;
    }
}